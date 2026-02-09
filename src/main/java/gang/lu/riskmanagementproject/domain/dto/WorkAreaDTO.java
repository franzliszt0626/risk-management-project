package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


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
    @NotBlank(message = "区域编码不能为空")
    @Length(max = 20, message = "区域编码长度不能超过20个字符")
    private String areaCode;

    @ApiModelProperty(value = "区域名称", required = true, example = "高空作业平台A")
    @NotBlank(message = "区域名称不能为空")
    @Length(max = 100, message = "区域名称长度不能超过100个字符")
    private String areaName;

    @ApiModelProperty(value = "区域风险等级", example = "低风险")
    private AreaRiskLevel areaRiskLevel;

    @ApiModelProperty(value = "描述", example = "高空作业平台A区，注意防坠落")
    @Length(max = 200, message = "描述长度不能超过200个字符")
    private String description;
}