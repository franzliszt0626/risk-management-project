package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.field.AreaRiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import static gang.lu.riskmanagementproject.message.FailedMessages.*;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026-02-09 20:55:18
 * @description 工作区域数据传输实体
 */
@Data
@ApiModel(description = "工作区域 - 传输对象")
public class WorkAreaDTO {
    @ApiModelProperty(value = "区域编码", required = true, example = "AREA_001")
    @Length(max = 20, message = WORK_AREA_CODE_INVALID)
    private String areaCode;

    @ApiModelProperty(value = "区域名称", required = true, example = "高空作业平台A")
    @Length(max = 100, message = WORK_AREA_NAME_INVALID)
    private String areaName;

    @ApiModelProperty(value = "区域风险等级", example = "低风险")
    @ValidEnum(enumClass = AreaRiskLevel.class, bizName = BusinessConstants.AREA_RISK_LEVEL)
    private String areaRiskLevelValue;

    @ApiModelProperty(value = "描述", example = "高空作业平台A区，注意防坠落")
    @Length(max = 200, message = WORK_AREA_DESCRIPTION_INVALID)
    private String description;
}