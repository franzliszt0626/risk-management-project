package gang.lu.riskmanagementproject.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:06
 * @description 风险历史前端展示实体
 */


@Data
@ApiModel(description = "历史风险数据 - 视图对象")
public class RiskHistoryVO {
    private Long workerId;
    private Integer heartRate;
    private Double sdnnMs;
    private Double fatiguePercent;
    private Integer breathRate;
    private Integer auScore;
    private Integer stressLevel;
    private LocalDateTime recordTime;
}
