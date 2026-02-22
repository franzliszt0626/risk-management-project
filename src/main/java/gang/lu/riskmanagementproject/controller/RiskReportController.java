package gang.lu.riskmanagementproject.controller;


import gang.lu.riskmanagementproject.service.RiskReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 15:59
 */
@Api(tags = "风险报告导出")
@RestController
@RequestMapping("/risk-report")
@RequiredArgsConstructor
public class RiskReportController {

    private final RiskReportService riskReportService;


    @ApiOperation(
            value = "导出工人风险报告PDF",
            notes = "包含该工人的历史风险记录 + AI预测分析建议，直接下载PDF文件。" +
                    "includeAi=true时调用AI分析（需联网），false时仅导出历史数据。"
    )
    @GetMapping("/export/{workerId}")
    public void exportReport(
            @ApiParam(value = "工人ID", required = true, example = "2")
            @PathVariable Long workerId,
            @ApiParam(value = "最多取最近N条历史记录，默认50", example = "50")
            @RequestParam(defaultValue = "50") Integer limit,
            @ApiParam(value = "是否包含AI预测（默认false）", example = "false")
            @RequestParam(defaultValue = "false") Boolean includeAi,
            HttpServletResponse response) {
        riskReportService.exportPdf(workerId, limit, includeAi, response);
    }
}
