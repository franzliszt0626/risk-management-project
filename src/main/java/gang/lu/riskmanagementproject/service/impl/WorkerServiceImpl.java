package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.WorkerVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.WorkerService;
import gang.lu.riskmanagementproject.util.ConvertUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import gang.lu.riskmanagementproject.validator.WorkerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static gang.lu.riskmanagementproject.common.BusinessScene.*;
import static gang.lu.riskmanagementproject.common.EnumName.STATUS;
import static gang.lu.riskmanagementproject.common.EnumName.WORK_TYPE;
import static gang.lu.riskmanagementproject.common.FieldName.NAME;
import static gang.lu.riskmanagementproject.common.FieldName.WORKER_CODE;

/**
 * <p>
 * 工人基本信息表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Service
@RequiredArgsConstructor
public class WorkerServiceImpl extends ServiceImpl<WorkerMapper, Worker> implements WorkerService {

    private final WorkerMapper workerMapper;

    private final WorkerValidator workerValidator;

    private final GeneralValidator generalValidator;

    /**
     * 创建工人（统一参数校验+枚举处理）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "创建工人", recordParams = true)
    public void createWorker(WorkerDTO dto) {
        // 1. 校验
        String workerCode = dto.getWorkerCode();
        String name = dto.getName();
        workerValidator.validateWorkerCode(workerCode, null, ADD_WORKER);
        generalValidator.validateStringNotBlank(name, NAME, ADD_WORKER);

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
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_OPERATE_CREATE_ERROR);
        }
    }

    /**
     * 删除工人（ID校验+存在性校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "删除工人", recordParams = true)
    public void deleteWorker(Long id) {
        workerValidator.validateWorkerExist(id);
        int deleted = workerMapper.deleteById(id);
        if (deleted != 1) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_OPERATE_DELETE_ERROR);
        }
    }

    /**
     * 更新工人（统一校验+枚举处理）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "更新工人", recordParams = true)
    public void updateWorker(Long id, WorkerDTO dto) {
        Worker existing = workerValidator.validateWorkerExist(id);

        // 工号唯一性校验
        String newWorkerCode = dto.getWorkerCode();
        if (StrUtil.isNotBlank(newWorkerCode) && !newWorkerCode.equals(existing.getWorkerCode())) {
            workerValidator.validateWorkerCode(newWorkerCode, id, UPDATE_WORKER);
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
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_OPERATE_UPDATE_ERROR);
        }

    }

    /**
     * 根据ID查询工人
     */
    @Override
    @BusinessLog(value = "查询工人（ID）", recordParams = true)
    public WorkerVO getWorkerById(Long id) {
        Worker worker = workerValidator.validateWorkerExist(id);
        return ConvertUtil.convert(worker, WorkerVO.class);
    }

    /**
     * 根据工号查询工人
     */
    @Override
    @BusinessLog(value = "查询工人（工号）", recordParams = true)
    public WorkerVO getWorkerByCode(String workerCode) {
        generalValidator.validateStringNotBlank(workerCode, WORKER_CODE, GET_WORKER_BY_WORKCODE);
        Worker worker = getWorkerByCodeWithOutVerify(workerCode);
        if (ObjectUtil.isNull(worker)) {
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }
        return ConvertUtil.convert(worker, WorkerVO.class);
    }

    /**
     * 按姓名模糊查询
     */
    @Override
    @BusinessLog(value = "按姓名模糊查询工人", recordParams = true)
    public List<WorkerVO> searchWorkersByName(String name) {
        if (StrUtil.isBlank(name)) {
            return Collections.emptyList();
        }
        String trimedName = name.trim();
        List<Worker> workers = lambdaQuery()
                .like(Worker::getName, trimedName)
                .orderByDesc(Worker::getUpdateTime)
                .list();
        if (ObjectUtil.isEmpty(workers)) {
            return Collections.emptyList();
        }
        return ConvertUtil.convertList(workers, WorkerVO.class);
    }

    /**
     * 按状态查询（枚举直接作为查询条件）
     */
    @Override
    @BusinessLog(value = "按状态查询工人", recordParams = true)
    public List<WorkerVO> getWorkersByStatus(Status status) {
        generalValidator.validateEnumNotNull(status, STATUS, GET_WORKER_BY_STATUS);

        List<Worker> workers = lambdaQuery()
                .eq(Worker::getStatus, status)
                .orderByDesc(Worker::getUpdateTime)
                .list();

        if (ObjectUtil.isEmpty(workers)) {
            return Collections.emptyList();
        }

        return ConvertUtil.convertList(workers, WorkerVO.class);
    }

    /**
     * 按工种查询（适配WorkType枚举）
     */
    @Override
    @BusinessLog(value = "按工种查询工人", recordParams = true)
    public List<WorkerVO> getWorkersByWorkType(WorkType workType) {
        generalValidator.validateEnumNotNull(workType, WORK_TYPE, GET_WORKER_BY_WORKTYPE);
        List<Worker> workers = lambdaQuery()
                .eq(Worker::getWorkType, workType)
                .orderByDesc(Worker::getUpdateTime)
                .list();
        if (ObjectUtil.isEmpty(workers)) {
            return Collections.emptyList();
        }
        return ConvertUtil.convertList(workers, WorkerVO.class);
    }

    /**
     * 按岗位查询
     */
    @Override
    @BusinessLog(value = "按岗位查询工人", recordParams = true)
    public List<WorkerVO> getWorkersByPosition(String position) {
        if (StrUtil.isBlank(position)) {
            return Collections.emptyList();
        }
        String trimedPosition = position.trim();
        List<Worker> workers = lambdaQuery()
                .eq(Worker::getPosition, trimedPosition)
                .orderByDesc(Worker::getUpdateTime)
                .list();
        if (ObjectUtil.isEmpty(workers)) {
            return Collections.emptyList();
        }
        return ConvertUtil.convertList(workers, WorkerVO.class);
    }

    /**
     * 分页查询所有工人（统一分页参数处理）
     */
    @Override
    @BusinessLog(value = "分页查询所有工人", recordParams = true)
    public Page<WorkerVO> getAllWorkers(Integer pageNum, Integer pageSize) {
        // 分页参数处理
        Integer[] pageParams = workerValidator.handleWorkerPageParams(pageNum, pageSize);
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
        return voPage;
    }


    /**
     * 内部复用
     */
    private Worker getWorkerByCodeWithOutVerify(String workerCode) {
        if (StrUtil.isBlank(workerCode)) {
            return null;
        }
        return lambdaQuery().eq(Worker::getWorkerCode, workerCode).one();
    }
}
