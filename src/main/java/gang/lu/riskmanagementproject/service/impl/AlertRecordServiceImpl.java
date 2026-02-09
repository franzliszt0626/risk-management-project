package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.AlertRecordMapper;
import gang.lu.riskmanagementproject.service.AlertRecordService;
import gang.lu.riskmanagementproject.util.ConvertUtil;
import gang.lu.riskmanagementproject.validator.AlertRecordValidator;
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
import static gang.lu.riskmanagementproject.common.FieldName.*;


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

    private final WorkerValidator workerValidator;

    private final AlertRecordValidator alertRecordValidator;

    private final GeneralValidator generalValidator;

    /**
     * 新增一条预警记录
     *
     * @param dto dto实体
     * @return VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "新增预警记录", recordParams = true)
    public AlertRecordVO addAlertRecord(AlertRecordDTO dto) {
        // 1. 校验工人存在性
        Long workerId = dto.getWorkerId();
        workerValidator.validateWorkerExist(workerId, ADD_ALERT_RECORD);
        // 2. 校验预警等级
        alertRecordValidator.validateAlertLevel(dto.getAlertLevel(), ADD_ALERT_RECORD);
        // 3. DTO转PO + 插入
        AlertRecord alertRecord = ConvertUtil.convert(dto, AlertRecord.class);
        generalValidator.validateDbOperateResult(alertRecordMapper.insert(alertRecord));
        return ConvertUtil.convert(alertRecord, AlertRecordVO.class);
    }

    /**
     * 删除预警记录（根据ID）
     *
     * @param id id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "删除预警记录", recordParams = true)
    public void deleteAlertRecord(Long id) {
        alertRecordValidator.validateAlertRecordExist(id);
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
    @BusinessLog(value = "修改预警记录", recordParams = true)
    public AlertRecordVO updateAlertRecord(Long id, AlertRecordDTO dto) {
        AlertRecord alertRecord = alertRecordValidator.validateAlertRecordExist(id);
        // 1、校验工人ID
        Long newWorkerId = dto.getWorkerId();
        // 如果前端没传id，则校验旧id，传了就校验新id
        workerValidator.validateWorkerExist(
                ObjectUtil.isNotNull(newWorkerId) ? newWorkerId : alertRecord.getWorkerId(),
                UPDATE_ALERT_RECORD
        );
        // 2、校验预警等级（若传入）
        if (ObjectUtil.isNotNull(dto.getAlertLevel())) {
            alertRecordValidator.validateAlertLevel(dto.getAlertLevel(), UPDATE_ALERT_RECORD);
        }

        // 3、DTO转PO（覆盖非空字段）
        BeanUtil.copyProperties(dto, alertRecord, ID, CREATE_TIME);
        generalValidator.validateDbOperateResult(alertRecordMapper.updateById(alertRecord));
        return ConvertUtil.convert(alertRecordMapper.selectById(id), AlertRecordVO.class);
    }


    /**
     * 根据ID查
     *
     * @param id 预警记录id
     * @return VO
     */
    @Override
    @BusinessLog(value = "查询预警记录（ID）", recordParams = true)
    public AlertRecordVO getAlertRecordById(Long id) {
        AlertRecord alertRecord = alertRecordValidator.validateAlertRecordExist(id);
        return ConvertUtil.convert(alertRecord, AlertRecordVO.class);
    }

    /**
     * 根据工人ID查询（查不到返回空）
     *
     * @param workerId 工人id
     * @return 集合
     */
    @Override
    @BusinessLog(value = "查询工人预警记录", recordParams = true)
    public List<AlertRecordVO> getAlertRecordsByWorkerId(Long workerId) {
        workerValidator.validateWorkerExist(workerId, GET_ALERT_RECORD);
        List<AlertRecord> alertRecords = alertRecordMapper.selectByWorkerId(workerId);
        if (ObjectUtil.isEmpty(alertRecords)) {
            return Collections.emptyList();
        }
        return ConvertUtil.convertList(alertRecords, AlertRecordVO.class);
    }

    /**
     * 根据预警级别查询
     *
     * @param alertLevel 级别
     * @return 结果集合
     */
    @Override
    @BusinessLog(value = "查询预警记录（级别）", recordParams = true)
    public List<AlertRecordVO> getAlertRecordsByAlertLevel(AlertLevel alertLevel) {
        alertRecordValidator.validateAlertLevel(alertLevel, GET_ALERT_RECORD);
        List<AlertRecord> alertRecords = alertRecordMapper.selectByAlertLevel(alertLevel.getValue());
        if (ObjectUtil.isEmpty(alertRecords)) {
            return Collections.emptyList();
        }
        return ConvertUtil.convertList(alertRecords, AlertRecordVO.class);
    }

    /**
     * 根据预警类型模糊查询
     *
     * @param alertType 类型
     * @return 结果集合
     */
    @Override
    @BusinessLog(value = "查询预警记录（类型模糊）", recordParams = true)
    public List<AlertRecordVO> getAlertRecordsByAlertTypeLike(String alertType) {
        generalValidator.validateStringNotBlank(alertType, ALERT_TYPE, GET_ALERT_RECORD);
        List<AlertRecord> alertRecords = alertRecordMapper.selectByAlertTypeLike(alertType);
        if (ObjectUtil.isEmpty(alertRecords)) {
            return Collections.emptyList();
        }
        return ConvertUtil.convertList(alertRecords, AlertRecordVO.class);
    }

    /**
     * 标记预警记录为已处理
     *
     * @param id        id
     * @param handledBy 由谁处理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "标记预警记录为已处理", recordParams = true)
    public void markAlertRecordAsHandled(Long id, String handledBy) {
        // 1、看记录是否存在
        alertRecordValidator.validateAlertRecordExist(id);
        // 2、校验处理人是否为空
        generalValidator.validateStringNotBlank(handledBy, HANDLED_BY, HANDLE_ALERT_RECORD);
        int rows = alertRecordMapper.markAsHandled(id, handledBy, LocalDateTime.now());
        if (rows == 0) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.ALERT_RECORD_HANDLE_ERROR);
        }
    }

}
