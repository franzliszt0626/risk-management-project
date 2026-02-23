package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.service.VideoAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.WORKER_CODE;
import static gang.lu.riskmanagementproject.common.global.GlobalAllowTypeConstants.VIDEO;
import static gang.lu.riskmanagementproject.message.FailedMessages.WORKER_CODE_EMPTY;
import static gang.lu.riskmanagementproject.message.SuccessMessages.VIDEO_ANALYZE_SUCCESS;

/**
 * 视频分析接口
 *
 * @author Franz Liszt
 * @since 2026-02-22
 */
@Api(tags = "视频分析")
@Validated
@RestController
@RequestMapping("/api/video-analysis")
@RequiredArgsConstructor
public class VideoAnalysisController {

    private final VideoAnalysisService videoAnalysisService;

    // ======================== 个性化业务接口 ========================

    @ApiOperation(
            value = "上传视频进行风险分析",
            notes = "接收视频文件（≤ 50 MB，支持 mp4 / avi / mov），转发算法服务完成检测，" +
                    "将分析结果写入风险指标表并返回"
    )
    @PostMapping(value = "/analyze/{workerCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<RiskIndicatorVO> analyzeVideo(
            @ApiParam(WORKER_CODE)
            @PathVariable
            @NotBlank(message = WORKER_CODE_EMPTY) String workerCode,
            @ApiParam(value = "待分析的视频文件（mp4 / avi / mov，≤ 50 MB）", required = true)
            @RequestPart(VIDEO) MultipartFile video) {
        RiskIndicatorVO vo = videoAnalysisService.analyzeAndSave(workerCode, video);
        return Result.ok(VIDEO_ANALYZE_SUCCESS, vo);
    }
}