package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.vo.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.RiskLevelCountVO;
import gang.lu.riskmanagementproject.domain.vo.RiskTimePeriodCountVO;
import gang.lu.riskmanagementproject.service.RiskIndicatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026-02-01 11:08:10
 * @description 风险指标管理，存储用户实时风险指标
 */
@RestController
@RequestMapping("/api/risk-indicator")
@Api(tags = "指标管理 API")
@RequiredArgsConstructor
public class RiskIndicatorController {

    private final RiskIndicatorService riskIndicatorService;

    /**
     * 这个接口用于前端接受算法返回的参数后，调用本接口插入风险指标数据
     *
     * @param riskIndicatorDTO 前端传来的数据传输实体2
     * @return void
     */
    @PostMapping
    @ApiOperation("插入风险指标信息，接受前端调用。接受算法数据")
    public Result<Void> insertRiskIndicator(@RequestBody RiskIndicatorDTO riskIndicatorDTO) {
        riskIndicatorService.insertRiskIndicator(riskIndicatorDTO);
        return Result.ok();
    }

    @GetMapping("/latest/{workerId}")
    @ApiOperation("根据工人id查询他【最新一次】的风险指标")
    public Result<RiskIndicatorVO> getLatestRiskIndicatorByWorkerId(@PathVariable Long workerId) {
        return Result.ok(riskIndicatorService.getLatestRiskIndicatorByWorkerId(workerId));
    }

    @GetMapping("/{workerId}")
    @ApiOperation("根据工人id查询他的【历史所有的】风险指标")
    public Result<List<RiskIndicatorVO>> getRiskIndicatorsByWorkerId(@PathVariable Long workerId) {
        return Result.ok(riskIndicatorService.getRiskIndicatorsByWorkerId(workerId));
    }

    /**
     * 统计去重工人的风险等级人数分布
     */
    @GetMapping("/count/risk-level")
    @ApiOperation("统计不重复工人的风险等级人数分布")
    public Result<RiskLevelCountVO> countDistinctWorkerByRiskLevel() {
        RiskLevelCountVO countVO = riskIndicatorService.countDistinctWorkerByRiskLevel();
        return Result.ok(countVO);
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
        return Result.ok(countVO);
    }

}
