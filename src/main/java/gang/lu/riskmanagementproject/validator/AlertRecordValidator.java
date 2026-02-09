package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.annotation.ValidateLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.AlertRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static gang.lu.riskmanagementproject.common.EnumName.ALERT_LEVEL;
import static gang.lu.riskmanagementproject.common.IdName.ALERT_RECORD_ID;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 11:11
 * @description 预警记录相关校验
 */
@Component
@RequiredArgsConstructor
public class AlertRecordValidator {

    private final AlertRecordMapper alertRecordMapper;

    private final GeneralValidator generalValidator;

    /**
     * 预警等级非空校验
     */
    @ValidateLog("预警等级枚举非空校验")
    public void validateAlertLevel(Object alertLevel, String businessScene) {
        generalValidator.validateEnumNotNull(alertLevel, ALERT_LEVEL, businessScene);
    }

    /**
     * 校验预警记录ID合法性+记录存在性
     */
    @ValidateLog("预警记录ID存在性校验")
    public AlertRecord validateAlertRecordExist(Long id) {
        generalValidator.validateId(id, ALERT_RECORD_ID);
        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        if (ObjectUtil.isNull(alertRecord)) {
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_DATA_NOT_FOUND);
        }
        return alertRecord;
    }

}
