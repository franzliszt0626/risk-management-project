package gang.lu.riskmanagementproject.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import gang.lu.riskmanagementproject.domain.vo.WorkerVO;
import gang.lu.riskmanagementproject.service.WorkerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026-01-31 17:36:08
 * @description 工人接口
 */
@RestController
@RequestMapping("/api/workers")
@Api(tags = "工人管理 API")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping
    @ApiOperation("创建新工人信息")
    @ApiParam(name = "dto", value = "工人信息DTO", required = true)
    public Result<Void> createWorker(@RequestBody WorkerDTO dto) {
        workerService.createWorker(dto);
        return Result.ok("创建工人成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据工人ID删除工人")
    @ApiParam(name = "id", value = "工人ID", required = true, example = "1")
    public Result<Void> deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return Result.ok("删除工人成功");
    }

    @PutMapping("/{id}")
    @ApiOperation("根据工人ID更新工人信息")
    @ApiParam(name = "id", value = "工人ID", required = true, example = "1")
    public Result<Void> updateWorker(
            @PathVariable Long id,
            @RequestBody WorkerDTO dto
    ) {
        workerService.updateWorker(id, dto);
        return Result.ok("更新工人成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("根据工人ID精确查询单个工人信息")
    @ApiParam(name = "id", value = "工人ID", required = true, example = "1")
    public Result<WorkerVO> getWorkerById(@PathVariable Long id) {
        WorkerVO vo = workerService.getWorkerById(id);
        return Result.ok("查询工人成功", vo);
    }

    @GetMapping
    @ApiOperation("分页列出所有工人信息")
    public Result<Page<WorkerVO>> getAllWorkers(
            @ApiParam(value = "页码（默认1）", example = "1")
            @RequestParam(required = false) Integer pageNum,
            @ApiParam(value = "每页条数（默认20，最大50）", example = "20")
            @RequestParam(required = false) Integer pageSize
    ) {
        Page<WorkerVO> workerVOPage = workerService.getAllWorkers(pageNum, pageSize);
        return Result.ok("分页查询工人成功", workerVOPage);
    }

    @GetMapping("/code/{workerCode}")
    @ApiOperation("根据工号（workerCode）精确查询工人")
    @ApiParam(name = "workerCode", value = "工号", required = true, example = "W001")
    public Result<WorkerVO> getWorkerByCode(@PathVariable String workerCode) {
        WorkerVO vo = workerService.getWorkerByCode(workerCode);
        return Result.ok("根据工号查询工人成功", vo);
    }

    @GetMapping("/search/name")
    @ApiOperation("按姓名模糊查询工人列表")
    @ApiParam(name = "name", value = "工人姓名（模糊匹配）", required = true, example = "张三")
    public Result<List<WorkerVO>> searchByName(@RequestParam String name) {
        List<WorkerVO> list = workerService.searchWorkersByName(name);
        return Result.ok(String.format("按姓名查询工人成功，共%s条", list.size()), list);
    }

    @GetMapping("/status/{statusValue}")
    @ApiOperation("按工人状态查询列表（状态值：正常/异常/离线）")
    @ApiParam(name = "statusValue", value = "工人状态", required = true, example = "正常")
    public Result<List<WorkerVO>> getByStatus(@PathVariable String statusValue) {
        // 复用枚举fromValue方法，统一转换逻辑
        Status status = Status.fromValue(statusValue);
        List<WorkerVO> list = workerService.getWorkersByStatus(status);
        return Result.ok(String.format("按状态[%s]查询工人成功，共%s条", statusValue, list.size()), list);
    }

    @GetMapping("/position/{position}")
    @ApiOperation("按岗位名称精确查询工人列表")
    @ApiParam(name = "position", value = "岗位名称", required = true, example = "焊工")
    public Result<List<WorkerVO>> getByPosition(@PathVariable String position) {
        List<WorkerVO> list = workerService.getWorkersByPosition(position);
        return Result.ok(String.format("按岗位[%s]查询工人成功，共%s条", position, list.size()), list);
    }


    @GetMapping("/work-type/{workTypeValue}")
    @ApiOperation("按工种查询列表（工种：高空作业/受限空间/设备操作/正常作业）")
    @ApiParam(name = "workTypeValue", value = "工种", required = true, example = "高空作业")
    public Result<List<WorkerVO>> getByWorkType(@PathVariable String workTypeValue) {
        WorkType workType = WorkType.fromValue(workTypeValue);
        List<WorkerVO> list = workerService.getWorkersByWorkType(workType);
        return Result.ok(String.format("按工种[%s]查询工人成功，共%s条", workTypeValue, list.size()), list);
    }
}