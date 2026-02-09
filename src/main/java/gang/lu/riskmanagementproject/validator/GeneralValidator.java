package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gang.lu.riskmanagementproject.annotation.ValidateLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.FailureMessages.COMMON_INVALID_ID_ERROR;
import static gang.lu.riskmanagementproject.common.FailureMessages.COMMON_PARAM_EMPTY_ERROR;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026-02-09 19:47:23
 * @description 统一校验器
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

    /**
     * 通用数据库校验
     */
    public void validateDbOperateResult(int rows) {
        if (rows != 1) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }
    }

    /**
     * 通用批量ID存在性校验（抽取共性，所有业务复用）
     */
    @ValidateLog("批量ID存在性校验")
    public <T> void validateBatchIdsExist(List<Long> ids, BaseMapper<T> mapper, String notExistMsg) {
        if (CollUtil.isEmpty(ids)) {
            throw new BizException(HttpStatus.BAD_REQUEST, FailureMessages.COMMON_PARAM_EMPTY_ERROR);
        }
        List<T> existList = mapper.selectBatchIds(ids);
        if (existList.size() != ids.size()) {
            Set<Long> existIds = existList.stream()
                    .map(item -> (Long) ReflectUtil.getFieldValue(item, "id"))
                    .collect(Collectors.toSet());
            List<Long> notExistIds = ids.stream().filter(id -> !existIds.contains(id)).toList();
            throw new BizException(HttpStatus.NOT_FOUND, String.format(notExistMsg, notExistIds));
        }
    }

}