package gang.lu.riskmanagementproject.domain.vo.normal;

import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:08
 * @description 工作地点前端展示实体
 */


@Data
@ApiModel(description = "工作区域 - 视图对象")
public class WorkAreaVO {

    @ApiModelProperty(value = "区域编号", example = "AREA_001")
    private String areaCode;

    @ApiModelProperty(value = "区域名称", example = "合肥工业大学新管理大楼A")
    private String areaName;

    @ApiModelProperty(value = "区域风险等级", allowableValues = "低风险、中风险、高风险")
    private AreaRiskLevel areaRiskLevel;

    @ApiModelProperty(value = "描述")
    private String description;
}
