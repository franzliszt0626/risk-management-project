package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.annotation.ValidId;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.dto.query.AlertRecordQueryDTO;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.service.AlertRecordService;
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

import static gang.lu.riskmanagementproject.common.FailedMessages.ALERT_LEVEL_HANDLER_EMPTY;
import static gang.lu.riskmanagementproject.common.FailedMessages.ALERT_LEVEL_ID_LIST_EMPTY;
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
@Api(tags = "预警记录管理 API")
@RequiredArgsConstructor
@Validated
public class AlertRecordController {

    private final AlertRecordService alertRecordService;

    // ======================== 通用CRUD接口 ========================

    @ApiOperation("新增预警记录")
    @PostMapping
    public Result<AlertRecordVO> addAlertRecord(
            @ApiParam("预警记录新增数据")
            @Valid @RequestBody AlertRecordDTO dto) {
        AlertRecordVO vo = alertRecordService.add(dto);
        return Result.ok(ALERT_RECORD_ADD_SUCCESS, vo);
    }

    @ApiOperation("删除预警记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAlertRecord(
            @ApiParam("预警记录ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.ALERT_RECORD_ID) Long id) {
        alertRecordService.delete(id);
        return Result.ok(ALERT_RECORD_DELETE_SUCCESS);
    }

    @ApiOperation("批量删除预警记录")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteAlertRecord(
            @ApiParam("预警记录ID列表")
            @RequestBody
            @NotEmpty(message = ALERT_LEVEL_ID_LIST_EMPTY) List<Long> ids) {
        alertRecordService.batchDelete(ids);
        return Result.ok(ALERT_RECORD_BATCH_DELETE_SUCCESS);
    }

    @ApiOperation("修改预警记录")
    @PutMapping("/{id}")
    public Result<AlertRecordVO> updateAlertRecord(
            @ApiParam("预警记录ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.ALERT_RECORD_ID) Long id,
            @ApiParam("预警记录修改数据")
            @Valid @RequestBody AlertRecordDTO dto) {
        AlertRecordVO vo = alertRecordService.update(id, dto);
        return Result.ok(ALERT_RECORD_UPDATE_SUCCESS, vo);
    }

    @ApiOperation("根据ID查询预警记录")
    @GetMapping("/{id}")
    public Result<AlertRecordVO> getAlertRecordById(
            @ApiParam("预警记录ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.ALERT_RECORD_ID) Long id) {
        AlertRecordVO vo = alertRecordService.getOneById(id);
        return Result.ok(ALERT_RECORD_GET_SUCCESS, vo);
    }

    @ApiOperation("多条件组合分页查询预警记录")
    @ApiImplicitParam(name = "queryDTO", value = "预警记录查询条件（含分页）", required = true, dataType = "AlertRecordQueryDTO", paramType = "body")
    @PostMapping("/search")
    public Result<PageVO<AlertRecordVO>> searchAlertRecords(
            @Valid @RequestBody AlertRecordQueryDTO queryDTO) {
        PageHelper.bindGlobalDefaultRule(queryDTO);
        PageVO<AlertRecordVO> pageVO = alertRecordService.search(queryDTO);
        return Result.ok(String.format(ALERT_RECORD_GET_COUNT_SUCCESS, pageVO.getTotal()), pageVO);
    }

    // ======================== 个性化业务接口 ========================

    @ApiOperation("标记预警记录为已处理")
    @PutMapping("/{id}/handle")
    public Result<Void> markAlertRecordAsHandled(
            @ApiParam("预警记录ID")
            @PathVariable
            @ValidId(bizName = BusinessConstants.ALERT_RECORD_ID) Long id,
            @ApiParam("处理人")
            @RequestParam
            @NotBlank(message = ALERT_LEVEL_HANDLER_EMPTY) String handledBy) {
        alertRecordService.markAlertRecordAsHandled(id, handledBy);
        return Result.ok(ALERT_RECORD_MARK_HANDLED_SUCCESS);
    }
}