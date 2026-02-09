package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.annotation.ValidateLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.po.WorkArea;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkAreaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static gang.lu.riskmanagementproject.common.IdName.WORK_AREA_ID;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 11:06
 * @description 工作区域相关校验
 */
@Component
@RequiredArgsConstructor
public class WorkAreaValidator {

    private final WorkAreaMapper workAreaMapper;

    private final GeneralValidator generalValidator;


    /**
     * 工作区域编码/名称非空校验
     */
    @ValidateLog("工作区域字段非空校验")
    public void validateWorkAreaField(String value, String fieldName, String businessScene) {
        generalValidator.validateStringNotBlank(value, fieldName, businessScene);
    }


    /**
     * 校验工作区域ID合法性+记录存在性
     */
    @ValidateLog("工作区域ID存在性校验")
    public WorkArea validateWorkAreaExist(Long id) {
        generalValidator.validateId(id, WORK_AREA_ID);
        WorkArea workArea = workAreaMapper.selectById(id);
        if (ObjectUtil.isNull(workArea)) {
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORK_AREA_NOT_EXIST);
        }
        return workArea;
    }
}
