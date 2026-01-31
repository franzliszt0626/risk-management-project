package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:07
 * @description 工作地点数据传输实体
 */
@Data
@ApiModel(description = "工作区域 - 传输对象")
public class WorkAreaDTO {
    @ApiModelProperty(value = "区域编码", example = "AREA_001")
    private String areaCode;

    @ApiModelProperty(value = "区域名称", example = "高空作业平台A")
    private String areaName;

    @ApiModelProperty(value = "区域风险等级")
    private RiskLevel riskLevel;

    @ApiModelProperty(value = "描述")
    private String description;
}