package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskPredictionVO;
import gang.lu.riskmanagementproject.service.RiskAiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.WORKER_ID;
import static gang.lu.riskmanagementproject.message.SuccessMessages.AI_ANALYZE_SUCCESS;

/**
 * AI 风险预测接口
 *
 * @author Franz Liszt
 * @since 2026-02-22
 */
@Api(tags = "AI 风险预测")
@Validated
@RestController
@RequestMapping("/api/risk-ai")
@RequiredArgsConstructor
public class RiskAiController {

    private final RiskAiService riskAiService;

    // ======================== 个性化业务接口 ========================

    @ApiOperation(
            value = "预测工人未来风险",
            notes = "读取工人历史风险记录，调用 Qwen 大模型分析，返回风险等级预测、趋势描述与健康建议"
    )
    @GetMapping("/predict/{workerId}")
    public Result<RiskPredictionVO> predictRisk(
            @ApiParam(value = WORKER_ID, required = true, example = "2")
            @PathVariable
            @ValidId(bizName = WORKER_ID) Long workerId,
            @ApiParam(value = "参与分析的最近记录条数（1-100），默认 20", example = "20")
            @RequestParam(defaultValue = "20") Integer limit) {
        RiskPredictionVO vo = riskAiService.predictRisk(workerId, limit);
        return Result.ok(AI_ANALYZE_SUCCESS, vo);
    }
}