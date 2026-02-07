package gang.lu.riskmanagementproject.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/7 14:42
 * @description 当日的每四个小时段中高风险人数统计
 */
@Data
@ApiModel(description = "当日4小时时间段高风险工人数统计结果")
public class RiskTimePeriodCountVO {
    @ApiModelProperty(value = "统计日期（yyyy-MM-dd）", example = "2026-02-01")
    private String statDate;

    @ApiModelProperty(value = "时间段统计列表")
    private List<TimePeriodItem> periodItems;

    /**
     * 单个时间段统计项
     */
    @Data
    @ApiModel(description = "时间段统计项")
    public static class TimePeriodItem {
        @ApiModelProperty(value = "时间段描述", example = "00:00-04:00")
        private String periodDesc;

        @ApiModelProperty(value = "高风险人数（中+高+严重风险去重总数）", example = "15")
        private Integer highRiskCount;
    }
}
