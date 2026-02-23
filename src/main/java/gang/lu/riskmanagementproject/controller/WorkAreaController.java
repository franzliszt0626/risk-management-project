package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
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

import static gang.lu.riskmanagementproject.common.BusinessConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;
import static gang.lu.riskmanagementproject.message.SuccessMessages.*;

/**
 * 工作区域管理接口
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Api(tags = "工作区域管理")
@Validated
@RestController
@RequestMapping("/api/work-area")
@RequiredArgsConstructor
public class WorkAreaController {

    private final WorkAreaService workAreaService;

    // ======================== 通用CRUD接口 ========================

    @ApiOperation("新增工作区域")
    @PostMapping
    public Result<WorkAreaVO> addWorkArea(
            @ApiParam(ADD_WORK_AREA)
            @Valid @RequestBody WorkAreaDTO dto) {
        WorkAreaVO vo = workAreaService.add(dto);
        return Result.ok(WORK_AREA_ADD_SUCCESS, vo);
    }

    @ApiOperation("删除工作区域")
    @DeleteMapping("/{id}")
    public Result<Void> deleteWorkArea(
            @ApiParam(WORK_AREA_ID)
            @PathVariable
            @ValidId(bizName = WORK_AREA_ID) Long id) {
        workAreaService.delete(id);
        return Result.ok(WORK_AREA_DELETE_SUCCESS);
    }

    @ApiOperation("批量删除工作区域")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteWorkArea(
            @ApiParam(WORK_AREA_ID_LIST)
            @RequestBody
            @NotEmpty(message = WORK_AREA_DELETE_BATCH_ID_EMPTY) List<Long> ids) {
        workAreaService.batchDelete(ids);
        return Result.ok(WORK_AREA_DELETE_BATCH_SUCCESS);
    }

    @ApiOperation("修改工作区域")
    @PutMapping("/{id}")
    public Result<WorkAreaVO> updateWorkArea(
            @ApiParam(WORK_AREA_ID)
            @PathVariable
            @ValidId(bizName = WORK_AREA_ID) Long id,
            @ApiParam(UPDATE_WORK_AREA)
            @Valid @RequestBody WorkAreaDTO dto) {
        WorkAreaVO vo = workAreaService.update(id, dto);
        return Result.ok(WORK_AREA_UPDATE_SUCCESS, vo);
    }

    @ApiOperation("根据 ID 查询工作区域")
    @GetMapping("/{id}")
    public Result<WorkAreaVO> getWorkAreaById(
            @ApiParam(WORK_AREA_ID)
            @PathVariable
            @ValidId(bizName = WORK_AREA_ID) Long id) {
        WorkAreaVO vo = workAreaService.getOneById(id);
        return Result.ok(WORK_AREA_GET_SUCCESS, vo);
    }

    @ApiOperation("多条件组合分页查询工作区域")
    @ApiImplicitParam(name = "queryDTO", value = "工作区域查询条件（含分页）",
            required = true, dataType = "WorkAreaQueryDTO", paramType = "body")
    @PostMapping("/search")
    public Result<PageVO<WorkAreaVO>> searchWorkAreas(
            @Valid @RequestBody WorkAreaQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<WorkAreaVO> pageVO = workAreaService.search(queryDTO);
        return Result.ok(String.format(WORK_AREA_GET_COUNT_SUCCESS, pageVO.getTotal()), pageVO);
    }

    // ======================== 个性化业务接口 ========================

    @ApiOperation(
            value = "根据区域编码查询工作区域",
            notes = "区域编码精确匹配，编码全局唯一时返回单条记录。"
    )
    @ApiImplicitParam(name = "areaCode", value = WORK_AREA_CODE,
            required = true, dataType = "String", paramType = "path", example = "AREA_001")
    @GetMapping("/code/{areaCode}")
    public Result<List<WorkAreaVO>> getWorkAreaByCode(
            @ApiParam(WORK_AREA_CODE)
            @PathVariable
            @NotBlank(message = WORK_AREA_CODE_EMPTY) String areaCode) {
        List<WorkAreaVO> areas = workAreaService.getWorkAreaByCode(areaCode);
        return Result.ok(String.format(WORK_AREA_GET_BY_CODE_SUCCESS, areas.size()), areas);
    }

    @ApiOperation(
            value = "统计各风险等级工作区域数量",
            notes = "按区域风险等级（低风险 / 中风险 / 高风险）分组统计，并返回总数。"
    )
    @GetMapping("/count/risk-level")
    public Result<WorkAreaRiskCountVO> countWorkAreaByRiskLevel() {
        WorkAreaRiskCountVO vo = workAreaService.countWorkAreaByRiskLevel();
        return Result.ok(WORK_AREA_STATISTIC_RISK_LEVEL_COUNT_SUCCESS, vo);
    }
}