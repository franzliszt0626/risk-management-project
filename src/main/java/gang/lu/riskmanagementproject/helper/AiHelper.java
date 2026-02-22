package gang.lu.riskmanagementproject.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gang.lu.riskmanagementproject.common.ai.AiConstants;
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
public class AiHelper implements AiConstants {

    private final OkHttpClient okHttpClient;
    private final DashScopeProperty dashScopeProperty;
    private final ObjectMapper objectMapper;


    /**
     * 构建提示词
     */
    public String buildPrompt(Long workerId, List<RiskIndicatorVO> history) {
        StringBuilder sb = new StringBuilder();
        sb.append(PROMPT_HEADER);
        sb.append("以下是工人ID=").append(workerId)
                .append(" 的历史生理风险监测数据（按时间排序，最新在后）：\n\n");
        sb.append(PROMPT_TABLE_HEADER);
        sb.append(PROMPT_TABLE_DIVIDER);

        for (int i = 0; i < history.size(); i++) {
            RiskIndicatorVO r = history.get(i);
            sb.append(String.format(PROMPT_ROW_FORMAT,
                    i + 1,
                    r.getCreateTime() != null ? r.getCreateTime().toString() : PROMPT_UNKNOWN,
                    r.getHeartRate() != null ? r.getHeartRate() : 0,
                    r.getRespiratoryRate() != null ? r.getRespiratoryRate() : 0,
                    r.getFatiguePercent() != null ? r.getFatiguePercent() : 0.0,
                    r.getRiskLevel() != null ? r.getRiskLevel().getValue() : PROMPT_UNKNOWN,
                    Boolean.TRUE.equals(r.getAlertFlag()) ? PROMPT_ALERT_YES : PROMPT_ALERT_NO
            ));
        }
        sb.append("\n请根据以上数据：\n");
        sb.append("1. 预测该工人未来的风险趋势\n");
        sb.append("2. 给出具体的健康与安全建议\n\n");
        sb.append("请严格按以下JSON格式返回（不要有任何额外文字）：\n");
        sb.append("{\n");
        sb.append("  \"").append(AI_FIELD_PREDICTED_RISK).append("\": \"低风险|中风险|高风险|严重风险\",\n");
        sb.append("  \"").append(AI_FIELD_RISK_TREND).append("\": \"上升|平稳|下降\",\n");
        sb.append("  \"").append(AI_FIELD_ANALYSIS).append("\": \"一段简洁的综合分析（100字以内）\",\n");
        sb.append("  \"").append(AI_FIELD_SUGGESTIONS).append("\": [\"建议1\", \"建议2\", \"建议3\"],\n");
        sb.append("  \"").append(AI_FIELD_CONFIDENCE).append("\": \"置信度说明\"\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 调用千问
     */
    public String callQwen(String userPrompt) {
        try {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put(MODEL, dashScopeProperty.getModel());
            requestMap.put(MAX_TOKENS, dashScopeProperty.getMaxTokens());
            requestMap.put(MESSAGES, new Object[]{
                    new HashMap<String, Object>() {{
                        put(ROLE, USER);
                        put(CONTENT, userPrompt);
                    }}
            });

            String requestJson = objectMapper.writeValueAsString(requestMap);

            Request request = new Request.Builder()
                    .url(dashScopeProperty.getBaseUrl() + PATH_CHAT_COMPLETIONS)
                    .post(RequestBody.create(requestJson, MediaType.parse("application/json")))
                    .addHeader("Authorization", "Bearer " + dashScopeProperty.getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .build();

            log.info("[AiHelper] 调用 Qwen 模型: {}", dashScopeProperty.getModel());

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    log.error("[AiHelper] Qwen API 返回非 2xx: {}", response.code());
                    throw new BizException(HttpStatus.BAD_GATEWAY, AI_MODEL_INVALID);
                }
                String body = response.body().string();
                // 打印 AI 原始返回，方便排查
                log.info("[AiHelper] Qwen 原始响应 ↓\n{}", body);
                // OpenAI 格式: choices[0].message.content
                JsonNode root = objectMapper.readTree(body);
                String content = root.path(CHOICES).get(0)
                        .path(MESSAGE).path(CONTENT).asText();
                log.debug("[AiHelper] 提取 content ↓\n{}", content);
                return content;
            }
        } catch (IOException e) {
            log.error("[AiHelper] Qwen API 连接异常", e);
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
            log.error("[AiHelper] AI 响应解析失败，原始内容: {}", aiText, e);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, AI_RESPONSE_RESOLVE_FAILURE);
        }
    }
}