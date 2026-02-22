package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.converter.WorkerConverter;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkerQueryDTO;
import gang.lu.riskmanagementproject.domain.enums.field.Status;
import gang.lu.riskmanagementproject.domain.enums.field.WorkType;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerStatusCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerTypeCountVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.helper.QueryWrapperHelper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.WorkerService;
import gang.lu.riskmanagementproject.util.StatisticalUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.BusinessConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * <p>
 * 工人基本信息表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Service
public class WorkerServiceImpl
        extends BaseCrudServiceImpl<Worker, WorkerDTO, WorkerVO, WorkerQueryDTO, WorkerMapper, WorkerConverter>
        implements WorkerService {

    public WorkerServiceImpl(WorkerMapper baseMapper,
                             WorkerConverter converter,
                             GeneralValidator generalValidator) {
        super(baseMapper, converter, generalValidator);
    }

    // ======================== 通用CRUD ========================

    /**
     * 新增工人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = ADD_WORKER, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public WorkerVO add(WorkerDTO dto) {
        return super.add(dto);
    }

    /**
     * 删除工人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = DELETE_WORKER, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void delete(Long id) {
        super.delete(id);
    }

    /**
     * 批量删除工人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = BATCH_DELETE_WORKER, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void batchDelete(Iterable<Long> ids) {
        super.batchDelete(ids);
    }

    /**
     * 修改工人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = UPDATE_WORKER, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public WorkerVO update(Long id, WorkerDTO dto) {
        return super.update(id, dto);
    }

    /**
     * 根据ID查询工人
     */
    @Override
    @BusinessLog(value = GET_WORKER_BY_ID, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public WorkerVO getOneById(Long id) {
        return super.getOneById(id);
    }

    /**
     * 多条件分页查询工人
     */
    @Override
    @BusinessLog(value = GET_WORKER_BY_MULTIPLY_CONDITION, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public PageVO<WorkerVO> search(WorkerQueryDTO queryDTO) {
        return super.search(queryDTO);
    }

    // ======================== 校验方法 ========================

    /**
     * 新增校验
     */
    @Override
    public void validateAdd(WorkerDTO dto) {
        generalValidator.validateStringNotBlank(dto.getWorkerCode(), BusinessConstants.WORKER_CODE, ADD_WORKER);
        generalValidator.validateStringNotBlank(dto.getName(), BusinessConstants.NAME, ADD_WORKER);

        if (lambdaQuery().eq(Worker::getWorkerCode, dto.getWorkerCode()).exists()) {
            throw new BizException(HttpStatus.CONFLICT,
                    String.format(WORKER_CODE_DUPLICATE, dto.getWorkerCode()));
        }
    }

    /**
     * 修改校验
     */
    @Override
    public void validateUpdate(Long id, WorkerDTO dto) {
        String newCode = dto.getWorkerCode();
        if (StrUtil.isBlank(newCode)) {
            return;
        }

        boolean exists = lambdaQuery()
                .eq(Worker::getWorkerCode, newCode)
                .ne(Worker::getId, id)
                .exists();

        if (exists) {
            throw new BizException(HttpStatus.CONFLICT,
                    String.format(WORKER_CODE_DUPLICATE, newCode));
        }
    }

    /**
     * 查询校验
     */
    @Override
    public void validateSearch(WorkerQueryDTO queryDTO) {
        generalValidator.validateMinMaxRange(queryDTO.getMinWorkYears(), queryDTO.getMaxWorkYears(), WORK_YEAR);
    }

    /**
     * 构建查询条件
     */
    @Override
    public LambdaQueryWrapper<Worker> buildQueryWrapper(WorkerQueryDTO queryDTO) {
        return QueryWrapperHelper.<Worker>create()
                .likeIfPresent(Worker::getWorkerCode, queryDTO.getWorkerCode())
                .likeIfPresent(Worker::getName, queryDTO.getName())
                .likeIfPresent(Worker::getPosition, queryDTO.getPosition())
                .eqEnumIfPresent(Worker::getWorkType, queryDTO.getWorkTypeValue(), WorkType.class)
                .eqEnumIfPresent(Worker::getStatus, queryDTO.getStatusValue(), Status.class)
                .geIfPresent(Worker::getWorkYears, queryDTO.getMinWorkYears())
                .leIfPresent(Worker::getWorkYears, queryDTO.getMaxWorkYears())
                .orderByDesc(Worker::getUpdateTime);
    }

    // ======================== 模板方法 ========================

    @Override
    protected String getNotFoundMsg() {
        return WORKER_NOT_EXIST;
    }

    @Override
    protected String getBatchIdEmptyMsg() {
        return WORKER_DELETE_BATCH_ID_EMPTY;
    }

    @Override
    protected String getBatchNotFoundMsg() {
        return WORKER_NOT_EXIST_BATCH;
    }

    @Override
    protected String getBusinessScene() {
        return GET_WORKER;
    }

    // ======================== 个性化业务 ========================

    /**
     * 根据工号查询工人
     */
    @Override
    @BusinessLog(value = GET_WORKER_BY_WORKCODE, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public WorkerVO getWorkerByCode(String workerCode) {
        generalValidator.validateStringNotBlank(workerCode, BusinessConstants.WORKER_CODE, GET_WORKER_BY_WORKCODE);

        Worker worker = getWorkerByCodeWithOutVerify(workerCode);
        if (ObjectUtil.isNull(worker)) {
            throw new BizException(HttpStatus.NOT_FOUND, WORKER_NOT_EXIST_BY_CODE);
        }

        return converter.poToVo(worker);
    }

    /**
     * 按状态统计工人数量
     */
    @Override
    @BusinessLog(value = GET_WORKER_DISTRIBUTION_BY_STATUS, recordParams = false, logLevel = BusinessLog.LogLevel.INFO)
    public WorkerStatusCountVO countWorkerByStatus() {
        List<Map<String, Object>> statusCountList = baseMapper.countWorkerByStatus();
        WorkerStatusCountVO vo = new WorkerStatusCountVO();
        vo.setNormalCount(StatisticalUtil.getCountFromList(statusCountList, WORKER_STATUS, Status.NORMAL.getValue()));
        vo.setAbnormalCount(StatisticalUtil.getCountFromList(statusCountList, WORKER_STATUS, Status.ABNORMAL.getValue()));
        vo.setOfflineCount(StatisticalUtil.getCountFromList(statusCountList, WORKER_STATUS, Status.OFFLINE.getValue()));
        vo.setTotalCount(vo.getNormalCount() + vo.getAbnormalCount() + vo.getOfflineCount());
        return vo;
    }

    /**
     * 按工种统计工人数量
     */
    @Override
    @BusinessLog(value = GET_WORKER_DISTRIBUTION_BY_WORKTYPE, recordParams = false, logLevel = BusinessLog.LogLevel.INFO)
    public WorkerTypeCountVO countWorkerByWorkType() {
        List<Map<String, Object>> workTypeCountList = baseMapper.countWorkerByWorkType();
        WorkerTypeCountVO vo = new WorkerTypeCountVO();
        vo.setHighAltitudeCount(StatisticalUtil.getCountFromList(workTypeCountList, WORKER_TYPE, WorkType.HIGH_ALTITUDE.getValue()));
        vo.setConfinedSpaceCount(StatisticalUtil.getCountFromList(workTypeCountList, WORKER_TYPE, WorkType.CONFINED_SPACE.getValue()));
        vo.setEquipmentOperationCount(StatisticalUtil.getCountFromList(workTypeCountList, WORKER_TYPE, WorkType.EQUIPMENT_OPERATION.getValue()));
        vo.setNormalWorkCount(StatisticalUtil.getCountFromList(workTypeCountList, WORKER_TYPE, WorkType.NORMAL_WORK.getValue()));
        vo.setTotalCount(vo.getHighAltitudeCount() + vo.getConfinedSpaceCount()
                + vo.getEquipmentOperationCount() + vo.getNormalWorkCount());
        return vo;
    }

    // ======================== 内部方法 ========================

    /**
     * 内部复用：根据工号查询（无校验）
     */
    private Worker getWorkerByCodeWithOutVerify(String workerCode) {
        if (StrUtil.isBlank(workerCode)) {
            return null;
        }
        return lambdaQuery().eq(Worker::getWorkerCode, workerCode).one();
    }
}