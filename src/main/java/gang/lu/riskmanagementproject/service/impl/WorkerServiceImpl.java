package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import gang.lu.riskmanagementproject.domain.query.WorkerQuery;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerStatusCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerTypeCountVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.WorkerService;
import gang.lu.riskmanagementproject.util.ConvertUtil;
import gang.lu.riskmanagementproject.util.PageUtil;
import gang.lu.riskmanagementproject.util.StatisticalUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import gang.lu.riskmanagementproject.validator.WorkerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.BusinessScene.*;
import static gang.lu.riskmanagementproject.common.EnumName.STATUS;
import static gang.lu.riskmanagementproject.common.EnumName.WORK_TYPE;
import static gang.lu.riskmanagementproject.common.FieldName.*;

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
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_CREATE_ERROR);
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
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_DELETE_ERROR);
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
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_UPDATE_ERROR);
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
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_NOT_EXIST);
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
                .like(Worker::getPosition, trimedPosition)
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
        Page<Worker> workerPage = PageUtil.buildPage(pageNum, pageSize, GET_WORKER);
        // 分页查询
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Worker::getUpdateTime);
        // 3. 分页查询（MyBatis-Plus自动计算总数，无需手动count）
        workerPage = page(workerPage, wrapper);
        // 4. 转换VO（无需手动补全total/pages）
        return ConvertUtil.convertPageWithManualTotal(workerPage, WorkerVO.class);
    }

    /**
     * 按工人状态统计数量
     *
     * @return 状态统计结果VO
     */
    @Override
    @BusinessLog(value = "统计工人状态数量", recordParams = false)
    public WorkerStatusCountVO countWorkerByStatus() {
        // 调用Mapper获取按状态分组的统计结果（现在是List<Map>）
        List<Map<String, Object>> statusCountList = baseMapper.countWorkerByStatus();
        // 封装VO对象（使用新增的工具方法）
        WorkerStatusCountVO vo = new WorkerStatusCountVO();
        vo.setNormalCount(StatisticalUtil.getCountFromList(statusCountList, WORKER_STATUS, Status.NORMAL.getValue()));
        vo.setAbnormalCount(StatisticalUtil.getCountFromList(statusCountList, WORKER_STATUS, Status.ABNORMAL.getValue()));
        vo.setOfflineCount(StatisticalUtil.getCountFromList(statusCountList, WORKER_STATUS, Status.OFFLINE.getValue()));
        // 计算总数量
        vo.setTotalCount(vo.getNormalCount() + vo.getAbnormalCount() + vo.getOfflineCount());
        return vo;
    }

    /**
     * 按工人种类统计数量
     *
     * @return 状态统计结果VO
     */
    @BusinessLog(value = "统计工人种类数量", recordParams = false)
    @Override
    public WorkerTypeCountVO countWorkerByWorkType() {
        // 调用Mapper获取按状态分组的统计结果（现在是List<Map>）
        List<Map<String, Object>> workTypeCountList = baseMapper.countWorkerByWorkType();
        // 封装VO对象（使用新增的工具方法）
        WorkerTypeCountVO vo = new WorkerTypeCountVO();
        vo.setHighAltitudeCount(StatisticalUtil.getCountFromList(workTypeCountList, WORKER_TYPE, WorkType.HIGH_ALTITUDE.getValue()));
        vo.setConfinedSpaceCount(StatisticalUtil.getCountFromList(workTypeCountList, WORKER_TYPE, WorkType.CONFINED_SPACE.getValue()));
        vo.setEquipmentOperationCount(StatisticalUtil.getCountFromList(workTypeCountList, WORKER_TYPE, WorkType.EQUIPMENT_OPERATION.getValue()));
        vo.setNormalWorkCount(StatisticalUtil.getCountFromList(workTypeCountList, WORKER_TYPE, WorkType.NORMAL_WORK.getValue()));
        // 计算总数量
        vo.setTotalCount(vo.getHighAltitudeCount() + vo.getConfinedSpaceCount() + vo.getEquipmentOperationCount() + vo.getNormalWorkCount());
        return vo;
    }

    /**
     * 批量删除工人
     *
     * @param ids id们
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "批量删除工人", recordParams = true)
    public void batchDeleteWorkers(List<Long> ids) {
        // 1. 空值校验
        if (CollUtil.isEmpty(ids)) {
            throw new BizException(HttpStatus.BAD_REQUEST, FailureMessages.WORKER_DELETE_BATCH_ID_EMPTY);
        }
        // 2. 批量存在性校验（1次查询替代N次）
        List<Worker> existWorkers = workerMapper.selectBatchIds(ids);
        if (existWorkers.size() != ids.size()) {
            // 找出不存在的ID
            Set<Long> existIds = existWorkers.stream().map(Worker::getId).collect(Collectors.toSet());
            List<Long> notExistIds = ids.stream().filter(id -> !existIds.contains(id)).toList();
            throw new BizException(HttpStatus.NOT_FOUND,
                    String.format(FailureMessages.WORKER_NOT_EXIST_BATCH, notExistIds));
        }
        // 3. 执行批量删除
        int deleted = workerMapper.deleteBatchIds(ids);
        if (deleted != ids.size()) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.WORKER_DELETE_BATCH_ERROR);
        }
    }

    /**
     * 组合条件分页查询工人
     *
     * @param workerQuery 查询数据传输实体
     * @return 分页结果
     */
    @Override
    @BusinessLog(value = "组合查询工人", recordParams = true)
    public Page<WorkerVO> searchWorkers(WorkerQuery workerQuery) {
        // 1. 构建分页对象
        Page<Worker> workerPage = PageUtil.buildPage(workerQuery.getPageNum(), workerQuery.getPageSize(), GET_WORKER);
        // 2. 构建组合查询条件（逻辑不变）
        LambdaQueryWrapper<Worker> wrapper = buildWorkerQueryWrapper(workerQuery);
        // 3. 分页查询（自动计算总数）
        workerPage = page(workerPage, wrapper);
        // 4. 转换VO
        return ConvertUtil.convertPageWithManualTotal(workerPage, WorkerVO.class);
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

    /**
     * 建立查询条件
     */
    private LambdaQueryWrapper<Worker> buildWorkerQueryWrapper(WorkerQuery workerQuery) {
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        // 工号模糊匹配
        if (StrUtil.isNotBlank(workerQuery.getWorkerCode())) {
            wrapper.like(Worker::getWorkerCode, workerQuery.getWorkerCode().trim());
        }
        // 姓名模糊匹配
        if (StrUtil.isNotBlank(workerQuery.getName())) {
            wrapper.like(Worker::getName, workerQuery.getName().trim());
        }
        // 岗位模糊匹配
        if (StrUtil.isNotBlank(workerQuery.getPosition())) {
            wrapper.like(Worker::getPosition, workerQuery.getPosition().trim());
        }
        // 工龄区间
        if (Objects.nonNull(workerQuery.getMinWorkYears())) {
            wrapper.ge(Worker::getWorkYears, workerQuery.getMinWorkYears());
        }
        if (Objects.nonNull(workerQuery.getMaxWorkYears())) {
            wrapper.le(Worker::getWorkYears, workerQuery.getMaxWorkYears());
        }
        // 工作类型（枚举转换）
        if (StrUtil.isNotBlank(workerQuery.getWorkTypeValue())) {
            WorkType workType = WorkType.fromValue(workerQuery.getWorkTypeValue());
            wrapper.eq(Worker::getWorkType, workType);
        }
        // 状态（枚举转换）
        if (StrUtil.isNotBlank(workerQuery.getStatusValue())) {
            Status status = Status.fromValue(workerQuery.getStatusValue());
            wrapper.eq(Worker::getStatus, status);
        }
        // 排序
        wrapper.orderByDesc(Worker::getUpdateTime);
        return wrapper;
    }
}
