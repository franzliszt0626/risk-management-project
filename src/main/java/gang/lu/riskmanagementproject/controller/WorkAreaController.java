package gang.lu.riskmanagementproject.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import gang.lu.riskmanagementproject.domain.vo.WorkAreaRiskCountVO;
import gang.lu.riskmanagementproject.domain.vo.WorkAreaVO;
import gang.lu.riskmanagementproject.service.WorkAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class WorkAreaController {

    private final WorkAreaService workAreaService;

    /**
     * 新增工作区域
     */
    @PostMapping
    @ApiOperation("新增工作区域")
    public Result<Boolean> addWorkArea(@RequestBody WorkAreaDTO dto) {
        boolean result = workAreaService.addWorkArea(dto);
        return Result.ok(result);
    }

    /**
     * 删除工作区域（按ID）
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除工作区域")
    public Result<Boolean> deleteWorkArea(
            @ApiParam(value = "区域ID", example = "1")
            @PathVariable Long id) {
        boolean result = workAreaService.deleteWorkArea(id);
        return Result.ok(result);
    }

    /**
     * 修改工作区域
     */
    @PutMapping("/{id}")
    @ApiOperation("修改工作区域")
    public Result<Boolean> updateWorkArea(
            @ApiParam(value = "区域ID", example = "1")
            @PathVariable Long id,
            @RequestBody WorkAreaDTO dto) {
        boolean result = workAreaService.updateWorkArea(id, dto);
        return Result.ok(result);
    }

    /**
     * 按ID查询工作区域
     */
    @GetMapping("/{id}")
    @ApiOperation("按ID查询工作区域")
    public Result<WorkAreaVO> getWorkAreaById(
            @ApiParam(value = "区域ID", example = "1")
            @PathVariable Long id) {
        WorkAreaVO vo = workAreaService.getWorkAreaById(id);
        return Result.ok(vo);
    }

    /**
     * 按区域编码查询工作区域
     */
    @GetMapping("/code/{areaCode}")
    @ApiOperation("按区域编码查询工作区域")
    public Result<List<WorkAreaVO>> getWorkAreaByCode(
            @ApiParam(value = "区域编码", example = "AREA_001")
            @PathVariable String areaCode) {
        List<WorkAreaVO> areas = workAreaService.getWorkAreaByCode(areaCode);
        return Result.ok(areas);
    }

    /**
     * 多条件分页查询工作区域
     */
    @GetMapping
    @ApiOperation("多条件分页查询工作区域")
    public Result<Page<WorkAreaVO>> queryWorkAreas(
            @ApiParam(value = "页码（默认1）", example = "1")
            @RequestParam(required = false) Integer pageNum,

            @ApiParam(value = "每页条数（默认20）", example = "20")
            @RequestParam(required = false) Integer pageSize,

            @ApiParam(value = "区域名称（模糊查询）", example = "高空作业")
            @RequestParam(required = false) String areaName,

            @ApiParam(value = "风险等级（低风险/中风险/高风险/严重风险）", example = "低风险")
            @RequestParam(required = false) RiskLevel riskLevel) {
        Page<WorkAreaVO> page = workAreaService.queryWorkAreas(pageNum, pageSize, areaName, riskLevel);
        return Result.ok(page);
    }

    /**
     * 分页展示所有工作区域（无筛选条件）
     */
    @GetMapping("/all")
    @ApiOperation("分页查询所有工作区域（无筛选条件）")
    public Result<Page<WorkAreaVO>> getAllWorkAreas(
            @ApiParam(value = "页码（默认1）", example = "1")
            @RequestParam(required = false) Integer pageNum,

            @ApiParam(value = "每页条数（默认20，最大50）", example = "20")
            @RequestParam(required = false) Integer pageSize) {
        Page<WorkAreaVO> page = workAreaService.getAllWorkAreas(pageNum, pageSize);
        return Result.ok(page);
    }

    /**
     * 按风险等级统计工作区域数量
     */
    @GetMapping("/count/risk-level")
    @ApiOperation("按风险等级统计工作区域数量")
    public Result<WorkAreaRiskCountVO> countWorkAreaByRiskLevel() {
        WorkAreaRiskCountVO countVO = workAreaService.countWorkAreaByRiskLevel();
        return Result.ok(countVO);
    }

}
