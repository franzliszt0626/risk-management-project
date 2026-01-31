package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.vo.WorkerVO;
import gang.lu.riskmanagementproject.service.WorkerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gang.lu.riskmanagementproject.domain.enums.Status.fromValue;


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
    public Result<Void> createWorker(@RequestBody WorkerDTO dto) {
        workerService.createWorker(dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据工人ID删除工人（逻辑删除或物理删除）")
    public Result<Void> deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @ApiOperation("根据工人ID更新工人信息")
    public Result<Void> updateWorker(@PathVariable Long id, @RequestBody WorkerDTO dto) {
        workerService.updateWorker(id, dto);
        return Result.ok();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据工人ID精确查询单个工人信息")
    public Result<WorkerVO> getWorkerById(@PathVariable Long id) {
        WorkerVO vo = workerService.getWorkerById(id);
        return Result.ok(vo);
    }

    @GetMapping("/code/{workerCode}")
    @ApiOperation("根据工号（workerCode）精确查询工人")
    public Result<WorkerVO> getWorkerByCode(@PathVariable String workerCode) {
        WorkerVO vo = workerService.getWorkerByCode(workerCode);
        return Result.ok(vo);
    }

    @GetMapping("/search/name")
    @ApiOperation("按姓名模糊查询工人列表（支持部分匹配）")
    public Result<List<WorkerVO>> searchByName(@RequestParam String name) {
        List<WorkerVO> list = workerService.searchWorkersByName(name);
        return Result.ok(list);
    }

    @GetMapping("/status/{statusValue}")
    @ApiOperation("按工人状态查询列表（状态值必须为：正常 / 异常 / 离线）")
    public Result<List<WorkerVO>> getByStatus(@PathVariable String statusValue) {
        Status status = fromValue(statusValue);
        List<WorkerVO> list = workerService.getWorkersByStatus(status);
        return Result.ok(list);
    }

    @GetMapping("/position/{position}")
    @ApiOperation("按岗位名称精确查询工人列表（如：焊工、电工）")
    public Result<List<WorkerVO>> getByPosition(@PathVariable String position) {
        List<WorkerVO> list = workerService.getWorkersByPosition(position);
        return Result.ok(list);
    }
}