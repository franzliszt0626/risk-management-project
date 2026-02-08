package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        // 1. 通用校验：工号+姓名非空
        String workerCode = dto.getWorkerCode();
        String name = dto.getName();
        BusinessVerifyUtil.validateWorkerCode(workerCode, "创建工人");
        if (StrUtil.isBlank(name)) {
            log.warn("【创建工人失败】姓名为空，工号：{}", workerCode);
            throw new BizException(FailureMessages.WORKER_PARAM_EMPTY_NAME);
        }

        // 2. 校验工号唯一性
        BusinessVerifyUtil.validateWorkerCodeUnique(workerCode, null, "创建工人");

        // 3. 枚举校验（Status/WorkType）
        if (ObjectUtil.isNotNull(dto.getStatus())) {
            BusinessVerifyUtil.validateStatus(dto.getStatus(), "创建工人");
        }
        if (ObjectUtil.isNotNull(dto.getWorkType())) {
            BusinessVerifyUtil.validateWorkType(dto.getWorkType(), "创建工人");
        }

        // 4. DTO转PO（枚举直接赋值）
        Worker worker = convertToPO(dto);
        worker.setCreateTime(LocalDateTime.now());
        worker.setUpdateTime(LocalDateTime.now());

        // 5. 保存数据并校验结果
        int inserted = workerMapper.insert(worker);
        if (inserted != 1) {
            log.error("【创建工人失败】数据库插入异常，工号：{}", workerCode);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_OPERATE_CREATE_ERROR);
        }

        log.info("【创建工人成功】工号：{}，姓名：{}，状态：{}，工种：{}",
                workerCode, name,
                ObjectUtil.isNull(worker.getStatus()) ? "无" : worker.getStatus().getValue(),
                ObjectUtil.isNull(worker.getWorkType()) ? "无" : worker.getWorkType().getValue());
    }

    /**
     * 删除工人（ID校验+存在性校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorker(Long id) {
        // 1. 通用ID校验
        BusinessVerifyUtil.validateId(id, "工人ID");

        // 2. 校验工人存在性
        Worker existing = workerMapper.selectById(id);
        if (ObjectUtil.isNull(existing)) {
            log.warn("【删除工人失败】工人不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }

        // 3. 删除数据并校验结果
        int deleted = workerMapper.deleteById(id);
        if (deleted != 1) {
            log.error("【删除工人失败】数据库删除异常，ID：{}", id);
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
        // 1. 通用ID校验+存在性校验
        BusinessVerifyUtil.validateId(id, "工人ID");
        Worker existing = workerMapper.selectById(id);
        if (ObjectUtil.isNull(existing)) {
            log.warn("【更新工人失败】工人不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }

        // 2. 校验工号唯一性（若修改工号）
        String newWorkerCode = dto.getWorkerCode();
        if (StrUtil.isNotBlank(newWorkerCode) && !newWorkerCode.equals(existing.getWorkerCode())) {
            BusinessVerifyUtil.validateWorkerCodeUnique(newWorkerCode, id, "更新工人");
        }

        // 3. 枚举校验（Status/WorkType）
        if (ObjectUtil.isNotNull(dto.getStatus())) {
            BusinessVerifyUtil.validateStatus(dto.getStatus(), "更新工人");
        }
        if (ObjectUtil.isNotNull(dto.getWorkType())) {
            BusinessVerifyUtil.validateWorkType(dto.getWorkType(), "更新工人");
        }

        // 4. 构建更新对象
        Worker updated = convertToPO(dto);
        updated.setId(id);
        updated.setCreateTime(existing.getCreateTime());
        updated.setUpdateTime(LocalDateTime.now());

        // 5. 更新数据并校验结果
        int updatedRows = workerMapper.updateById(updated);
        if (updatedRows != 1) {
            log.error("【更新工人失败】数据库更新异常，ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_OPERATE_UPDATE_ERROR);
        }

        log.info("【更新工人成功】ID：{}，原工号：{}，新工号：{}",
                id, existing.getWorkerCode(), newWorkerCode);
    }

    /**
     * 根据ID查询工人
     */
    @Override
    public WorkerVO getWorkerById(Long id) {
        BusinessVerifyUtil.validateId(id, "工人ID");
        Worker worker = workerMapper.selectById(id);
        if (ObjectUtil.isNull(worker)) {
            log.warn("【查询工人失败】工人不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }
        return convertToVO(worker);
    }

    /**
     * 根据工号查询工人
     */
    @Override
    public WorkerVO getWorkerByCode(String workerCode) {
        BusinessVerifyUtil.validateWorkerCode(workerCode, "根据工号查询工人");
        Worker worker = getWorkerByCodeWithOutVerify(workerCode);
        if (ObjectUtil.isNull(worker)) {
            log.warn("【查询工人失败】工人不存在，工号：{}", workerCode);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }
        return convertToVO(worker);
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

        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Worker::getName, name.trim());
        wrapper.orderByDesc(Worker::getUpdateTime);

        List<Worker> workers = workerMapper.selectList(wrapper);
        if (ObjectUtil.isEmpty(workers)) {
            log.info("【查询工人】姓名模糊匹配：{}，暂无记录", name);
            return Collections.emptyList();
        }

        List<WorkerVO> voList = workers.stream().map(this::convertToVO).collect(Collectors.toList());
        log.info("【查询工人成功】姓名模糊匹配：{}，共{}条", name, voList.size());
        return voList;
    }

    /**
     * 按状态查询（枚举直接作为查询条件）
     */
    @Override
    public List<WorkerVO> getWorkersByStatus(Status status) {
        BusinessVerifyUtil.validateStatus(status, "按状态查询工人");

        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Worker::getStatus, status);
        wrapper.orderByDesc(Worker::getUpdateTime);

        List<Worker> workers = workerMapper.selectList(wrapper);
        if (ObjectUtil.isEmpty(workers)) {
            log.info("【查询工人】状态：{}，暂无记录", status.getValue());
            return Collections.emptyList();
        }

        List<WorkerVO> voList = workers.stream().map(this::convertToVO).collect(Collectors.toList());
        log.info("【查询工人成功】状态：{}，共{}条", status.getValue(), voList.size());
        return voList;
    }

    /**
     * 新增：按工种查询（适配WorkType枚举）
     */
    @Override
    public List<WorkerVO> getWorkersByWorkType(WorkType workType) {
        BusinessVerifyUtil.validateWorkType(workType, "按工种查询工人");

        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Worker::getWorkType, workType);
        wrapper.orderByDesc(Worker::getUpdateTime);

        List<Worker> workers = workerMapper.selectList(wrapper);
        if (ObjectUtil.isEmpty(workers)) {
            log.info("【查询工人】工种：{}，暂无记录", workType.getValue());
            return Collections.emptyList();
        }

        List<WorkerVO> voList = workers.stream().map(this::convertToVO).collect(Collectors.toList());
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

        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Worker::getPosition, position.trim());
        wrapper.orderByDesc(Worker::getUpdateTime);

        List<Worker> workers = workerMapper.selectList(wrapper);
        if (ObjectUtil.isEmpty(workers)) {
            log.info("【查询工人】岗位：{}，暂无记录", position);
            return Collections.emptyList();
        }

        List<WorkerVO> voList = workers.stream().map(this::convertToVO).collect(Collectors.toList());
        log.info("【查询工人成功】岗位：{}，共{}条", position, voList.size());
        return voList;
    }

    /**
     * 分页查询所有工人（统一分页参数处理）
     */
    @Override
    public Page<WorkerVO> getAllWorkers(Integer pageNum, Integer pageSize) {
        // 1. 统一处理分页参数（默认20，最大50）
        Integer[] pageParams = BusinessVerifyUtil.handleWorkAreaPageParams(pageNum, pageSize);
        int finalPageNum = pageParams[0];
        int finalPageSize = pageParams[1];

        // 2. 分页查询
        Page<Worker> workerPage = new Page<>(finalPageNum, finalPageSize);
        Page<Worker> resultPage = lambdaQuery()
                .orderByDesc(Worker::getUpdateTime)
                .page(workerPage);

        // 3. PO转VO分页对象
        Page<WorkerVO> voPage = new Page<>();
        BeanUtil.copyProperties(resultPage, voPage);
        voPage.setRecords(resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));

        log.info("【分页查询工人成功】第{}页，每页{}条，总条数{}",
                finalPageNum, finalPageSize, resultPage.getTotal());
        return voPage;
    }

    // ========== 私有工具方法（共性抽取） ==========

    /**
     * DTO转PO（枚举直接赋值）
     */
    private Worker convertToPO(WorkerDTO dto) {
        if (ObjectUtil.isNull(dto)) {
            return null;
        }
        Worker worker = BeanUtil.copyProperties(dto, Worker.class);
        // 枚举字段直接赋值（无需转换，转换器已处理）
        worker.setStatus(dto.getStatus());
        worker.setWorkType(dto.getWorkType());
        return worker;
    }

    /**
     * PO转VO（枚举直接拷贝）
     */
    private WorkerVO convertToVO(Worker po) {
        if (ObjectUtil.isNull(po)) {
            return null;
        }
        WorkerVO vo = BeanUtil.copyProperties(po, WorkerVO.class);
        // 枚举字段直接拷贝，VO中对应字段也是枚举类型
        vo.setStatus(po.getStatus());
        vo.setWorkType(po.getWorkType());
        return vo;
    }

    /**
     * 仅查询工号，不做校验（内部复用）
     */
    public Worker getWorkerByCodeWithOutVerify(String workerCode) {
        if (StrUtil.isBlank(workerCode)) {
            return null;
        }
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Worker::getWorkerCode, workerCode);
        return workerMapper.selectOne(wrapper);
    }
}
