package gang.lu.riskmanagementproject.helper;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskPredictionVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.property.DashScopeProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.ai.AiFieldConstants.*;
import static gang.lu.riskmanagementproject.common.ai.AiLogConstants.*;
import static gang.lu.riskmanagementproject.common.ai.AiPromptConstants.*;
import static gang.lu.riskmanagementproject.common.global.GlobalSimbolConstants.AI_TEXT_JSON_PATTERN;
import static gang.lu.riskmanagementproject.common.global.GlobalSimbolConstants.AI_TEXT_TICK_PATTERN;
import static gang.lu.riskmanagementproject.common.http.HttpConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * AI 调用助手：负责 Prompt 构建、Qwen 接口调用、响应解析。
 *
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 15:31
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AiHelper {

    private final OkHttpClient okHttpClient;
    private final DashScopeProperty dashScopeProperty;
    private final ObjectMapper objectMapper;

    /**
     * 构建优化后的提示词（增强约束性、规范性）
     */
    public String buildPrompt(Long workerId, List<RiskIndicatorVO> history) {
        StringBuilder sb = new StringBuilder();
        // 1. 基础角色定位
        sb.append(PROMPT_HEADER);
        // 2. 数据描述
        sb.append(PROMPT_DATA_DESC_PREFIX)
                .append(workerId)
                .append(PROMPT_DATA_DESC_SUFFIX);
        // 3. 表格头
        sb.append(PROMPT_TABLE_HEADER);
        sb.append(PROMPT_TABLE_DIVIDER);

        // 4. 填充历史数据
        for (int i = 0; i < history.size(); i++) {
            RiskIndicatorVO r = history.get(i);
            sb.append(String.format(PROMPT_ROW_FORMAT,
                    i + 1,
                    ObjectUtil.isNotNull(r.getCreateTime()) ? r.getCreateTime().toString() : PROMPT_UNKNOWN,
                    ObjectUtil.isNotNull(r.getHeartRate()) ? r.getHeartRate() : 0,
                    ObjectUtil.isNotNull(r.getRespiratoryRate()) ? r.getRespiratoryRate() : 0,
                    ObjectUtil.isNotNull(r.getFatiguePercent()) ? r.getFatiguePercent() : 0.0,
                    ObjectUtil.isNotNull(r.getRiskLevel()) ? r.getRiskLevel().getValue() : PROMPT_UNKNOWN,
                    Boolean.TRUE.equals(r.getAlertFlag()) ? PROMPT_ALERT_YES : PROMPT_ALERT_NO
            ));
        }

        // 5. 任务要求（明确时间范围、建议数量）
        sb.append(PROMPT_TASK_REQUIRE);
        sb.append(PROMPT_TASK_1);
        sb.append(PROMPT_TASK_2);

        // 6. 格式要求（增强约束）
        sb.append(PROMPT_FORMAT_REQUIRE);
        sb.append("{\n");
        sb.append("  \"").append(AI_FIELD_PREDICTED_RISK).append("\": ").append(PROMPT_JSON_FIELD_PREDICTED_RISK_DESC).append(",\n");
        sb.append("  \"").append(AI_FIELD_RISK_TREND).append("\": ").append(PROMPT_JSON_FIELD_TREND_DESC).append(",\n");
        sb.append("  \"").append(AI_FIELD_ANALYSIS).append("\": ").append(PROMPT_JSON_FIELD_ANALYSIS_DESC).append(",\n");
        sb.append("  \"").append(AI_FIELD_SUGGESTIONS).append("\": ").append(PROMPT_JSON_FIELD_SUGGESTIONS_DESC).append(",\n");
        sb.append("  \"").append(AI_FIELD_CONFIDENCE).append("\": ").append(PROMPT_JSON_FIELD_CONFIDENCE_DESC).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 调用千问（替换硬编码常量，优化HashMap初始化）
     */
    public String callQwen(String userPrompt) {
        try {
            int initialRequestSize = 3;
            Map<String, Object> requestMap = new HashMap<>(initialRequestSize);
            requestMap.put(MODEL, dashScopeProperty.getModel());
            requestMap.put(MAX_TOKENS, dashScopeProperty.getMaxTokens());
            int initialMessageSize = 2;
            requestMap.put(MESSAGES, new Object[]{
                    new HashMap<String, Object>(initialMessageSize) {{
                        put(ROLE, USER);
                        put(CONTENT, userPrompt);
                    }}
            });

            String requestJson = objectMapper.writeValueAsString(requestMap);

            Request request = new Request.Builder()
                    .url(dashScopeProperty.getBaseUrl() + PATH_CHAT_COMPLETIONS)
                    .post(RequestBody.create(requestJson, MediaType.parse(MEDIA_TYPE_JSON)))
                    .addHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER_PREFIX + dashScopeProperty.getApiKey())
                    .addHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_JSON)
                    .build();

            log.info(LOG_CALL_QWEN_MODEL, dashScopeProperty.getModel());

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    log.error(LOG_QWEN_NON_2XX, response.code());
                    throw new BizException(HttpStatus.BAD_GATEWAY, AI_MODEL_INVALID);
                }
                String body = response.body().string();
                // 打印 AI 原始返回，方便排查
                log.info(LOG_QWEN_RAW_RESPONSE, body);
                // OpenAI 格式: choices[0].message.content
                JsonNode root = objectMapper.readTree(body);
                String content = root.path(CHOICES).get(0)
                        .path(MESSAGE).path(CONTENT).asText();
                log.debug(LOG_EXTRACT_CONTENT, content);
                return content;
            }
        } catch (IOException e) {
            log.error(LOG_QWEN_CONNECTION_EXCEPTION, e);
            throw new BizException(HttpStatus.SERVICE_UNAVAILABLE, AI_MODEL_CONNECTION_FAIL);
        }
    }

    /**
     * 解析响应
     */
    public RiskPredictionVO parseAiResponse(Long workerId, int recordCount, String aiText) {
        try {
            String json = aiText.trim();
            // 去除 Markdown 代码块标记（如 ```json ... ```）
            if (json.contains(AI_TEXT_TICK_PATTERN)) {
                json = json.replace(AI_TEXT_JSON_PATTERN, "")
                        .replace(AI_TEXT_TICK_PATTERN, "")
                        .trim();
            }
            JsonNode node = objectMapper.readTree(json);
            RiskPredictionVO vo = new RiskPredictionVO();
            vo.setWorkerId(workerId);
            vo.setRecordCount(recordCount);
            vo.setPredictedRiskLevel(node.path(AI_FIELD_PREDICTED_RISK).asText(AI_DEFAULT_RISK));
            vo.setRiskTrend(node.path(AI_FIELD_RISK_TREND).asText(AI_DEFAULT_TREND));
            vo.setAnalysisSummary(node.path(AI_FIELD_ANALYSIS).asText(""));
            vo.setConfidenceNote(node.path(AI_FIELD_CONFIDENCE).asText(""));

            List<String> suggestions = new ArrayList<>();
            JsonNode sugNode = node.path(AI_FIELD_SUGGESTIONS);
            if (sugNode.isArray()) {
                sugNode.forEach(s -> suggestions.add(s.asText()));
            }
            vo.setSuggestions(suggestions);
            return vo;
        } catch (Exception e) {
            log.error(LOG_PARSE_AI_RESPONSE_FAILED, aiText, e);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, AI_RESPONSE_RESOLVE_FAILURE);
        }
    }
}