package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkAreaQueryDTO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkAreaVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.area.WorkAreaRiskCountVO;
import gang.lu.riskmanagementproject.service.WorkAreaService;
import gang.lu.riskmanagementproject.util.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

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
@RequiredArgsConstructor
@Api(tags = "工作区域管理 API")
@Validated
public class WorkAreaController {

    private final WorkAreaService workAreaService;

    /**
     * 新增工作区域
     */
    @PostMapping
    @ApiOperation("新增工作区域")
    @ApiImplicitParam(name = "dto", value = "工作区域数据", required = true, dataType = "WorkAreaDTO", paramType = "body")
    public Result<Boolean> addWorkArea(@Valid @RequestBody WorkAreaDTO dto) {
        boolean result = workAreaService.addWorkArea(dto);
        return Result.ok(WORK_AREA_ADD_SUCCESS, result);
    }

    /**
     * 删除工作区域（按ID）
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除工作区域")
    @ApiImplicitParam(name = "id", value = "工作区域ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<Boolean> deleteWorkArea(@PathVariable
                                          @ValidId(bizName = BusinessConstants.WORK_AREA_ID) Long id) {
        boolean result = workAreaService.deleteWorkArea(id);
        return Result.ok(WORK_AREA_DELETE_SUCCESS, result);
    }

    /**
     * 修改工作区域
     */
    @PutMapping("/{id}")
    @ApiOperation("修改工作区域")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工作区域ID", required = true, dataType = "Long", paramType = "path", example = "1"),
            @ApiImplicitParam(name = "dto", value = "工作区域更新数据", required = true, dataType = "WorkAreaDTO", paramType = "body")
    })
    public Result<Boolean> updateWorkArea(
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORK_AREA_ID) Long id,
            @Valid @RequestBody WorkAreaDTO dto) {
        boolean result = workAreaService.updateWorkArea(id, dto);
        return Result.ok(WORK_AREA_UPDATE_SUCCESS, result);
    }

    /**
     * 按ID查询工作区域
     */
    @GetMapping("/{id}")
    @ApiOperation("按ID查询工作区域")
    @ApiImplicitParam(name = "id", value = "工作区域ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<WorkAreaVO> getWorkAreaById(
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORK_AREA_ID) Long id) {
        WorkAreaVO vo = workAreaService.getWorkAreaById(id);
        return Result.ok(WORK_AREA_GET_SUCCESS, vo);
    }

    /**
     * 按区域编码查询工作区域
     */
    @GetMapping("/code/{areaCode}")
    @ApiOperation("按区域编码查询工作区域")
    @ApiImplicitParam(name = "areaCode", value = "区域编码", required = true, dataType = "String", paramType = "path", example = "AREA_001")
    public Result<List<WorkAreaVO>> getWorkAreaByCode(
            @PathVariable
            @NotBlank(message = "区域编码不能为空") String areaCode) {
        List<WorkAreaVO> areas = workAreaService.getWorkAreaByCode(areaCode);
        return Result.ok(String.format(WORK_AREA_GET_BY_CODE_SUCCESS, areas.size()), areas);
    }

    /**
     * 多条件分页查询工作区域
     */
    @PostMapping("/search")
    @ApiOperation("多条件组合分页查询工作区域（支持ID/编码/名称/风险等级等筛选）")
    @ApiImplicitParam(name = "queryDTO", value = "工作区域查询条件（含分页）", required = true, dataType = "WorkAreaQueryDTO", paramType = "body")
    public Result<PageVO<WorkAreaVO>> searchWorkAreas(@Valid @RequestBody WorkAreaQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<WorkAreaVO> pageVO = workAreaService.searchWorkAreas(queryDTO);
        return Result.ok(WORK_AREA_GET_ALL_BY_PAGE_CONDITIONAL_SUCCESS, pageVO);
    }

    /**
     * 按风险等级统计工作区域数量
     */
    @GetMapping("/count/risk-level")
    @ApiOperation("按风险等级统计工作区域数量")
    public Result<WorkAreaRiskCountVO> countWorkAreaByRiskLevel() {
        WorkAreaRiskCountVO countVO = workAreaService.countWorkAreaByRiskLevel();
        return Result.ok(WORK_AREA_STATISTIC_RISK_LEVEL_COUNT_SUCCESS, countVO);
    }

}
