package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import gang.lu.riskmanagementproject.annotation.ValidateLog;
import gang.lu.riskmanagementproject.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static gang.lu.riskmanagementproject.common.FailureMessages.COMMON_INVALID_ID_ERROR;
import static gang.lu.riskmanagementproject.common.FailureMessages.COMMON_PARAM_EMPTY_ERROR;

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
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(COMMON_INVALID_ID_ERROR, idName)
            );
        }
    }

    /**
     * 字符串非空校验
     */
    @ValidateLog("字符串非空校验")
    public void validateStringNotBlank(String value, String fieldName, String businessScene) {
        if (StrUtil.isBlank(value)) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(COMMON_PARAM_EMPTY_ERROR, businessScene, fieldName));
        }
    }

    /**
     * 枚举非空校验
     */
    @ValidateLog("枚举非空校验")
    public <E> void validateEnumNotNull(E enumValue, String enumName, String businessScene) {
        if (ObjectUtil.isNull(enumValue)) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(COMMON_PARAM_EMPTY_ERROR, businessScene, enumName));
        }
    }

}