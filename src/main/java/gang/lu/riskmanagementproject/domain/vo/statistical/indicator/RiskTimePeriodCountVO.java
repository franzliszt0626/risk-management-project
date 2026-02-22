package gang.lu.riskmanagementproject.domain.vo.statistical.indicator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 当日各 4 小时时间段高风险工人数统计结果视图对象。
 * <p>
 * 高风险口径：中风险 + 高风险 + 严重风险，按工人去重统计。
 *
 * @author Franz Liszt
 * @since 2026-02-07
 */
@Data
@ApiModel(description = "当日 4 小时时间段高风险工人数统计结果")
public class RiskTimePeriodCountVO {

    @ApiModelProperty(value = "统计日期（yyyy-MM-dd）", example = "2026-02-01")
    private String statDate;

    @ApiModelProperty(value = "各时间段统计列表（共 6 段，每段 4 小时）")
    private List<TimePeriodItem> periodItems;

    /**
     * 单个时间段统计项。
     */
    @Data
    @ApiModel(description = "单个时间段高风险工人数统计项")
    public static class TimePeriodItem {

        @ApiModelProperty(value = "时间段描述", example = "08:00-12:00")
        private String periodDesc;

        @ApiModelProperty(value = "该时间段高风险工人数（中 + 高 + 严重风险去重合计）", example = "15")
        private Integer highRiskCount;
    }
}