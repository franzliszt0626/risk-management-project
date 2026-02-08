package gang.lu.riskmanagementproject.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import gang.lu.riskmanagementproject.domain.vo.WorkerVO;
import gang.lu.riskmanagementproject.service.WorkerService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gang.lu.riskmanagementproject.common.SuccessMessages.*;


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
    @ApiImplicitParam(name = "dto", value = "工人信息DTO", required = true, dataType = "WorkerDTO", paramType = "body")
    public Result<Void> createWorker(@RequestBody WorkerDTO dto) {
        workerService.createWorker(dto);
        return Result.ok(WORKER_CREATE_SUCCESS_MESSAGE);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据工人ID删除工人")
    @ApiImplicitParam(name = "id", value = "工人ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<Void> deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return Result.ok(WORKER_DELETE_SUCCESS_MESSAGE);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据工人ID更新工人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工人ID", required = true, dataType = "Long", paramType = "path", example = "1"),
            @ApiImplicitParam(name = "dto", value = "工人更新数据", required = true, dataType = "WorkerDTO", paramType = "body")
    })
    public Result<Void> updateWorker(
            @PathVariable Long id,
            @RequestBody WorkerDTO dto
    ) {
        workerService.updateWorker(id, dto);
        return Result.ok(WORKER_UPDATE_SUCCESS_MESSAGE);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据工人ID精确查询单个工人信息")
    @ApiImplicitParam(name = "id", value = "工人ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<WorkerVO> getWorkerById(@PathVariable Long id) {
        WorkerVO vo = workerService.getWorkerById(id);
        return Result.ok(WORKER_GET_SUCCESS_MESSAGE, vo);
    }

    @GetMapping
    @ApiOperation("分页列出所有工人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码（默认1）", dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数（默认20，最大50）", dataType = "Integer", paramType = "query", example = "20")
    })
    public Result<Page<WorkerVO>> getAllWorkers(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize
    ) {
        Page<WorkerVO> workerVOPage = workerService.getAllWorkers(pageNum, pageSize);
        return Result.ok(WORKER_GET_SUCCESS_MESSAGE, workerVOPage);
    }

    @GetMapping("/code/{workerCode}")
    @ApiOperation("根据工号（workerCode）精确查询工人")
    @ApiImplicitParam(name = "workerCode", value = "工号", required = true, dataType = "String", paramType = "path", example = "W001")
    public Result<WorkerVO> getWorkerByCode(@PathVariable String workerCode) {
        WorkerVO vo = workerService.getWorkerByCode(workerCode);
        return Result.ok(WORKER_GET_SUCCESS_MESSAGE, vo);
    }

    @GetMapping("/search/name")
    @ApiOperation("按姓名模糊查询工人列表")
    @ApiImplicitParam(name = "name", value = "工人姓名（模糊匹配）", required = true, dataType = "String", paramType = "query", example = "张三")
    public Result<List<WorkerVO>> searchByName(@RequestParam String name) {
        List<WorkerVO> list = workerService.searchWorkersByName(name);
        return Result.ok(String.format(WORKER_GET_COUNT_SUCCESS_MESSAGE, list.size()), list);
    }

    @GetMapping("/status/{statusValue}")
    @ApiOperation("按工人状态查询列表（状态值：正常/异常/离线）")
    @ApiImplicitParam(name = "statusValue", value = "工人状态", required = true, dataType = "String", paramType = "path", example = "正常")
    public Result<List<WorkerVO>> getByStatus(@PathVariable String statusValue) {
        // 复用枚举fromValue方法，统一转换逻辑
        Status status = Status.fromValue(statusValue);
        List<WorkerVO> list = workerService.getWorkersByStatus(status);
        return Result.ok(String.format(WORKER_GET_COUNT_BY_STATUS_SUCCESS_MESSAGE, statusValue, list.size()), list);
    }

    @GetMapping("/position/{position}")
    @ApiOperation("按岗位名称精确查询工人列表")
    @ApiImplicitParam(name = "position", value = "岗位名称", required = true, dataType = "String", paramType = "path", example = "焊工")
    public Result<List<WorkerVO>> getByPosition(@PathVariable String position) {
        List<WorkerVO> list = workerService.getWorkersByPosition(position);
        return Result.ok(String.format(WORKER_GET_COUNT_BY_POSITION_SUCCESS_MESSAGE, position, list.size()), list);
    }

    @GetMapping("/work-type/{workTypeValue}")
    @ApiOperation("按工种查询列表（工种：高空作业/受限空间/设备操作/正常作业）")
    @ApiImplicitParam(name = "workTypeValue", value = "工种", required = true, dataType = "String", paramType = "path", example = "高空作业")
    public Result<List<WorkerVO>> getByWorkType(@PathVariable String workTypeValue) {
        WorkType workType = WorkType.fromValue(workTypeValue);
        List<WorkerVO> list = workerService.getWorkersByWorkType(workType);
        return Result.ok(String.format(WORKER_GET_COUNT_BY_WORKTYPE_SUCCESS_MESSAGE, workTypeValue, list.size()), list);
    }
}