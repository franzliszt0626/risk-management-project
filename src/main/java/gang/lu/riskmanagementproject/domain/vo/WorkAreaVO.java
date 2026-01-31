package gang.lu.riskmanagementproject.domain.vo;

import io.swagger.annotations.ApiModel;
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
    private String areaCode;
    private String areaName;
    private String riskLevel;
    private String description;
}
