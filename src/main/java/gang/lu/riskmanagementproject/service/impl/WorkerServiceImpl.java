package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.converter.WorkerConverter;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkerQueryDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerStatusCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerTypeCountVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.WorkerService;
import gang.lu.riskmanagementproject.util.PageHelper;
import gang.lu.riskmanagementproject.util.StatisticalUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.BusinessConstants.*;
import static gang.lu.riskmanagementproject.common.FailedMessages.*;

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
    private final WorkerConverter workerConverter;
    private final GeneralValidator generalValidator;


    /**
     * 组合条件分页查询工人
     *
     * @param workerQueryDTO 查询数据传输实体
     * @return 分页结果
     */
    @Override
    @BusinessLog(value = "组合查询工人", recordParams = true)
    public PageVO<WorkerVO> searchWorkers(WorkerQueryDTO workerQueryDTO) {
        // 1. 构建分页对象（直接传入继承了PageQueryDTO的workerQueryDTO）
        Page<Worker> poPage = PageHelper.buildPage(workerQueryDTO, GET_WORKER);
        // 2. 构建组合查询条件
        LambdaQueryWrapper<Worker> wrapper = buildWorkerQueryWrapper(workerQueryDTO);
        // 3. 分页查询
        poPage = page(poPage, wrapper);
        // 4. 复用PageConverter的转换逻辑
        return workerConverter.pagePoToPageVO(poPage);
    }


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
        generalValidator.validateStringNotBlank(workerCode, BusinessConstants.WORKER_CODE, BusinessConstants.ADD_WORKER);
        generalValidator.validateStringNotBlank(name, BusinessConstants.NAME, BusinessConstants.ADD_WORKER);
        // 2. 工号唯一性校验
        if (lambdaQuery().eq(Worker::getWorkerCode, workerCode).exists()) {
            throw new BizException(HttpStatus.CONFLICT, WORKER_CODE_DUPLICATE);
        }
        // 4. DTO→PO转换 + 设置时间字段
        Worker worker = workerConverter.dtoToPo(dto);
        worker.setCreateTime(LocalDateTime.now());
        worker.setUpdateTime(LocalDateTime.now());
        // 5. 插入数据库并校验结果
        int inserted = workerMapper.insert(worker);
        generalValidator.validateDbOperateResult(inserted);
    }

    /**
     * 删除工人（ID校验+存在性校验）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "删除工人", recordParams = true)
    public void deleteWorker(Long id) {
        generalValidator.validateIdExist(id, workerMapper, WORKER_NOT_EXIST);
        int deleted = workerMapper.deleteById(id);
        generalValidator.validateDbOperateResult(deleted);
    }

    /**
     * 更新工人（统一校验+枚举处理）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "更新工人", recordParams = true)
    public void updateWorker(Long id, WorkerDTO dto) {
        // 1. 校验工人存在性
        Worker existing = generalValidator.validateIdExist(id, workerMapper, WORKER_NOT_EXIST);
        // 2. 工号唯一性校验（仅当工号修改时）
        String newWorkerCode = dto.getWorkerCode();
        if (StrUtil.isNotBlank(newWorkerCode) && !newWorkerCode.equals(existing.getWorkerCode())) {
            boolean codeExists = lambdaQuery()
                    .eq(Worker::getWorkerCode, newWorkerCode)
                    .ne(Worker::getId, id)
                    .exists();
            if (codeExists) {
                throw new BizException(HttpStatus.CONFLICT, WORKER_CODE_DUPLICATE);
            }
        }
        // 4. 用DTO更新PO（空值不覆盖）
        Worker updated = new Worker();
        workerConverter.updatePoFromDto(dto, updated);
        updated.setId(id);
        updated.setCreateTime(existing.getCreateTime());
        updated.setUpdateTime(LocalDateTime.now());
        // 5. 更新数据库并校验结果
        int updatedRows = workerMapper.updateById(updated);
        generalValidator.validateDbOperateResult(updatedRows);

    }

    /**
     * 根据ID查询工人
     */
    @Override
    @BusinessLog(value = "查询工人（ID）", recordParams = true)
    public WorkerVO getWorkerById(Long id) {
        Worker worker = generalValidator.validateIdExist(id, workerMapper, WORKER_NOT_EXIST);
        return workerConverter.poToVo(worker);
    }

    /**
     * 根据工号查询工人
     */
    @Override
    @BusinessLog(value = "查询工人（工号）", recordParams = true)
    public WorkerVO getWorkerByCode(String workerCode) {
        // 1. 通用非空校验
        generalValidator.validateStringNotBlank(workerCode, BusinessConstants.WORKER_CODE, BusinessConstants.GET_WORKER_BY_WORKCODE);
        // 2. 查询工人
        Worker worker = getWorkerByCodeWithOutVerify(workerCode);
        if (ObjectUtil.isNull(worker)) {
            throw new BizException(HttpStatus.NOT_FOUND, WORKER_NOT_EXIST);
        }
        // 3. PO→VO转换
        return workerConverter.poToVo(worker);
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
            throw new BizException(HttpStatus.BAD_REQUEST, WORKER_DELETE_BATCH_ID_EMPTY);
        }
        // 2. 批量存在性校验（1次查询替代N次）
        List<Worker> existWorkers = workerMapper.selectBatchIds(ids);
        if (existWorkers.size() != ids.size()) {
            // 找出不存在的ID
            Set<Long> existIds = existWorkers.stream().map(Worker::getId).collect(Collectors.toSet());
            List<Long> notExistIds = ids.stream().filter(id -> !existIds.contains(id)).toList();
            throw new BizException(HttpStatus.NOT_FOUND,
                    String.format(WORKER_NOT_EXIST_BATCH, notExistIds));
        }
        // 3. 执行批量删除
        int deleted = workerMapper.deleteBatchIds(ids);
        generalValidator.validateDbOperateResult(deleted);
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
    private LambdaQueryWrapper<Worker> buildWorkerQueryWrapper(WorkerQueryDTO workerQueryDTO) {
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();

        // 1. 工号模糊匹配（原getWorkerByCode的模糊版）
        if (StrUtil.isNotBlank(workerQueryDTO.getWorkerCode())) {
            wrapper.like(Worker::getWorkerCode, workerQueryDTO.getWorkerCode().trim());
        }

        // 2. 姓名模糊匹配（原searchWorkersByName逻辑）
        if (StrUtil.isNotBlank(workerQueryDTO.getName())) {
            wrapper.like(Worker::getName, workerQueryDTO.getName().trim());
        }

        // 3. 岗位模糊匹配（原getWorkersByPosition逻辑）
        if (StrUtil.isNotBlank(workerQueryDTO.getPosition())) {
            wrapper.like(Worker::getPosition, workerQueryDTO.getPosition().trim());
        }

        // 4. 工龄区间筛选
        if (Objects.nonNull(workerQueryDTO.getMinWorkYears())) {
            wrapper.ge(Worker::getWorkYears, workerQueryDTO.getMinWorkYears());
        }
        if (Objects.nonNull(workerQueryDTO.getMaxWorkYears())) {
            wrapper.le(Worker::getWorkYears, workerQueryDTO.getMaxWorkYears());
        }

        // 5. 工种筛选（原getWorkersByWorkType逻辑）
        if (StrUtil.isNotBlank(workerQueryDTO.getWorkTypeValue())) {
            WorkType workType = workerConverter.stringToWorkType(workerQueryDTO.getWorkTypeValue());
            wrapper.eq(Worker::getWorkType, workType);
        }

        // 6. 状态筛选（原getWorkersByStatus逻辑）
        if (StrUtil.isNotBlank(workerQueryDTO.getStatusValue())) {
            Status status = workerConverter.stringToStatus(workerQueryDTO.getStatusValue());
            wrapper.eq(Worker::getStatus, status);
        }

        // 7. 排序（统一按更新时间降序）
        wrapper.orderByDesc(Worker::getUpdateTime);

        return wrapper;
    }
}
