package gang.lu.riskmanagementproject.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import gang.lu.riskmanagementproject.domain.query.WorkerQuery;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerStatusCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerTypeCountVO;
import gang.lu.riskmanagementproject.service.WorkerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
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
@Validated
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping
    @ApiOperation("创建新工人信息")
    @ApiImplicitParam(name = "dto", value = "工人信息DTO", required = true, dataType = "WorkerDTO", paramType = "body")
    public Result<Void> createWorker(@Valid @RequestBody WorkerDTO dto) {
        workerService.createWorker(dto);
        return Result.ok(WORKER_CREATE_SUCCESS_MESSAGE);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据工人ID删除工人")
    @ApiImplicitParam(name = "id", value = "工人ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<Void> deleteWorker(
            @PathVariable
            @NotNull(message = "工人ID不能为空")
            @Positive(message = "工人ID必须为正整数") Long id) {
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
            @PathVariable
            @NotNull(message = "工人ID不能为空")
            @Positive(message = "工人ID必须为正整数") Long id,
            @Valid @RequestBody WorkerDTO dto
    ) {
        workerService.updateWorker(id, dto);
        return Result.ok(WORKER_UPDATE_SUCCESS_MESSAGE);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据工人ID精确查询单个工人信息")
    @ApiImplicitParam(name = "id", value = "工人ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<WorkerVO> getWorkerById(
            @PathVariable
            @NotNull(message = "工人ID不能为空")
            @Positive(message = "工人ID必须为正整数") Long id) {
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
            @RequestParam(required = false)
            @Min(value = 1, message = "页码不能小于1") Integer pageNum,
            @RequestParam(required = false)
            @Min(value = 1, message = "每页条数不能小于1")
            @Max(value = 50, message = "每页条数不能超过50") Integer pageSize
    ) {
        Page<WorkerVO> workerVOPage = workerService.getAllWorkers(pageNum, pageSize);
        return Result.ok(WORKER_GET_SUCCESS_MESSAGE, workerVOPage);
    }

    @GetMapping("/code/{workerCode}")
    @ApiOperation("根据工号（workerCode）精确查询工人")
    @ApiImplicitParam(name = "workerCode", value = "工号", required = true, dataType = "String", paramType = "path", example = "W001")
    public Result<WorkerVO> getWorkerByCode(@PathVariable @NotBlank(message = "工号不能为空") String workerCode) {
        WorkerVO vo = workerService.getWorkerByCode(workerCode);
        return Result.ok(WORKER_GET_SUCCESS_MESSAGE, vo);
    }

    @GetMapping("/search/name")
    @ApiOperation("按姓名模糊查询工人列表")
    @ApiImplicitParam(name = "name", value = "工人姓名（模糊匹配）", required = true, dataType = "String", paramType = "query", example = "张三")
    public Result<List<WorkerVO>> searchByName(@RequestParam @NotBlank(message = "姓名不能为空") String name) {
        List<WorkerVO> list = workerService.searchWorkersByName(name);
        return Result.ok(String.format(WORKER_GET_COUNT_SUCCESS_MESSAGE, list.size()), list);
    }

    @GetMapping("/status/{statusValue}")
    @ApiOperation("按工人状态查询列表（状态值：正常/异常/离线）")
    @ApiImplicitParam(name = "statusValue", value = "工人状态", required = true, dataType = "String", paramType = "path", example = "正常")
    public Result<List<WorkerVO>> getByStatus(
            @PathVariable
            @NotBlank(message = "工人状态不能为空")
            @Length(max = 10, message = "工人状态长度不能超过10个字符") String statusValue) {
        // 复用枚举fromValue方法，统一转换逻辑
        Status status = Status.fromValue(statusValue);
        List<WorkerVO> list = workerService.getWorkersByStatus(status);
        return Result.ok(String.format(WORKER_GET_COUNT_BY_STATUS_SUCCESS_MESSAGE, statusValue, list.size()), list);
    }

    @GetMapping("/position/{position}")
    @ApiOperation("按岗位名称模糊查询工人列表")
    @ApiImplicitParam(name = "position", value = "岗位名称", required = true, dataType = "String", paramType = "path", example = "焊工")
    public Result<List<WorkerVO>> getByPosition(
            @PathVariable
            @NotBlank(message = "岗位名称不能为空")
            @Length(max = 100, message = "岗位名称长度不能超过100个字符") String position) {
        List<WorkerVO> list = workerService.getWorkersByPosition(position);
        return Result.ok(String.format(WORKER_GET_COUNT_BY_POSITION_SUCCESS_MESSAGE, position, list.size()), list);
    }

    @GetMapping("/work-type/{workTypeValue}")
    @ApiOperation("按工种查询列表（工种：高空作业/受限空间/设备操作/正常作业）")
    @ApiImplicitParam(name = "workTypeValue", value = "工种", required = true, dataType = "String", paramType = "path", example = "高空作业")
    public Result<List<WorkerVO>> getByWorkType(
            @PathVariable
            @NotBlank(message = "工种不能为空")
            @Length(max = 20, message = "工种长度不能超过20个字符") String workTypeValue) {
        WorkType workType = WorkType.fromValue(workTypeValue);
        List<WorkerVO> list = workerService.getWorkersByWorkType(workType);
        return Result.ok(String.format(WORKER_GET_COUNT_BY_WORKTYPE_SUCCESS_MESSAGE, workTypeValue, list.size()), list);
    }

    @GetMapping("/status/count")
    @ApiOperation("按状态统计工人数量（状态：正常/异常/离线）")
    public Result<WorkerStatusCountVO> countWorkerByStatus() {
        WorkerStatusCountVO countVO = workerService.countWorkerByStatus();
        return Result.ok(WORKER_STATISTIC_COUNT_BY_STATUS_SUCCESS_MESSAGE, countVO);
    }

    @GetMapping("/work-type/count")
    @ApiOperation("按工种统计工人数量（工种：高空作业/受限空间/设备操作/正常作业）")
    public Result<WorkerTypeCountVO> countWorkerByWorkType() {
        WorkerTypeCountVO countVO = workerService.countWorkerByWorkType();
        return Result.ok(WORKER_STATISTIC_COUNT_BY_WORKTYPE_SUCCESS_MESSAGE, countVO);
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除工人")
    @ApiImplicitParam(name = "ids", value = "工人ID列表", required = true, dataType = "List<Long>", paramType = "body", example = "[1,2,3]")
    public Result<Void> batchDeleteWorkers(
            @RequestBody
            @NotEmpty(message = "工人ID列表不能为空")
            @Valid List<@Positive(message = "工人ID必须为正整数") Long> ids) {
        workerService.batchDeleteWorkers(ids);
        return Result.ok(WORKER_DELETE_BATCH_SUCCESS_MESSAGE);
    }

    @PostMapping("/search")
    @ApiOperation("组合条件分页查询工人")
    @ApiImplicitParam(name = "workerQuery", value = "组合查询条件", required = true, dataType = "WorkerQuery", paramType = "body")
    public Result<Page<WorkerVO>> searchWorkers(@Valid @RequestBody WorkerQuery workerQuery) {
        Page<WorkerVO> page = workerService.searchWorkers(workerQuery);
        return Result.ok(String.format(WORKER_GET_BY_CONDITION_SUCCESS_MESSAGE, page.getTotal()), page);
    }

}