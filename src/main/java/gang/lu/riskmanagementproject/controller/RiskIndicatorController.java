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
import gang.lu.riskmanagementproject.helper.PageHelper;
import gang.lu.riskmanagementproject.service.RiskIndicatorService;
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

import static gang.lu.riskmanagementproject.common.BusinessConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.RISK_INDICATOR_ID_LIST_EMPTY;
import static gang.lu.riskmanagementproject.message.SuccessMessages.*;

/**
 * 风险指标管理接口
 *
 * @author Franz Liszt
 * @since 2026-02-01
 */
@Api(tags = "风险指标管理")
@Validated
@RestController
@RequestMapping("/api/risk-indicator")
@RequiredArgsConstructor
public class RiskIndicatorController {

    private final RiskIndicatorService riskIndicatorService;

    // ======================== 通用CRUD接口 ========================

    @ApiOperation("新增风险指标")
    @PostMapping
    public Result<RiskIndicatorVO> addRiskIndicator(
            @ApiParam(ADD_RISK_INDICATOR)
            @Valid @RequestBody RiskIndicatorDTO dto) {
        RiskIndicatorVO vo = riskIndicatorService.add(dto);
        return Result.ok(RISK_INDICATOR_ADD_SUCCESS, vo);
    }

    @ApiOperation("删除风险指标")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRiskIndicator(
            @ApiParam(RISK_INDICATOR_ID)
            @PathVariable
            @ValidId(bizName = RISK_INDICATOR_ID) Long id) {
        riskIndicatorService.delete(id);
        return Result.ok(RISK_INDICATOR_DELETE_SUCCESS);
    }

    @ApiOperation("批量删除风险指标")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteRiskIndicator(
            @ApiParam(RISK_INDICATOR_ID_LIST)
            @RequestBody
            @NotEmpty(message = RISK_INDICATOR_ID_LIST_EMPTY) List<Long> ids) {
        riskIndicatorService.batchDelete(ids);
        return Result.ok(RISK_INDICATOR_BATCH_DELETE_SUCCESS);
    }

    @ApiOperation("修改风险指标")
    @PutMapping("/{id}")
    public Result<RiskIndicatorVO> updateRiskIndicator(
            @ApiParam(RISK_INDICATOR_ID)
            @PathVariable
            @ValidId(bizName = RISK_INDICATOR_ID) Long id,
            @ApiParam(UPDATE_RISK_INDICATOR)
            @Valid @RequestBody RiskIndicatorDTO dto) {
        RiskIndicatorVO vo = riskIndicatorService.update(id, dto);
        return Result.ok(RISK_INDICATOR_UPDATE_SUCCESS, vo);
    }

    @ApiOperation("根据 ID 查询风险指标")
    @GetMapping("/{id}")
    public Result<RiskIndicatorVO> getRiskIndicatorById(
            @ApiParam(RISK_INDICATOR_ID)
            @PathVariable
            @ValidId(bizName = RISK_INDICATOR_ID) Long id) {
        RiskIndicatorVO vo = riskIndicatorService.getOneById(id);
        return Result.ok(RISK_INDICATOR_GET_SUCCESS, vo);
    }

    @ApiOperation("多条件组合分页查询风险指标")
    @ApiImplicitParam(name = "queryDTO", value = "风险指标查询条件（含分页）",
            required = true, dataType = "RiskIndicatorQueryDTO", paramType = "body")
    @PostMapping("/search")
    public Result<PageVO<RiskIndicatorVO>> searchRiskIndicators(
            @Valid @RequestBody RiskIndicatorQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<RiskIndicatorVO> pageVO = riskIndicatorService.search(queryDTO);
        return Result.ok(String.format(RISK_INDICATOR_GET_COUNT_SUCCESS, pageVO.getTotal()), pageVO);
    }

    // ======================== 个性化业务接口 ========================

    @ApiOperation(
            value = "查询工人最新风险指标",
            notes = "返回指定工人 create_time 最大的一条风险指标记录。"
    )
    @ApiImplicitParam(name = "workerId", value = WORKER_ID,
            required = true, dataType = "Long", paramType = "path")
    @GetMapping("/latest/{workerId}")
    public Result<RiskIndicatorVO> getLatestRiskIndicatorByWorkerId(
            @ApiParam(WORKER_ID)
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORKER_ID) Long workerId) {
        RiskIndicatorVO vo = riskIndicatorService.getLatestRiskIndicatorByWorkerId(workerId);
        return Result.ok(RISK_INDICATOR_GET_LATEST_SUCCESS, vo);
    }

    @ApiOperation(
            value = "统计工人风险等级人数分布",
            notes = "按工人去重，取每人最新一条记录的风险等级进行分组统计。"
    )
    @GetMapping("/count/risk-level")
    public Result<RiskLevelCountVO> countDistinctWorkerByRiskLevel() {
        RiskLevelCountVO vo = riskIndicatorService.countDistinctWorkerByRiskLevel();
        return Result.ok(RISK_INDICATOR_STATISTIC_RISK_LEVEL_COUNT_SUCCESS, vo);
    }

    @ApiOperation(
            value = "统计当日各时段高风险工人数",
            notes = "按 4 小时为一段统计当日高风险（中 + 高 + 严重风险）工人数，statDate 不传则默认当天。"
    )
    @GetMapping("/count/time-period")
    public Result<RiskTimePeriodCountVO> countHighRiskWorkerByTimePeriod(
            @ApiParam(value = "统计日期（yyyy-MM-dd），默认当天", example = "2026-02-01")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate statDate) {
        RiskTimePeriodCountVO vo = riskIndicatorService.countHighRiskWorkerByTimePeriod(statDate);
        return Result.ok(RISK_INDICATOR_STATISTIC_HIGH_RISK_COUNT_SUCCESS, vo);
    }
}