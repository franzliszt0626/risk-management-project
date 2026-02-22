package gang.lu.riskmanagementproject.controller;

import gang.lu.riskmanagementproject.common.Result;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.service.VideoAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static gang.lu.riskmanagementproject.common.BusinessConstants.WORKER_ID;
import static gang.lu.riskmanagementproject.message.SuccessMessages.VIDEO_ANALYZE_SUCCESS;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 14:38
 * @description
 */
@Api(tags = "视频分析 - BigSmall视频检测")
@RestController
@RequestMapping("/video-analysis")
@RequiredArgsConstructor
public class VideoAnalysisController {

    private final VideoAnalysisService videoAnalysisService;


    @ApiOperation(
            value = "上传视频进行风险分析",
            notes = "接收前端上传的视频（≤50MB，≤30s），转发给BigSmall医疗检测算法，" +
                    "将分析结果写入风险指标表并返回结果。支持 mp4/avi/mov 格式。"
    )
    @PostMapping(value = "/analyze/{workerId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<RiskIndicatorVO> analyzeVideo(
            @ApiParam(value = WORKER_ID, required = true, example = "1")
            @PathVariable Long workerId,
            @ApiParam(value = "待分析的视频文件 (mp4/avi/mov, ≤50MB)", required = true)
            @RequestPart("video") MultipartFile video) {
        RiskIndicatorVO vo = videoAnalysisService.analyzeAndSave(workerId, video);
        return Result.ok(VIDEO_ANALYZE_SUCCESS, vo);
    }
}
