package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkAreaQueryDTO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkAreaVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.area.WorkAreaRiskCountVO;
import gang.lu.riskmanagementproject.helper.PageHelper;
import gang.lu.riskmanagementproject.service.WorkAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static gang.lu.riskmanagementproject.common.FailedMessages.*;
import static gang.lu.riskmanagementproject.common.SuccessMessages.*;

/**
 * <p>
 * 工作区域表 前端控制器
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@RestController
@RequestMapping("/api/work-area")
@Api(tags = "工作区域管理 API")
@RequiredArgsConstructor
@Validated
public class WorkAreaController {

    private final WorkAreaService workAreaService;

    // ======================== 通用CRUD接口 ========================

    @ApiOperation("新增工作区域")
    @PostMapping
    public Result<WorkAreaVO> addWorkArea(
            @ApiParam("工作区域新增数据")
            @Valid @RequestBody WorkAreaDTO dto) {
        WorkAreaVO vo = workAreaService.add(dto);
        return Result.ok(WORK_AREA_ADD_SUCCESS, vo);
    }

    @ApiOperation("删除工作区域")
    @DeleteMapping("/{id}")
    public Result<Void> deleteWorkArea(
            @ApiParam("工作区域ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORK_AREA_ID) Long id) {
        workAreaService.delete(id);
        return Result.ok(WORK_AREA_DELETE_SUCCESS);
    }

    @ApiOperation("批量删除工作区域")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteWorkArea(
            @ApiParam("工作区域ID列表")
            @RequestBody
            @NotEmpty(message = WORK_AREA_DELETE_BATCH_ID_EMPTY) List<Long> ids) {
        workAreaService.batchDelete(ids);
        return Result.ok(WORK_AREA_DELETE_BATCH_SUCCESS);
    }

    @ApiOperation("修改工作区域")
    @PutMapping("/{id}")
    public Result<WorkAreaVO> updateWorkArea(
            @ApiParam("工作区域ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORK_AREA_ID) Long id,
            @ApiParam("工作区域修改数据")
            @Valid @RequestBody WorkAreaDTO dto) {
        WorkAreaVO vo = workAreaService.update(id, dto);
        return Result.ok(WORK_AREA_UPDATE_SUCCESS, vo);
    }

    @ApiOperation("根据ID查询工作区域")
    @GetMapping("/{id}")
    public Result<WorkAreaVO> getWorkAreaById(
            @ApiParam("工作区域ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORK_AREA_ID) Long id) {
        WorkAreaVO vo = workAreaService.getOneById(id);
        return Result.ok(WORK_AREA_GET_SUCCESS, vo);
    }

    @ApiOperation("多条件组合分页查询工作区域")
    @ApiImplicitParam(name = "queryDTO", value = "工作区域查询条件（含分页）", required = true, dataType = "WorkAreaQueryDTO", paramType = "body")
    @PostMapping("/search")
    public Result<PageVO<WorkAreaVO>> searchWorkAreas(
            @Valid @RequestBody WorkAreaQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<WorkAreaVO> pageVO = workAreaService.search(queryDTO);
        return Result.ok(WORK_AREA_GET_ALL_BY_PAGE_CONDITIONAL_SUCCESS, pageVO);
    }

    // ======================== 个性化业务接口 ========================

    @ApiOperation("根据区域编码查询工作区域")
    @ApiImplicitParam(name = "areaCode", value = "区域编码", required = true, dataType = "String", paramType = "path", example = "AREA_001")
    @GetMapping("/code/{areaCode}")
    public Result<List<WorkAreaVO>> getWorkAreaByCode(
            @ApiParam("区域编码")
            @PathVariable
            @NotBlank(message = WORK_AREA_CODE_EMPTY) String areaCode) {
        List<WorkAreaVO> areas = workAreaService.getWorkAreaByCode(areaCode);
        return Result.ok(String.format(WORK_AREA_GET_BY_CODE_SUCCESS, areas.size()), areas);
    }

    @ApiOperation("按风险等级统计工作区域数量")
    @GetMapping("/count/risk-level")
    public Result<WorkAreaRiskCountVO> countWorkAreaByRiskLevel() {
        WorkAreaRiskCountVO countVO = workAreaService.countWorkAreaByRiskLevel();
        return Result.ok(WORK_AREA_STATISTIC_RISK_LEVEL_COUNT_SUCCESS, countVO);
    }
}