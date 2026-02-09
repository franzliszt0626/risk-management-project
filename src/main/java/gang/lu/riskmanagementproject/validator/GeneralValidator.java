package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import gang.lu.riskmanagementproject.annotation.ValidateLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 统一业务校验工具类
 * 职责：所有业务层的参数校验、存在性校验、分页参数处理
 *
 * @author FranzLiszt
 */
@Component
public class GeneralValidator {


    /**
     * ID校验（非空、非负）
     */
    @ValidateLog("ID合法性校验")
    public void validateId(Long id, String idName) {
        if (ObjectUtil.isNull(id) || id <= 0) {
            throw new BizException(HttpStatus.BAD_REQUEST, FailureMessages.COMMON_INVALID_ID_ERROR);
        }
    }

    /**
     * 字符串非空校验
     */
    @ValidateLog("字符串非空校验")
    public void validateStringNotBlank(String value, String fieldName, String businessScene) {
        if (StrUtil.isBlank(value)) {
            String errorMsg = String.format("【%s失败】%s不能为空", businessScene, fieldName);
            throw new BizException(HttpStatus.BAD_REQUEST, errorMsg);
        }
    }

    /**
     * 枚举非空校验
     */
    @ValidateLog("枚举非空校验")
    public <E> void validateEnumNotNull(E enumValue, String enumName, String businessScene) {
        if (ObjectUtil.isNull(enumValue)) {
            String errorMsg = String.format("【%s失败】%s不能为空", businessScene, enumName);
            throw new BizException(HttpStatus.BAD_REQUEST, errorMsg);
        }
    }

}