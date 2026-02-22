package gang.lu.riskmanagementproject.domain.dto.query;


import gang.lu.riskmanagementproject.annotation.ValidEnum;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.domain.enums.field.AreaRiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 14:47
 * @description 工作区域组合查询DTO（含分页参数）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "工作区域组合查询DTO（含分页参数）")
public class WorkAreaQueryDTO extends PageQueryDTO {

    @ApiModelProperty(value = "区域编码（模糊查询）", example = "AREA_001")
    private String areaCode;

    @ApiModelProperty(value = "区域名称（模糊查询）", example = "高空作业")
    private String areaName;

    @ApiModelProperty(value = "区域风险等级（低风险/中风险/高风险）", example = "低风险")
    @ValidEnum(enumClass = AreaRiskLevel.class, bizName = BusinessConstants.AREA_RISK_LEVEL)
    private String areaRiskLevelValue;

    @ApiModelProperty(value = "描述（模糊查询）", example = "防坠落")
    private String description;
}