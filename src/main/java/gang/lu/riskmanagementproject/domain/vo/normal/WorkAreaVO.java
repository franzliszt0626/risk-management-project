package gang.lu.riskmanagementproject.domain.vo.normal;

import gang.lu.riskmanagementproject.domain.enums.field.AreaRiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作区域视图对象，用于前端展示。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@ApiModel(description = "工作区域 - 视图对象")
public class WorkAreaVO {

    @ApiModelProperty(value = "区域编码", example = "AREA_001")
    private String areaCode;

    @ApiModelProperty(value = "区域名称", example = "高空作业平台 A")
    private String areaName;

    @ApiModelProperty(value = "区域风险等级（低风险 / 中风险 / 高风险）",
            allowableValues = "低风险,中风险,高风险", example = "低风险")
    private AreaRiskLevel areaRiskLevel;

    @ApiModelProperty(value = "区域描述", example = "高空作业平台 A 区，注意防坠落")
    private String description;
}