package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;
import gang.lu.riskmanagementproject.service.AlertRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
@Api(tags = "预警记录管理API")
@RequiredArgsConstructor
@Validated
public class AlertRecordController {

    private final AlertRecordService alertRecordService;


    @ApiOperation("新增预警记录")
    @PostMapping
    public Result<AlertRecordVO> addAlertRecord(@Valid @RequestBody AlertRecordDTO dto) {
        AlertRecordVO vo = alertRecordService.addAlertRecord(dto);
        return Result.ok(ALERT_RECORD_ADD_SUCCESS_MESSAGE, vo);
    }


    @ApiOperation("删除预警记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAlertRecord(
            @ApiParam("预警记录ID")
            @PathVariable
            @NotNull(message = "预警记录ID不能为空")
            @Positive(message = "预警记录ID必须为正整数") Long id) {
        alertRecordService.deleteAlertRecord(id);
        return Result.ok(ALERT_RECORD_DELETE_SUCCESS_MESSAGE);
    }


    @ApiOperation("修改预警记录")
    @PutMapping("/{id}")
    public Result<AlertRecordVO> updateAlertRecord(
            @ApiParam("预警记录ID")
            @PathVariable
            @NotNull(message = "预警记录ID不能为空")
            @Positive(message = "预警记录ID必须为正整数") Long id,
            @Valid @RequestBody AlertRecordDTO dto) {
        AlertRecordVO vo = alertRecordService.updateAlertRecord(id, dto);
        return Result.ok(ALERT_RECORD_UPDATE_SUCCESS_MESSAGE, vo);
    }


    @ApiOperation("根据ID查询预警记录")
    @GetMapping("/{id}")
    public Result<AlertRecordVO> getAlertRecordById(
            @ApiParam("预警记录ID")
            @PathVariable
            @NotNull(message = "预警记录ID不能为空")
            @Positive(message = "预警记录ID必须为正整数") Long id) {
        AlertRecordVO vo = alertRecordService.getAlertRecordById(id);
        return Result.ok(ALERT_RECORD_GET_SUCCESS_MESSAGE, vo);
    }


    @ApiOperation("根据工人ID查询预警记录")
    @GetMapping("/worker/{workerId}")
    public Result<List<AlertRecordVO>> getAlertRecordsByWorkerId(
            @ApiParam("工人ID")
            @PathVariable
            @NotNull(message = "工人ID不能为空")
            @Positive(message = "工人ID必须为正整数") Long workerId) {
        List<AlertRecordVO> voList = alertRecordService.getAlertRecordsByWorkerId(workerId);
        return Result.ok(ALERT_RECORD_GET_SUCCESS_MESSAGE, voList);
    }


    @ApiOperation("根据预警级别查询")
    @GetMapping("/level/{alertLevel}")
    public Result<List<AlertRecordVO>> getAlertRecordsByAlertLevel(
            @ApiParam("预警级别（警告/严重）")
            @PathVariable
            @NotNull(message = "预警级别不能为空") AlertLevel alertLevel) {
        List<AlertRecordVO> voList = alertRecordService.getAlertRecordsByAlertLevel(alertLevel);
        return Result.ok(ALERT_RECORD_GET_SUCCESS_MESSAGE, voList);
    }


    @ApiOperation("根据预警类型模糊查询")
    @GetMapping("/type")
    public Result<List<AlertRecordVO>> getAlertRecordsByAlertTypeLike(
            @ApiParam("预警类型关键词")
            @RequestParam
            @NotBlank(message = "预警类型关键词不能为空") String alertType) {
        List<AlertRecordVO> voList = alertRecordService.getAlertRecordsByAlertTypeLike(alertType);
        return Result.ok(ALERT_RECORD_GET_SUCCESS_MESSAGE, voList);
    }


    @ApiOperation("标记预警记录为已处理")
    @PutMapping("/{id}/handle")
    public Result<Void> markAlertRecordAsHandled(
            // 路径参数校验
            @ApiParam("预警记录ID")
            @PathVariable
            @NotNull(message = "预警记录ID不能为空")
            @Positive(message = "预警记录ID必须为正整数") Long id,
            // 请求参数校验
            @ApiParam("处理人")
            @RequestParam
            @NotBlank(message = "处理人不能为空")
            @Length(max = 20, message = "处理人长度不能超过20个字符") String handledBy) {
        alertRecordService.markAlertRecordAsHandled(id, handledBy);
        return Result.ok(ALERT_RECORD_MARK_HANDLED_SUCCESS_MESSAGE);
    }
}
