package gang.lu.riskmanagementproject.domain.vo;

import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:04
 * @description 风险数据前端展示类
 */


@Data
@ApiModel(description = "实时风险指标 - 视图对象")
public class RiskIndicatorVO {
    private Long workerId;
    private Integer heartRate;
    private Double sdnnMs;
    private Double fatiguePercent;
    private RiskLevel riskLevel;
    private Boolean alertFlag;
    private LocalDateTime recordTime;
}
