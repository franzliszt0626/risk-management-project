package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.WorkerVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.WorkerService;
import gang.lu.riskmanagementproject.util.BusinessVerifyUtil;
import gang.lu.riskmanagementproject.util.ConvertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static gang.lu.riskmanagementproject.common.BusinessScene.*;
import static gang.lu.riskmanagementproject.common.IdName.WORKER_ID;

/**
 * <p>
 * 工人基本信息表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerServiceImpl extends ServiceImpl<WorkerMapper, Worker> implements WorkerService {

    private final WorkerMapper workerMapper;

    /**
     * 创建工人（统一参数校验+枚举处理）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWorker(WorkerDTO dto) {
        // 1. 校验
        String workerCode = dto.getWorkerCode();
        String name = dto.getName();
        BusinessVerifyUtil.validateWorkerCode(workerCode, null, ADD_WORKER);
        BusinessVerifyUtil.validateStringNotBlank(name, "姓名", ADD_WORKER);

        // 2. 枚举默认值
        if (ObjectUtil.isNull(dto.getStatus())) {
            dto.setStatus(Status.NORMAL);
        }
        if (ObjectUtil.isNull(dto.getWorkType())) {
            dto.setWorkType(WorkType.NORMAL_WORK);
        }

        // 3. 转换+保存
        Worker worker = ConvertUtil.convert(dto, Worker.class);
        worker.setCreateTime(LocalDateTime.now());
        worker.setUpdateTime(LocalDateTime.now());

        int inserted = workerMapper.insert(worker);
        if (inserted != 1) {
            log.error("【创建工人失败】数据库异常，工号：{}", workerCode);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_OPERATE_CREATE_ERROR);
        }

        log.info("【创建工人成功】工号：{}，姓名：{}", workerCode, name);
    }

    /**
     * 删除工人（ID校验+存在性校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorker(Long id) {
        Worker existing = validateWorkerExist(id);
        int deleted = workerMapper.deleteById(id);
        if (deleted != 1) {
            log.error("【删除工人失败】数据库异常，ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_OPERATE_DELETE_ERROR);
        }
        log.info("【删除工人成功】ID：{}，工号：{}", id, existing.getWorkerCode());
    }

    /**
     * 更新工人（统一校验+枚举处理）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorker(Long id, WorkerDTO dto) {
        Worker existing = validateWorkerExist(id);

        // 工号唯一性校验
        String newWorkerCode = dto.getWorkerCode();
        if (StrUtil.isNotBlank(newWorkerCode) && !newWorkerCode.equals(existing.getWorkerCode())) {
            BusinessVerifyUtil.validateWorkerCode(newWorkerCode, id, UPDATE_WORKER);
        }

        // 枚举默认值
        if (ObjectUtil.isNull(dto.getStatus())) {
            dto.setStatus(existing.getStatus());
        }
        if (ObjectUtil.isNull(dto.getWorkType())) {
            dto.setWorkType(existing.getWorkType());
        }

        // 转换+更新
        Worker updated = ConvertUtil.convert(dto, Worker.class);
        updated.setId(id);
        updated.setCreateTime(existing.getCreateTime());
        updated.setUpdateTime(LocalDateTime.now());

        int updatedRows = workerMapper.updateById(updated);
        if (updatedRows != 1) {
            log.error("【更新工人失败】数据库异常，ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_OPERATE_UPDATE_ERROR);
        }

        log.info("【更新工人成功】ID：{}，原工号：{}，新工号：{}", id, existing.getWorkerCode(), newWorkerCode);
    }

    /**
     * 根据ID查询工人
     */
    @Override
    public WorkerVO getWorkerById(Long id) {
        Worker worker = validateWorkerExist(id);
        return ConvertUtil.convert(worker, WorkerVO.class);
    }

    /**
     * 根据工号查询工人
     */
    @Override
    public WorkerVO getWorkerByCode(String workerCode) {
        BusinessVerifyUtil.validateStringNotBlank(workerCode, "工号", GET_WORKER_BY_WORKCODE);
        Worker worker = getWorkerByCodeWithOutVerify(workerCode);
        if (ObjectUtil.isNull(worker)) {
            log.warn("【查询工人失败】工号：{} 不存在", workerCode);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }
        return ConvertUtil.convert(worker, WorkerVO.class);
    }

    /**
     * 按姓名模糊查询
     */
    @Override
    public List<WorkerVO> searchWorkersByName(String name) {
        if (StrUtil.isBlank(name)) {
            log.warn("【查询工人失败】姓名为空");
            return Collections.emptyList();
        }

        String trimedName = name.trim();
        List<Worker> workers = lambdaQuery()
                .like(Worker::getName, trimedName)
                .orderByDesc(Worker::getUpdateTime)
                .list();

        if (ObjectUtil.isEmpty(workers)) {
            log.info("【查询工人】姓名：{} 暂无记录", trimedName);
            return Collections.emptyList();
        }

        List<WorkerVO> voList = ConvertUtil.convertList(workers, WorkerVO.class);
        log.info("【查询工人成功】姓名：{}，共{}条", trimedName, voList.size());
        return voList;
    }

    /**
     * 按状态查询（枚举直接作为查询条件）
     */
    @Override
    public List<WorkerVO> getWorkersByStatus(Status status) {
        BusinessVerifyUtil.validateEnumNotNull(status, "工人状态", GET_WORKER_BY_STATUS);

        List<Worker> workers = lambdaQuery()
                .eq(Worker::getStatus, status)
                .orderByDesc(Worker::getUpdateTime)
                .list();

        if (ObjectUtil.isEmpty(workers)) {
            log.info("【查询工人】状态：{} 暂无记录", status.getValue());
            return Collections.emptyList();
        }

        List<WorkerVO> voList = ConvertUtil.convertList(workers, WorkerVO.class);
        log.info("【查询工人成功】状态：{}，共{}条", status.getValue(), voList.size());
        return voList;
    }

    /**
     * 按工种查询（适配WorkType枚举）
     */
    @Override
    public List<WorkerVO> getWorkersByWorkType(WorkType workType) {
        BusinessVerifyUtil.validateEnumNotNull(workType, "工种", GET_WORKER_BY_WORKTYPE);

        List<Worker> workers = lambdaQuery()
                .eq(Worker::getWorkType, workType)
                .orderByDesc(Worker::getUpdateTime)
                .list();

        if (ObjectUtil.isEmpty(workers)) {
            log.info("【查询工人】工种：{} 暂无记录", workType.getValue());
            return Collections.emptyList();
        }

        List<WorkerVO> voList = ConvertUtil.convertList(workers, WorkerVO.class);
        log.info("【查询工人成功】工种：{}，共{}条", workType.getValue(), voList.size());
        return voList;
    }

    /**
     * 按岗位查询
     */
    @Override
    public List<WorkerVO> getWorkersByPosition(String position) {
        if (StrUtil.isBlank(position)) {
            log.warn("【查询工人失败】岗位为空");
            return Collections.emptyList();
        }

        String trimedPosition = position.trim();
        List<Worker> workers = lambdaQuery()
                .eq(Worker::getPosition, trimedPosition)
                .orderByDesc(Worker::getUpdateTime)
                .list();

        if (ObjectUtil.isEmpty(workers)) {
            log.info("【查询工人】岗位：{} 暂无记录", trimedPosition);
            return Collections.emptyList();
        }

        List<WorkerVO> voList = ConvertUtil.convertList(workers, WorkerVO.class);
        log.info("【查询工人成功】岗位：{}，共{}条", trimedPosition, voList.size());
        return voList;
    }

    /**
     * 分页查询所有工人（统一分页参数处理）
     */
    @Override
    public Page<WorkerVO> getAllWorkers(Integer pageNum, Integer pageSize) {
        // 分页参数处理
        Integer[] pageParams = BusinessVerifyUtil.handleWorkerPageParams(pageNum, pageSize);
        int finalPageNum = pageParams[0];
        int finalPageSize = pageParams[1];

        // 分页查询
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Worker::getUpdateTime);

        long total = count(wrapper);
        Page<Worker> workerPage = new Page<>(finalPageNum, finalPageSize);
        workerPage = page(workerPage, wrapper);
        workerPage.setTotal(total);
        workerPage.setPages(total > 0 ? (total + finalPageSize - 1) / finalPageSize : 0);

        // 转换分页VO
        Page<WorkerVO> voPage = ConvertUtil.convertPageWithManualTotal(workerPage, WorkerVO.class);
        log.info("【分页查询工人成功】第{}页，总条数{}", finalPageNum, total);
        return voPage;
    }

    // ========== 私有工具方法（共性抽取） ==========

    /**
     * 核心校验
     */
    private Worker validateWorkerExist(Long id) {
        BusinessVerifyUtil.validateId(id, WORKER_ID);
        Worker worker = workerMapper.selectById(id);
        if (ObjectUtil.isNull(worker)) {
            log.warn("【工人操作失败】记录不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }
        return worker;
    }

    /**
     * 内部复用
     */
    public Worker getWorkerByCodeWithOutVerify(String workerCode) {
        if (StrUtil.isBlank(workerCode)) {
            return null;
        }
        return lambdaQuery().eq(Worker::getWorkerCode, workerCode).one();
    }

}
