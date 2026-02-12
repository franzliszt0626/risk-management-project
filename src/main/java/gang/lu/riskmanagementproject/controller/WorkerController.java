package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.dto.query.PageQueryDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkerQueryDTO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerStatusCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerTypeCountVO;
import gang.lu.riskmanagementproject.service.WorkerService;
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
import javax.validation.constraints.NotEmpty;
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


    @PostMapping("/search")
    @ApiOperation("多条件组合分页查询工人（支持姓名/岗位/状态/工种/工龄等筛选）")
    @ApiImplicitParam(name = "workerQueryDTO", value = "组合查询条件（含分页参数）", required = true, dataType = "WorkerQueryDTO", paramType = "body")
    public Result<PageVO<WorkerVO>> searchWorkers(@Valid @RequestBody WorkerQueryDTO workerQueryDTO) {
        PageHelper.bindGlobalDefaultRule(workerQueryDTO);
        PageVO<WorkerVO> pageVO = workerService.searchWorkers(workerQueryDTO);
        return Result.ok(String.format(WORKER_GET_COUNT_SUCCESS, pageVO.getTotal()), pageVO);
    }


    @PostMapping
    @ApiOperation("创建新工人信息")
    @ApiImplicitParam(name = "dto", value = "工人信息DTO", required = true, dataType = "WorkerDTO", paramType = "body")
    public Result<Void> createWorker(@Valid @RequestBody WorkerDTO dto) {
        workerService.createWorker(dto);
        return Result.ok(WORKER_CREATE_SUCCESS);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据工人ID删除工人")
    @ApiImplicitParam(name = "id", value = "工人ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<Void> deleteWorker(
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORKER_ID) Long id) {
        workerService.deleteWorker(id);
        return Result.ok(WORKER_DELETE_SUCCESS);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据工人ID更新工人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工人ID", required = true, dataType = "Long", paramType = "path", example = "1"),
            @ApiImplicitParam(name = "dto", value = "工人更新数据", required = true, dataType = "WorkerDTO", paramType = "body")
    })
    public Result<Void> updateWorker(
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORKER_ID) Long id,
            @Valid @RequestBody WorkerDTO dto
    ) {
        workerService.updateWorker(id, dto);
        return Result.ok(WORKER_UPDATE_SUCCESS);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据工人ID精确查询单个工人信息")
    @ApiImplicitParam(name = "id", value = "工人ID", required = true, dataType = "Long", paramType = "path", example = "1")
    public Result<WorkerVO> getWorkerById(
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORKER_ID) Long id) {
        WorkerVO vo = workerService.getWorkerById(id);
        return Result.ok(WORKER_GET_SUCCESS, vo);
    }


    @GetMapping("/code/{workerCode}")
    @ApiOperation("根据工号（workerCode）精确查询工人")
    @ApiImplicitParam(name = "workerCode", value = "工号", required = true, dataType = "String", paramType = "path", example = "W001")
    public Result<WorkerVO> getWorkerByCode(@PathVariable @NotBlank(message = "工号不能为空") String workerCode) {
        WorkerVO vo = workerService.getWorkerByCode(workerCode);
        return Result.ok(WORKER_GET_SUCCESS, vo);
    }


    @GetMapping("/status/count")
    @ApiOperation("按状态统计工人数量（状态：正常/异常/离线）")
    public Result<WorkerStatusCountVO> countWorkerByStatus() {
        WorkerStatusCountVO countVO = workerService.countWorkerByStatus();
        return Result.ok(WORKER_STATISTIC_COUNT_BY_STATUS_SUCCESS, countVO);
    }

    @GetMapping("/work-type/count")
    @ApiOperation("按工种统计工人数量（工种：高空作业/受限空间/设备操作/正常作业）")
    public Result<WorkerTypeCountVO> countWorkerByWorkType() {
        WorkerTypeCountVO countVO = workerService.countWorkerByWorkType();
        return Result.ok(WORKER_STATISTIC_COUNT_BY_WORKTYPE_SUCCESS, countVO);
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除工人")
    @ApiImplicitParam(name = "ids", value = "工人ID列表", required = true, dataType = "List<Long>", paramType = "body", example = "[1,2,3]")
    public Result<Void> batchDeleteWorkers(
            @RequestBody
            @NotEmpty(message = "工人ID列表不能为空")
            @Valid List<@ValidId(bizName = BusinessConstants.WORKER_ID) Long> ids) {
        workerService.batchDeleteWorkers(ids);
        return Result.ok(WORKER_DELETE_BATCH_SUCCESS);
    }

}