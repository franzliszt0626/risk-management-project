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
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

import static gang.lu.riskmanagementproject.message.FailedMessages.RISK_INDICATOR_ID_LIST_EMPTY;
import static gang.lu.riskmanagementproject.message.SuccessMessages.*;

/**
 * <p>
 * 风险指标表 前端控制器
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-02-01
 */
@RestController
@RequestMapping("/api/risk-indicator")
@Api(tags = "风险指标管理 API")
@RequiredArgsConstructor
@Validated
public class RiskIndicatorController {

    private final RiskIndicatorService riskIndicatorService;

    // ======================== 通用CRUD接口 ========================

    @ApiOperation("新增风险指标")
    @PostMapping
    public Result<RiskIndicatorVO> addRiskIndicator(
            @ApiParam("风险指标新增数据")
            @Valid @RequestBody RiskIndicatorDTO dto) {
        RiskIndicatorVO vo = riskIndicatorService.add(dto);
        return Result.ok(RISK_INDICATOR_ADD_SUCCESS, vo);
    }

    @ApiOperation("删除风险指标")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRiskIndicator(
            @ApiParam("风险指标ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.RISK_INDICATOR_ID) Long id) {
        riskIndicatorService.delete(id);
        return Result.ok(RISK_INDICATOR_DELETE_SUCCESS);
    }

    @ApiOperation("批量删除风险指标")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteRiskIndicator(
            @ApiParam("风险指标ID列表")
            @RequestBody
            @NotEmpty(message = RISK_INDICATOR_ID_LIST_EMPTY) List<Long> ids) {
        riskIndicatorService.batchDelete(ids);
        return Result.ok(RISK_INDICATOR_BATCH_DELETE_SUCCESS);
    }

    @ApiOperation("修改风险指标")
    @PutMapping("/{id}")
    public Result<RiskIndicatorVO> updateRiskIndicator(
            @ApiParam("风险指标ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.RISK_INDICATOR_ID) Long id,
            @ApiParam("风险指标修改数据")
            @Valid @RequestBody RiskIndicatorDTO dto) {
        RiskIndicatorVO vo = riskIndicatorService.update(id, dto);
        return Result.ok(RISK_INDICATOR_UPDATE_SUCCESS, vo);
    }

    @ApiOperation("根据ID查询风险指标")
    @GetMapping("/{id}")
    public Result<RiskIndicatorVO> getRiskIndicatorById(
            @ApiParam("风险指标ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.RISK_INDICATOR_ID) Long id) {
        RiskIndicatorVO vo = riskIndicatorService.getOneById(id);
        return Result.ok(RISK_INDICATOR_GET_SUCCESS, vo);
    }

    @ApiOperation("多条件组合分页查询风险指标")
    @ApiImplicitParam(name = "queryDTO", value = "风险指标查询条件（含分页）", required = true, dataType = "RiskIndicatorQueryDTO", paramType = "body")
    @PostMapping("/search")
    public Result<PageVO<RiskIndicatorVO>> searchRiskIndicators(
            @Valid @RequestBody RiskIndicatorQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<RiskIndicatorVO> pageVO = riskIndicatorService.search(queryDTO);
        return Result.ok(String.format(RISK_INDICATOR_GET_COUNT_SUCCESS, pageVO.getTotal()), pageVO);
    }

    // ======================== 个性化业务接口 ========================

    @ApiOperation("根据工人ID查询最新风险指标")
    @ApiImplicitParam(name = "workerId", value = "工人ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/latest/{workerId}")
    public Result<RiskIndicatorVO> getLatestRiskIndicatorByWorkerId(
            @ApiParam("工人ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORKER_ID) Long workerId) {
        RiskIndicatorVO vo = riskIndicatorService.getLatestRiskIndicatorByWorkerId(workerId);
        return Result.ok(RISK_INDICATOR_GET_LATEST_SUCCESS, vo);
    }

    @ApiOperation("统计不重复工人的风险等级人数分布")
    @GetMapping("/count/risk-level")
    public Result<RiskLevelCountVO> countDistinctWorkerByRiskLevel() {
        RiskLevelCountVO countVO = riskIndicatorService.countDistinctWorkerByRiskLevel();
        return Result.ok(RISK_INDICATOR_STATISTIC_RISK_LEVEL_COUNT_SUCCESS, countVO);
    }

    @ApiOperation("统计当日4小时时间段高风险工人数")
    @GetMapping("/count/time-period")
    public Result<RiskTimePeriodCountVO> countHighRiskWorkerByTimePeriod(
            @ApiParam(value = "统计日期（yyyy-MM-dd），默认当天", example = "2026-02-01")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate statDate) {
        RiskTimePeriodCountVO countVO = riskIndicatorService.countHighRiskWorkerByTimePeriod(statDate);
        return Result.ok(RISK_INDICATOR_STATISTIC_HIGH_RISK_COUNT_SUCCESS, countVO);
    }
}