package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import gang.lu.riskmanagementproject.mapper.AlertRecordMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.AlertRecordService;
import gang.lu.riskmanagementproject.helper.PageHelper;
import gang.lu.riskmanagementproject.util.EnumConvertUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AlertRecordServiceImpl extends ServiceImpl<AlertRecordMapper, AlertRecord> implements AlertRecordService {

    private final AlertRecordMapper alertRecordMapper;
    private final WorkerMapper workerMapper;
    private final GeneralValidator generalValidator;
    private final AlertRecordConverter alertRecordConverter;


    @Override
    @BusinessLog(value = GET_ALERT_RECORD_BY_MULTIPLY_CONDITION, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public PageVO<AlertRecordVO> searchAlertRecords(AlertRecordQueryDTO queryDTO) {
        // 1. 构建分页对象
        Page<AlertRecord> poPage = PageHelper.buildPage(queryDTO, GET_ALERT_RECORD);
        // 校验时间
        generalValidator.validateTimeRange(queryDTO.getCreatedStartTime(), queryDTO.getCreatedEndTime());
        generalValidator.validateTimeRange(queryDTO.getHandleStartTime(), queryDTO.getHandleEndTime());
        // 2. 构建多条件查询Wrapper
        LambdaQueryWrapper<AlertRecord> wrapper = buildAlertRecordQueryWrapper(queryDTO);
        // 3. 分页查询
        poPage = page(poPage, wrapper);
        // 4. PO分页转VO分页
        return alertRecordConverter.pagePoToPageVO(poPage);
    }


    /**
     * 新增一条预警记录
     *
     * @param dto dto实体
     * @return VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = ADD_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public AlertRecordVO addAlertRecord(AlertRecordDTO dto) {
        // 1. 校验工人存在性（id由controller校验）
        generalValidator.validateIdExist(dto.getWorkerId(), workerMapper, WORKER_NOT_EXIST);
        // 2. DTO转PO + 插入
        AlertRecord alertRecord = alertRecordConverter.dtoToPo(dto);
        generalValidator.validateDbOperateResult(alertRecordMapper.insert(alertRecord));
        return alertRecordConverter.poToVo(alertRecord);
    }

    /**
     * 删除预警记录（根据ID）
     *
     * @param id id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = DELETE_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void deleteAlertRecord(Long id) {
        generalValidator.validateIdExist(id, alertRecordMapper, ALERT_RECORD_NOT_EXIST);
        generalValidator.validateDbOperateResult(alertRecordMapper.deleteById(id));
    }

    /**
     * 修改预警记录
     *
     * @param id  id
     * @param dto DTO
     * @return VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = UPDATE_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public AlertRecordVO updateAlertRecord(Long id, AlertRecordDTO dto) {
        // 1. 校验预警记录存在性
        AlertRecord alertRecord = generalValidator.validateIdExist(id, alertRecordMapper, ALERT_RECORD_NOT_EXIST);
        // 2、校验工人ID
        Long newWorkerId = dto.getWorkerId();
        if (ObjectUtil.isNotNull(newWorkerId)) {
            generalValidator.validateIdExist(newWorkerId, workerMapper, WORKER_NOT_EXIST);
        }
        // 3. 通用转换器：用DTO更新PO（空值不覆盖）
        alertRecordConverter.updatePoFromDto(dto, alertRecord);
        // 4. 数据库操作
        generalValidator.validateDbOperateResult(alertRecordMapper.updateById(alertRecord));
        // 5. 返回VO
        return alertRecordConverter.poToVo(alertRecord);
    }


    /**
     * 根据ID查
     *
     * @param id 预警记录id
     * @return VO
     */
    @Override
    @BusinessLog(value = GET_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public AlertRecordVO getAlertRecordById(Long id) {
        AlertRecord alertRecord = generalValidator.validateIdExist(id, alertRecordMapper, ALERT_RECORD_NOT_EXIST);
        return alertRecordConverter.poToVo(alertRecord);
    }

    /**
     * 标记预警记录为已处理
     *
     * @param id        id
     * @param handledBy 由谁处理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = HANDLE_ALERT_RECORD, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void markAlertRecordAsHandled(Long id, String handledBy) {
        // 1. 校验预警记录存在性
        generalValidator.validateIdExist(id, alertRecordMapper, ALERT_RECORD_NOT_EXIST);
        // 2. 校验处理人非空
        generalValidator.validateStringNotBlank(handledBy, BusinessConstants.HANDLED_BY, BusinessConstants.HANDLE_ALERT_RECORD);
        // 3. 标记已处理
        int rows = alertRecordMapper.markAsHandled(id, handledBy, LocalDateTime.now());
        if (rows == 0) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, ALERT_RECORD_HANDLE_ERROR);
        }
    }

    private LambdaQueryWrapper<AlertRecord> buildAlertRecordQueryWrapper(AlertRecordQueryDTO queryDTO) {
        LambdaQueryWrapper<AlertRecord> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(queryDTO.getAlertLevelValue())) {
            AlertLevel alertLevel = EnumConvertUtil.toEnum(queryDTO.getAlertLevelValue(), AlertLevel.class);
            wrapper.eq(AlertRecord::getAlertLevel, alertLevel);
        }
        if (queryDTO.getWorkerId() != null) {
            wrapper.eq(AlertRecord::getWorkerId, queryDTO.getWorkerId());
        }
        if (StrUtil.isNotBlank(queryDTO.getAlertType())) {
            wrapper.like(AlertRecord::getAlertType, queryDTO.getAlertType().trim());
        }
        if (queryDTO.getIsHandled() != null) {
            wrapper.eq(AlertRecord::getIsHandled, queryDTO.getIsHandled());
        }
        if (StrUtil.isNotBlank(queryDTO.getHandledBy())) {
            wrapper.like(AlertRecord::getHandledBy, queryDTO.getHandledBy().trim());
        }
        if (queryDTO.getCreatedStartTime() != null) {
            wrapper.ge(AlertRecord::getCreatedTime, queryDTO.getCreatedStartTime());
        }
        if (queryDTO.getCreatedEndTime() != null) {
            wrapper.le(AlertRecord::getCreatedTime, queryDTO.getCreatedEndTime());
        }
        if (queryDTO.getHandleStartTime() != null) {
            wrapper.ge(AlertRecord::getHandleTime, queryDTO.getHandleStartTime());
        }
        if (queryDTO.getHandleEndTime() != null) {
            wrapper.le(AlertRecord::getHandleTime, queryDTO.getHandleEndTime());
        }
        wrapper.orderByDesc(AlertRecord::getCreatedTime);
        return wrapper;
    }

}
