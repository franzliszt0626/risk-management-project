package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskPredictionVO;
import gang.lu.riskmanagementproject.service.RiskAiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static gang.lu.riskmanagementproject.common.BusinessConstants.WORKER_ID;
import static gang.lu.riskmanagementproject.message.SuccessMessages.AI_ANALYZE_SUCCESS;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 15:45
 * @description
 */
@Api(tags = "AI风险预测")
@RestController
@RequestMapping("/risk-ai")
@RequiredArgsConstructor
public class RiskAiController {

    private final RiskAiService riskAiService;


    @ApiOperation(
            value = "预测工人未来风险",
            notes = "传入工人ID，读取该工人所有历史风险记录，调用AI分析并返回预测结果与建议"
    )
    @GetMapping("/predict/{workerId}")
    public Result<RiskPredictionVO> predict(
            @ApiParam(value = WORKER_ID, required = true, example = "2")
            @PathVariable Long workerId,
            @ApiParam(value = "最多取最近N条记录参与分析，默认20", example = "20")
            @RequestParam(defaultValue = "20") Integer limit) {
        RiskPredictionVO result = riskAiService.predictRisk(workerId, limit);
        return Result.ok(AI_ANALYZE_SUCCESS, result);
    }
}
