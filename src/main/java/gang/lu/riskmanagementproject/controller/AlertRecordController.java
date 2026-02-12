package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.dto.query.AlertRecordQueryDTO;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.service.AlertRecordService;
import gang.lu.riskmanagementproject.util.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
        return Result.ok(ALERT_RECORD_ADD_SUCCESS, vo);
    }


    @ApiOperation("删除预警记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAlertRecord(
            @ApiParam("预警记录ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.ALERT_RECORD_ID) Long id) {
        alertRecordService.deleteAlertRecord(id);
        return Result.ok(ALERT_RECORD_DELETE_SUCCESS);
    }


    @ApiOperation("修改预警记录")
    @PutMapping("/{id}")
    public Result<AlertRecordVO> updateAlertRecord(
            @ApiParam("预警记录ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.ALERT_RECORD_ID) Long id,
            @Valid @RequestBody AlertRecordDTO dto) {
        AlertRecordVO vo = alertRecordService.updateAlertRecord(id, dto);
        return Result.ok(ALERT_RECORD_UPDATE_SUCCESS, vo);
    }


    @ApiOperation("根据ID查询预警记录")
    @GetMapping("/{id}")
    public Result<AlertRecordVO> getAlertRecordById(
            @ApiParam("预警记录ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.ALERT_RECORD_ID) Long id) {
        AlertRecordVO vo = alertRecordService.getAlertRecordById(id);
        return Result.ok(ALERT_RECORD_GET_SUCCESS, vo);
    }


    @PostMapping("/search")
    @ApiOperation("多条件组合分页查询预警记录（支持工人ID/预警类型/级别/处理状态等筛选）")
    @ApiImplicitParam(name = "queryDTO", value = "预警记录查询条件（含分页）", required = true, dataType = "AlertRecordQueryDTO", paramType = "body")
    public Result<PageVO<AlertRecordVO>> searchAlertRecords(@Valid @RequestBody AlertRecordQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<AlertRecordVO> pageVO = alertRecordService.searchAlertRecords(queryDTO);
        return Result.ok(ALERT_RECORD_GET_SUCCESS, pageVO);
    }


    @ApiOperation("标记预警记录为已处理")
    @PutMapping("/{id}/handle")
    public Result<Void> markAlertRecordAsHandled(
            // 路径参数校验
            @ApiParam("预警记录ID")
            @ValidId(bizName = BusinessConstants.ALERT_RECORD_ID) Long id,
            // 请求参数校验
            @ApiParam("处理人")
            @RequestParam
            @NotBlank(message = "处理人不能为空") String handledBy) {
        alertRecordService.markAlertRecordAsHandled(id, handledBy);
        return Result.ok(ALERT_RECORD_MARK_HANDLED_SUCCESS);
    }
}
