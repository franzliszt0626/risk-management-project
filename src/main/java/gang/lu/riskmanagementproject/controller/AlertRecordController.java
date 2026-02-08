package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import gang.lu.riskmanagementproject.domain.vo.AlertRecordVO;
import gang.lu.riskmanagementproject.service.AlertRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gang.lu.riskmanagementproject.common.SuccessMessages.*;

/**
 * <p>
 * 预警记录表 前端控制器
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@RestController
@RequestMapping("/api/alert-record")
@Api(tags = "预警记录管理")
@RequiredArgsConstructor
public class AlertRecordController {

    private final AlertRecordService alertRecordService;

    /**
     * 新增预警记录
     */
    @ApiOperation("新增预警记录")
    @PostMapping
    public Result<AlertRecordVO> addAlertRecord(@RequestBody AlertRecordDTO dto) {
        AlertRecordVO vo = alertRecordService.addAlertRecord(dto);
        return Result.ok(ALERT_RECORD_ADD_SUCCESS_MESSAGE, vo);
    }

    /**
     * 删除预警记录
     */
    @ApiOperation("删除预警记录")
    @DeleteMapping("/{id}")
    public Result<?> deleteAlertRecord(@ApiParam("预警记录ID") @PathVariable Long id) {
        alertRecordService.deleteAlertRecord(id);
        return Result.ok(ALERT_RECORD_DELETE_SUCCESS_MESSAGE);
    }

    /**
     * 修改预警记录
     */
    @ApiOperation("修改预警记录")
    @PutMapping("/{id}")
    public Result<AlertRecordVO> updateAlertRecord(
            @ApiParam("预警记录ID") @PathVariable Long id,
            @RequestBody AlertRecordDTO dto) {
        AlertRecordVO vo = alertRecordService.updateAlertRecord(id, dto);
        return Result.ok(ALERT_RECORD_UPDATE_SUCCESS_MESSAGE, vo);
    }

    /**
     * 根据ID查询预警记录
     */
    @ApiOperation("根据ID查询预警记录")
    @GetMapping("/{id}")
    public Result<AlertRecordVO> getAlertRecordById(@ApiParam("预警记录ID") @PathVariable Long id) {
        AlertRecordVO vo = alertRecordService.getAlertRecordById(id);
        return Result.ok(ALERT_RECORD_GET_SUCCESS_MESSAGE, vo);
    }

    /**
     * 根据工人ID查询预警记录
     */
    @ApiOperation("根据工人ID查询预警记录")
    @GetMapping("/worker/{workerId}")
    public Result<List<AlertRecordVO>> getAlertRecordsByWorkerId(@ApiParam("工人ID") @PathVariable Long workerId) {
        List<AlertRecordVO> voList = alertRecordService.getAlertRecordsByWorkerId(workerId);
        return Result.ok(ALERT_RECORD_GET_SUCCESS_MESSAGE, voList);
    }

    /**
     * 根据预警级别查询
     */
    @ApiOperation("根据预警级别查询")
    @GetMapping("/level/{alertLevel}")
    public Result<List<AlertRecordVO>> getAlertRecordsByAlertLevel(@ApiParam("预警级别（警告/严重）") @PathVariable AlertLevel alertLevel) {
        List<AlertRecordVO> voList = alertRecordService.getAlertRecordsByAlertLevel(alertLevel);
        return Result.ok(ALERT_RECORD_GET_SUCCESS_MESSAGE, voList);
    }

    /**
     * 根据预警类型模糊查询
     */
    @ApiOperation("根据预警类型模糊查询")
    @GetMapping("/type")
    public Result<List<AlertRecordVO>> getAlertRecordsByAlertTypeLike(@ApiParam("预警类型关键词") @RequestParam String alertType) {
        List<AlertRecordVO> voList = alertRecordService.getAlertRecordsByAlertTypeLike(alertType);
        return Result.ok(ALERT_RECORD_GET_SUCCESS_MESSAGE, voList);
    }

    /**
     * 标记预警记录为已处理
     */
    @ApiOperation("标记预警记录为已处理")
    @PutMapping("/{id}/handle")
    public Result<?> markAlertRecordAsHandled(
            @ApiParam("预警记录ID") @PathVariable Long id,
            @ApiParam("处理人") @RequestParam String handledBy) {
        alertRecordService.markAlertRecordAsHandled(id, handledBy);
        return Result.ok(ALERT_RECORD_MARK_HANDLED_SUCCESS_MESSAGE);
    }
}
