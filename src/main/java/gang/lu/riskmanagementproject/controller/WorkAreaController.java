package gang.lu.riskmanagementproject.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import gang.lu.riskmanagementproject.domain.vo.statistical.area.WorkAreaRiskCountVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkAreaVO;
import gang.lu.riskmanagementproject.service.WorkAreaService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
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
        return Result.ok(WORK_AREA_ADD_SUCCESS_MESSAGE, result);
    }

    /**
     * 删除工作区域（按ID）
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除工作区域")
    @ApiImplicitParam(name = "id", value = "工作区域ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<Boolean> deleteWorkArea(@PathVariable
                                          @NotNull(message = "工作区域ID不能为空")
                                          @Positive(message = "工作区域ID必须为正整数") Long id) {
        boolean result = workAreaService.deleteWorkArea(id);
        return Result.ok(WORK_AREA_DELETE_SUCCESS_MESSAGE, result);
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
            @NotNull(message = "工作区域ID不能为空")
            @Positive(message = "工作区域ID必须为正整数") Long id,
            @Valid @RequestBody WorkAreaDTO dto) {
        boolean result = workAreaService.updateWorkArea(id, dto);
        return Result.ok(WORK_AREA_UPDATE_SUCCESS_MESSAGE, result);
    }

    /**
     * 按ID查询工作区域
     */
    @GetMapping("/{id}")
    @ApiOperation("按ID查询工作区域")
    @ApiImplicitParam(name = "id", value = "工作区域ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<WorkAreaVO> getWorkAreaById(
            @PathVariable
            @NotNull(message = "工作区域ID不能为空")
            @Positive(message = "工作区域ID必须为正整数") Long id) {
        WorkAreaVO vo = workAreaService.getWorkAreaById(id);
        return Result.ok(WORK_AREA_GET_LATEST_SUCCESS_MESSAGE, vo);
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
        return Result.ok(String.format(WORK_AREA_GET_BY_CODE_SUCCESS_MESSAGE, areas.size()), areas);
    }

    /**
     * 多条件分页查询工作区域
     */
    @GetMapping
    @ApiOperation("多条件分页查询工作区域")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码（默认1）", dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数（默认20，最大50）", dataType = "Integer", paramType = "query", example = "20"),
            @ApiImplicitParam(name = "areaName", value = "区域名称（模糊查询）", dataType = "String", paramType = "query", example = "高空作业"),
            @ApiImplicitParam(name = "areaRiskLevel", value = "风险等级（低风险/中风险/高风险）", dataType = "String", paramType = "query", example = "低风险")
    })
    public Result<Page<WorkAreaVO>> queryWorkAreas(
            @RequestParam(required = false)
            @Min(value = 1, message = "页码不能小于1") Integer pageNum,
            @RequestParam(required = false)
            @Min(value = 1, message = "每页条数不能小于1")
            @Max(value = 50, message = "每页条数不能超过50") Integer pageSize,
            @RequestParam(required = false) String areaName,
            @RequestParam(required = false) AreaRiskLevel areaRiskLevel) {
        Page<WorkAreaVO> page = workAreaService.queryWorkAreas(pageNum, pageSize, areaName, areaRiskLevel);
        return Result.ok(WORK_AREA_GET_ALL_BY_PAGE_CONDITIONAL_SUCCESS_MESSAGE, page);
    }

    /**
     * 分页展示所有工作区域（无筛选条件）
     */
    @GetMapping("/all")
    @ApiOperation("分页查询所有工作区域（无筛选条件）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码（默认1）", dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数（默认20，最大50）", dataType = "Integer", paramType = "query", example = "20")
    })
    public Result<Page<WorkAreaVO>> getAllWorkAreas(
            @ApiParam(value = "页码（默认1）", example = "1")
            @RequestParam(required = false)
            @Min(value = 1, message = "页码不能小于1") Integer pageNum,
            @ApiParam(value = "每页条数（默认20，最大50）", example = "20")
            @RequestParam(required = false)
            @Min(value = 1, message = "每页条数不能小于1")
            @Max(value = 50, message = "每页条数不能超过50") Integer pageSize) {
        Page<WorkAreaVO> page = workAreaService.getAllWorkAreas(pageNum, pageSize);
        return Result.ok(WORK_AREA_GET_ALL_BY_PAGE_SUCCESS_MESSAGE, page);
    }

    /**
     * 按风险等级统计工作区域数量
     */
    @GetMapping("/count/risk-level")
    @ApiOperation("按风险等级统计工作区域数量")
    public Result<WorkAreaRiskCountVO> countWorkAreaByRiskLevel() {
        WorkAreaRiskCountVO countVO = workAreaService.countWorkAreaByRiskLevel();
        return Result.ok(WORK_AREA_STATISTIC_RISK_LEVEL_COUNT_SUCCESS_MESSAGE, countVO);
    }

}
