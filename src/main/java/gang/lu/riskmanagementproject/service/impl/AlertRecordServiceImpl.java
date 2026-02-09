package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.AlertRecordVO;
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
import static gang.lu.riskmanagementproject.common.FieldName.ALERT_TYPE;
import static gang.lu.riskmanagementproject.common.FieldName.HANDLED_BY;


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
        int inserted = alertRecordMapper.insert(alertRecord);
        if (inserted != 1) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }
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
        int deleted = alertRecordMapper.deleteById(id);
        if (deleted != 1) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }
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

        // 校验工人ID
        Long newWorkerId = dto.getWorkerId();
        if (ObjectUtil.isNotNull(newWorkerId)) {
            workerValidator.validateWorkerExist(newWorkerId, UPDATE_ALERT_RECORD);
        } else {
            workerValidator.validateWorkerExist(alertRecord.getWorkerId(), UPDATE_ALERT_RECORD);
        }

        // 校验预警等级（若传入）
        if (ObjectUtil.isNotNull(dto.getAlertLevel())) {
            alertRecordValidator.validateAlertLevel(dto.getAlertLevel(), UPDATE_ALERT_RECORD);
        }

        // DTO转PO（覆盖非空字段）
        BeanUtil.copyProperties(dto, alertRecord, "id", "createdTime");
        int updated = alertRecordMapper.updateById(alertRecord);
        if (updated != 1) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }

        AlertRecord latestRecord = alertRecordMapper.selectById(id);
        return ConvertUtil.convert(latestRecord, AlertRecordVO.class);
    }


    /**
     * 根据ID查询（查不到抛BizException）
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

        alertRecordValidator.validateAlertRecordExist(id);
        generalValidator.validateStringNotBlank(handledBy, HANDLED_BY, HANDLE_ALERT_RECORD);
        int rows = alertRecordMapper.markAsHandled(id, handledBy, LocalDateTime.now());
        if (rows == 0) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.ALERT_OPERATE_HANDLE_ERROR);
        }
    }

}
