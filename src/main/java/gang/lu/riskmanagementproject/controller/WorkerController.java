package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkerQueryDTO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerStatusCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerTypeCountVO;
import gang.lu.riskmanagementproject.service.WorkerService;
import gang.lu.riskmanagementproject.helper.PageHelper;
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

import static gang.lu.riskmanagementproject.message.FailedMessages.WORKER_DELETE_BATCH_ID_EMPTY;
import static gang.lu.riskmanagementproject.message.SuccessMessages.*;

/**
 * <p>
 * 工人基本信息表 前端控制器
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@RestController
@RequestMapping("/api/workers")
@Api(tags = "工人管理 API")
@RequiredArgsConstructor
@Validated
public class WorkerController {

    private final WorkerService workerService;

    // ======================== 通用CRUD接口 ========================

    @ApiOperation("新增工人")
    @PostMapping
    public Result<WorkerVO> addWorker(
            @ApiParam("工人新增数据")
            @Valid @RequestBody WorkerDTO dto) {
        WorkerVO vo = workerService.add(dto);
        return Result.ok(WORKER_CREATE_SUCCESS, vo);
    }

    @ApiOperation("删除工人")
    @DeleteMapping("/{id}")
    public Result<Void> deleteWorker(
            @ApiParam("工人ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORKER_ID) Long id) {
        workerService.delete(id);
        return Result.ok(WORKER_DELETE_SUCCESS);
    }

    @ApiOperation("批量删除工人")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteWorker(
            @ApiParam("工人ID列表")
            @RequestBody
            @NotEmpty(message = WORKER_DELETE_BATCH_ID_EMPTY) List<Long> ids) {
        workerService.batchDelete(ids);
        return Result.ok(WORKER_DELETE_BATCH_SUCCESS);
    }

    @ApiOperation("修改工人")
    @PutMapping("/{id}")
    public Result<WorkerVO> updateWorker(
            @ApiParam("工人ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORKER_ID) Long id,
            @ApiParam("工人修改数据")
            @Valid @RequestBody WorkerDTO dto) {
        WorkerVO vo = workerService.update(id, dto);
        return Result.ok(WORKER_UPDATE_SUCCESS, vo);
    }

    @ApiOperation("根据ID查询工人")
    @GetMapping("/{id}")
    public Result<WorkerVO> getWorkerById(
            @ApiParam("工人ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.WORKER_ID) Long id) {
        WorkerVO vo = workerService.getOneById(id);
        return Result.ok(WORKER_GET_SUCCESS, vo);
    }

    @ApiOperation("多条件组合分页查询工人")
    @ApiImplicitParam(name = "queryDTO", value = "工人查询条件（含分页）", required = true, dataType = "WorkerQueryDTO", paramType = "body")
    @PostMapping("/search")
    public Result<PageVO<WorkerVO>> searchWorkers(
            @Valid @RequestBody WorkerQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<WorkerVO> pageVO = workerService.search(queryDTO);
        return Result.ok(String.format(WORKER_GET_COUNT_SUCCESS, pageVO.getTotal()), pageVO);
    }

    // ======================== 个性化业务接口 ========================

    @ApiOperation("根据工号查询工人")
    @ApiImplicitParam(name = "workerCode", value = "工号", required = true, dataType = "String", paramType = "path", example = "W001")
    @GetMapping("/code/{workerCode}")
    public Result<WorkerVO> getWorkerByCode(
            @ApiParam("工号")
            @PathVariable
            @NotBlank(message = "工号不能为空") String workerCode) {
        WorkerVO vo = workerService.getWorkerByCode(workerCode);
        return Result.ok(WORKER_GET_SUCCESS, vo);
    }

    @ApiOperation("按状态统计工人数量")
    @GetMapping("/status/count")
    public Result<WorkerStatusCountVO> countWorkerByStatus() {
        WorkerStatusCountVO countVO = workerService.countWorkerByStatus();
        return Result.ok(WORKER_STATISTIC_COUNT_BY_STATUS_SUCCESS, countVO);
    }

    @ApiOperation("按工种统计工人数量")
    @GetMapping("/work-type/count")
    public Result<WorkerTypeCountVO> countWorkerByWorkType() {
        WorkerTypeCountVO countVO = workerService.countWorkerByWorkType();
        return Result.ok(WORKER_STATISTIC_COUNT_BY_WORKTYPE_SUCCESS, countVO);
    }
}