package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.service.RiskReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static gang.lu.riskmanagementproject.common.BusinessConstants.WORKER_ID;

/**
 * 风险报告导出接口
 *
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 15:59
 */
@Api(tags = "风险报告导出")
@Validated
@RestController
@RequestMapping("/api/risk-report")
@RequiredArgsConstructor
public class RiskReportController {

    private final RiskReportService riskReportService;

    @ApiOperation(
            value = "导出工人风险报告 PDF",
            notes = "包含该工人的历史风险记录，includeAi=true 时附带 AI 预测分析建议（需联网）。"
    )
    @GetMapping("/export/{workerId}")
    public void exportReport(
            @ApiParam(value = WORKER_ID, required = true, example = "2")
            @PathVariable
            @ValidId(bizName = WORKER_ID) Long workerId,
            @ApiParam(value = "最多取最近 N 条历史记录，默认 50", example = "50")
            @RequestParam(defaultValue = "50") Integer limit,
            @ApiParam(value = "是否包含 AI 预测，默认 false", example = "false")
            @RequestParam(defaultValue = "false") Boolean includeAi,
            HttpServletResponse response) {
        riskReportService.exportPdf(workerId, limit, includeAi, response);
    }
}