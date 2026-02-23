package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.domain.enums.field.AreaRiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.AREA_RISK_LEVEL;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 工作区域新增 / 修改传输对象。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Data
@ApiModel(description = "工作区域 - 传输对象")
public class WorkAreaDTO {

    @ApiModelProperty(value = "区域编码（全局唯一，最多 20 字符）", required = true, example = "AREA_001")
    @NotBlank(message = WORK_AREA_CODE_EMPTY)
    @Length(max = 20, message = WORK_AREA_CODE_INVALID)
    private String areaCode;

    @ApiModelProperty(value = "区域名称（最多 100 字符）", required = true, example = "高空作业平台 A")
    @NotBlank(message = WORK_AREA_NAME_EMPTY)
    @Length(max = 100, message = WORK_AREA_NAME_INVALID)
    private String areaName;

    @ApiModelProperty(value = "区域风险等级（低风险 / 中风险 / 高风险）", required = true, example = "低风险")
    @NotBlank(message = WORK_AREA_RISK_LEVEL_EMPTY)
    @ValidEnum(enumClass = AreaRiskLevel.class, bizName = AREA_RISK_LEVEL)
    private String areaRiskLevelValue;

    @ApiModelProperty(value = "区域描述（最多 200 字符）", example = "高空作业平台 A 区，注意防坠落")
    @Length(max = 200, message = WORK_AREA_DESCRIPTION_INVALID)
    private String description;
}