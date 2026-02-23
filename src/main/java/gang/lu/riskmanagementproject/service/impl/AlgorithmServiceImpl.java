package gang.lu.riskmanagementproject.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.domain.dto.AlgorithmResultDTO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.property.AlgorithmProperty;
import gang.lu.riskmanagementproject.service.AlgorithmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static gang.lu.riskmanagementproject.common.global.GlobalAllowTypeConstants.VIDEO;
import static gang.lu.riskmanagementproject.common.global.GlobalAllowTypeConstants.VIDEO_MP_4;
import static gang.lu.riskmanagementproject.common.global.GlobalBusinessConstants.*;
import static gang.lu.riskmanagementproject.common.http.HttpConstants.ANALYZE;
import static gang.lu.riskmanagementproject.message.FailedMessages.ALGORITHM_ERROR;
import static gang.lu.riskmanagementproject.message.FailedMessages.ALGORITHM_UNREACHABLE;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 14:22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmServiceImpl implements AlgorithmService {

    private final OkHttpClient okHttpClient;
    private final AlgorithmProperty algorithmProperty;
    private final ObjectMapper objectMapper;

    /**
     * 将视频发送至 Python 算法服务，解析并返回风险指标。
     * <p>
     * {@code recordResult = true}：AOP 会自动打印算法原始返回的结构化结果，
     * 方便排查心率/呼吸率等字段是否正确解析。
     */
    @Override
    @BusinessLog(
            value = VIDEO_ANALYZING,
            recordParams = true,
            recordResult = true,
            logLevel = BusinessLog.LogLevel.INFO
    )
    public AlgorithmResultDTO analyzeVideo(MultipartFile video) {
        try {
            RequestBody fileBody = RequestBody.create(
                    video.getBytes(),
                    MediaType.parse(video.getContentType() != null
                            ? video.getContentType()
                            : VIDEO_MP_4)
            );

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(VIDEO, video.getOriginalFilename(), fileBody)
                    .build();

            Request request = new Request.Builder()
                    .url(algorithmProperty.getBASE_URL() + ANALYZE)
                    .post(requestBody)
                    .build();

            log.info("[Algorithm] 发送视频 [{}]（{} bytes）至算法服务",
                    video.getOriginalFilename(), video.getSize());

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    log.error("[Algorithm] 算法服务返回非 2xx: {}", response.code());
                    throw new BizException(HttpStatus.BAD_GATEWAY, ALGORITHM_ERROR);
                }

                String json = response.body().string();
                // 打印算法服务原始 JSON 响应
                log.info("[Algorithm] 原始响应 ↓\n{}", json);

                return objectMapper.readValue(json, AlgorithmResultDTO.class);
            }

        } catch (IOException e) {
            log.error("[Algorithm] 连接算法服务失败", e);
            throw new BizException(HttpStatus.SERVICE_UNAVAILABLE, ALGORITHM_UNREACHABLE);
        }
    }
}