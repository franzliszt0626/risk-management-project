package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.dto.query.RiskIndicatorQueryDTO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskLevelCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskTimePeriodCountVO;
import gang.lu.riskmanagementproject.service.RiskIndicatorService;
import gang.lu.riskmanagementproject.helper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

import static gang.lu.riskmanagementproject.common.SuccessMessages.*;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026-02-01 11:08:10
 * @description 风险指标管理，存储用户实时风险指标
 */
@RestController
@RequestMapping("/api/risk-indicator")
@Api(tags = "风险指标管理 API")
@RequiredArgsConstructor
@Validated
public class RiskIndicatorController {

    private final RiskIndicatorService riskIndicatorService;


    @PostMapping("/search")
    @ApiOperation("多条件组合分页查询风险指标（支持工人ID/风险等级/时间区间等筛选）")
    @ApiImplicitParam(name = "queryDTO", value = "风险指标查询条件（含分页）", required = true, dataType = "RiskIndicatorQueryDTO", paramType = "body")
    public Result<PageVO<RiskIndicatorVO>> searchRiskIndicators(@Valid @RequestBody RiskIndicatorQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<RiskIndicatorVO> pageVO = riskIndicatorService.searchRiskIndicators(queryDTO);
        return Result.ok(String.format(RISK_INDICATOR_GET_HISTORY_SUCCESS, pageVO.getTotal()), pageVO);
    }


    /**
     * 这个接口用于前端接受算法返回的参数后，调用本接口插入风险指标数据
     *
     * @param riskIndicatorDTO 前端传来的数据传输实体
     * @return void
     */
    @PostMapping
    @ApiOperation("插入风险指标信息，接受前端调用，接受算法数据")
    @ApiImplicitParam(name = "riskIndicatorDTO", value = "风险指标数据", required = true, dataType = "RiskIndicatorDTO", paramType = "body")
    public Result<RiskIndicatorVO> addRiskIndicator(@Valid @RequestBody RiskIndicatorDTO riskIndicatorDTO) {
        RiskIndicatorVO riskIndicatorVO = riskIndicatorService.addRiskIndicator(riskIndicatorDTO);
        return Result.ok(RISK_INDICATOR_ADD_SUCCESS, riskIndicatorVO);
    }

    @GetMapping("/latest/{workerId}")
    @ApiOperation("根据工人id查询他【最新一次】的风险指标")
    @ApiImplicitParam(name = "workerId", value = "工人ID", required = true, dataType = "Long", paramType = "path")
    public Result<RiskIndicatorVO> getLatestRiskIndicatorByWorkerId(
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORKER_ID) Long workerId) {
        RiskIndicatorVO vo = riskIndicatorService.getLatestRiskIndicatorByWorkerId(workerId);
        return Result.ok(RISK_INDICATOR_GET_LATEST_SUCCESS, vo);
    }

    /**
     * 统计去重工人的风险等级人数分布
     */
    @GetMapping("/count/risk-level")
    @ApiOperation("统计不重复工人的风险等级人数分布")
    public Result<RiskLevelCountVO> countDistinctWorkerByRiskLevel() {
        RiskLevelCountVO countVO = riskIndicatorService.countDistinctWorkerByRiskLevel();
        return Result.ok(RISK_INDICATOR_STATISTIC_RISK_LEVEL_COUNT_SUCCESS, countVO);
    }

    /**
     * 统计当日各4小时时间段高风险工人数
     *
     * @param statDate 统计日期（格式：yyyy-MM-dd，不传则默认当天）
     */
    @GetMapping("/count/time-period")
    @ApiOperation("统计当日4小时时间段高风险工人数（中+高+严重风险）")
    public Result<RiskTimePeriodCountVO> countHighRiskWorkerByTimePeriod(
            @ApiParam(value = "统计日期（yyyy-MM-dd），默认当天", example = "2026-02-01")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate statDate) {
        RiskTimePeriodCountVO countVO = riskIndicatorService.countHighRiskWorkerByTimePeriod(statDate);
        return Result.ok(RISK_INDICATOR_STATISTIC_HIGH_RISK_COUNT_SUCCESS, countVO);
    }

}
