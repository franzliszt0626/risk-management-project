package gang.lu.riskmanagementproject.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.converter.AlertRecordConverter;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.dto.query.AlertRecordQueryDTO;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.helper.QueryWrapperHelper;
import gang.lu.riskmanagementproject.mapper.AlertRecordMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.AlertRecordService;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static gang.lu.riskmanagementproject.common.BusinessConstants.*;
import static gang.lu.riskmanagementproject.common.FailedMessages.*;

/**
 * <p>
 * 预警记录表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Service
public class AlertRecordServiceImpl
        extends BaseCrudServiceImpl<AlertRecord, AlertRecordDTO, AlertRecordVO, AlertRecordQueryDTO, AlertRecordMapper, AlertRecordConverter>
        implements AlertRecordService {

    private final WorkerMapper workerMapper;

    public AlertRecordServiceImpl(AlertRecordMapper alertRecordMapper,
                                  AlertRecordConverter alertRecordConverter,
                                  GeneralValidator generalValidator,
                                  WorkerMapper workerMapper) {
        super(alertRecordMapper, alertRecordConverter, generalValidator);
        this.workerMapper = workerMapper;
    }

    // ======================== 通用CRUD ========================

    /**
     * 新增预警记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = ADD_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public AlertRecordVO add(AlertRecordDTO dto) {
        return super.add(dto);
    }

    /**
     * 删除预警记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = DELETE_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void delete(Long id) {
        super.delete(id);
    }

    /**
     * 批量删除预警记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = BATCH_DELETE_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void batchDelete(Iterable<Long> ids) {
        super.batchDelete(ids);
    }

    /**
     * 修改预警记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = UPDATE_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public AlertRecordVO update(Long id, AlertRecordDTO dto) {
        return super.update(id, dto);
    }

    /**
     * 根据ID查询预警记录
     */
    @Override
    @BusinessLog(value = GET_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public AlertRecordVO getOneById(Long id) {
        return super.getOneById(id);
    }

    /**
     * 多条件分页查询预警记录
     */
    @Override
    @BusinessLog(value = GET_ALERT_RECORD_BY_MULTIPLY_CONDITION, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public PageVO<AlertRecordVO> search(AlertRecordQueryDTO queryDTO) {
        return super.search(queryDTO);
    }

    // ======================== 校验方法 ========================

    /**
     * 新增校验
     */
    @Override
    public void validateAdd(AlertRecordDTO dto) {
        generalValidator.validateIdExist(dto.getWorkerId(), workerMapper, WORKER_NOT_EXIST);
    }

    /**
     * 修改校验
     */
    @Override
    public void validateUpdate(Long id, AlertRecordDTO dto) {
        Long newWorkerId = dto.getWorkerId();
        if (ObjectUtil.isNotNull(newWorkerId)) {
            generalValidator.validateIdExist(newWorkerId, workerMapper, WORKER_NOT_EXIST);
        }
    }

    /**
     * 查询校验
     */
    @Override
    public void validateSearch(AlertRecordQueryDTO queryDTO) {
        generalValidator.validateTimeRange(queryDTO.getCreatedStartTime(), queryDTO.getCreatedEndTime());
        generalValidator.validateTimeRange(queryDTO.getHandleStartTime(), queryDTO.getHandleEndTime());
    }

    /**
     * 构建查询条件
     */
    @Override
    public LambdaQueryWrapper<AlertRecord> buildQueryWrapper(AlertRecordQueryDTO queryDTO) {
        return QueryWrapperHelper.<AlertRecord>create()
                .eqEnumIfPresent(AlertRecord::getAlertLevel, queryDTO.getAlertLevelValue(), AlertLevel.class)
                .eqIfPresent(AlertRecord::getWorkerId, queryDTO.getWorkerId())
                .likeIfPresent(AlertRecord::getAlertType, queryDTO.getAlertType())
                .eqIfPresent(AlertRecord::getIsHandled, queryDTO.getIsHandled())
                .likeIfPresent(AlertRecord::getHandledBy, queryDTO.getHandledBy())
                .geIfPresent(AlertRecord::getHandleTime, queryDTO.getHandleStartTime())
                .leIfPresent(AlertRecord::getHandleTime, queryDTO.getHandleEndTime())
                .geIfPresent(AlertRecord::getCreateTime, queryDTO.getCreatedStartTime())
                .leIfPresent(AlertRecord::getCreateTime, queryDTO.getCreatedEndTime())
                .orderByDesc(AlertRecord::getCreateTime);
    }

    // ======================== 模板方法 ========================

    @Override
    protected String getNotFoundMsg() {
        return ALERT_RECORD_NOT_EXIST;
    }

    @Override
    protected String getBatchIdEmptyMsg() {
        return ALERT_RECORD_DELETE_BATCH_ID_EMPTY;
    }

    @Override
    protected String getBatchNotFoundMsg() {
        return ALERT_RECORD_DELETE_BATCH_ID_INVALID;
    }

    @Override
    protected String getBusinessScene() {
        return GET_ALERT_RECORD;
    }

    // ======================== 个性化业务 ========================

    /**
     * 标记预警记录为已处理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = HANDLE_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void markAlertRecordAsHandled(Long id, String handledBy) {
        generalValidator.validateIdExist(id, baseMapper, ALERT_RECORD_NOT_EXIST);
        generalValidator.validateStringNotBlank(handledBy, BusinessConstants.HANDLED_BY, HANDLE_ALERT_RECORD);
        // 拿到对应的记录
        AlertRecord alertRecord = baseMapper.selectById(id);
        // 如果已经标注为已处理
        if (alertRecord.getIsHandled()) {
            throw new BizException(HttpStatus.BAD_REQUEST, ALERT_RECORD_ALREADY_HANDLED_ERROR);
        }
        int rows = baseMapper.markAsHandled(id, handledBy, LocalDateTime.now());
        if (rows == 0) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, ALERT_RECORD_HANDLE_ERROR);
        }
    }
}