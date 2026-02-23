package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkerQueryDTO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerStatusCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerTypeCountVO;
import gang.lu.riskmanagementproject.helper.PageHelper;
import gang.lu.riskmanagementproject.service.WorkerService;
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

import static gang.lu.riskmanagementproject.common.global.GlobalBusinessConstants.*;
import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.WORKER_CODE_EMPTY;
import static gang.lu.riskmanagementproject.message.FailedMessages.WORKER_DELETE_BATCH_ID_EMPTY;
import static gang.lu.riskmanagementproject.message.SuccessMessages.*;

/**
 * 工人信息管理接口
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Api(tags = "工人管理")
@Validated
@RestController
@RequestMapping("/api/worker")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    // ======================== 通用CRUD接口 ========================

    @ApiOperation("新增工人")
    @PostMapping
    public Result<WorkerVO> addWorker(
            @ApiParam(ADD_WORKER)
            @Valid @RequestBody WorkerDTO dto) {
        WorkerVO vo = workerService.add(dto);
        return Result.ok(WORKER_CREATE_SUCCESS, vo);
    }

    @ApiOperation("删除工人")
    @DeleteMapping("/{id}")
    public Result<Void> deleteWorker(
            @ApiParam(WORKER_ID)
            @PathVariable
            @ValidId(bizName = WORKER_ID) Long id) {
        workerService.delete(id);
        return Result.ok(WORKER_DELETE_SUCCESS);
    }

    @ApiOperation("批量删除工人")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteWorker(
            @ApiParam(WORKER_ID_LIST)
            @RequestBody
            @NotEmpty(message = WORKER_DELETE_BATCH_ID_EMPTY) List<Long> ids) {
        workerService.batchDelete(ids);
        return Result.ok(WORKER_DELETE_BATCH_SUCCESS);
    }

    @ApiOperation("修改工人信息")
    @PutMapping("/{id}")
    public Result<WorkerVO> updateWorker(
            @ApiParam(WORKER_ID)
            @PathVariable
            @ValidId(bizName = WORKER_ID) Long id,
            @ApiParam(UPDATE_WORKER)
            @Valid @RequestBody WorkerDTO dto) {
        WorkerVO vo = workerService.update(id, dto);
        return Result.ok(WORKER_UPDATE_SUCCESS, vo);
    }

    @ApiOperation("根据 ID 查询工人")
    @GetMapping("/{id}")
    public Result<WorkerVO> getWorkerById(
            @ApiParam(WORKER_ID)
            @PathVariable
            @ValidId(bizName = WORKER_ID) Long id) {
        WorkerVO vo = workerService.getOneById(id);
        return Result.ok(WORKER_GET_SUCCESS, vo);
    }

    @ApiOperation("多条件组合分页查询工人")
    @ApiImplicitParam(name = "queryDTO", value = "工人查询条件（含分页）",
            required = true, dataType = "WorkerQueryDTO", paramType = "body")
    @PostMapping("/search")
    public Result<PageVO<WorkerVO>> searchWorkers(
            @Valid @RequestBody WorkerQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<WorkerVO> pageVO = workerService.search(queryDTO);
        return Result.ok(String.format(WORKER_GET_COUNT_SUCCESS, pageVO.getTotal()), pageVO);
    }

    // ======================== 个性化业务接口 ========================

    @ApiOperation(
            value = "根据工号查询工人",
            notes = "工号全局唯一，精确匹配，返回单条记录。"
    )
    @ApiImplicitParam(name = "workerCode", value = WORKER_CODE,
            required = true, dataType = "String", paramType = "path", example = "W001")
    @GetMapping("/code/{workerCode}")
    public Result<WorkerVO> getWorkerByCode(
            @ApiParam(WORKER_CODE)
            @PathVariable
            @NotBlank(message = WORKER_CODE_EMPTY) String workerCode) {
        WorkerVO vo = workerService.getWorkerByCode(workerCode);
        return Result.ok(WORKER_GET_SUCCESS, vo);
    }

    @ApiOperation(
            value = "统计各状态工人数量",
            notes = "按工人状态（正常 / 异常 / 离线）分组统计，并返回总数。"
    )
    @GetMapping("/count/status")
    public Result<WorkerStatusCountVO> countWorkerByStatus() {
        WorkerStatusCountVO vo = workerService.countWorkerByStatus();
        return Result.ok(WORKER_STATISTIC_COUNT_BY_STATUS_SUCCESS, vo);
    }

    @ApiOperation(
            value = "统计各工种工人数量",
            notes = "按工种（高空作业 / 受限空间 / 设备操作 / 正常作业）分组统计，并返回总数。"
    )
    @GetMapping("/count/work-type")
    public Result<WorkerTypeCountVO> countWorkerByWorkType() {
        WorkerTypeCountVO vo = workerService.countWorkerByWorkType();
        return Result.ok(WORKER_STATISTIC_COUNT_BY_WORKTYPE_SUCCESS, vo);
    }
}