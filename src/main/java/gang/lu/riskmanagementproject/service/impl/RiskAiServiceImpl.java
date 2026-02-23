package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskPredictionVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.helper.AiHelper;
import gang.lu.riskmanagementproject.mapper.RiskIndicatorMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.property.RecordLimitProperty;
import gang.lu.riskmanagementproject.service.RiskAiService;
import gang.lu.riskmanagementproject.converter.RiskIndicatorConverter;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.global.GlobalBusinessConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RiskAiServiceImpl implements RiskAiService {

    private final AiHelper aiHelper;
    private final RiskIndicatorMapper riskIndicatorMapper;
    private final RiskIndicatorConverter riskIndicatorConverter;
    private final WorkerMapper workerMapper;
    private final GeneralValidator generalValidator;
    private final RecordLimitProperty recordLimitProperty;

    /**
     * 查询历史数据，调用 Qwen 模型，返回风险预测。
     * <p>
     * {@code recordResult = true}：AOP 会自动打印 {@link RiskPredictionVO}，
     * 包含 predictedRiskLevel / riskTrend / analysisSummary 等字段。
     * AI 原始 JSON 响应由 {@link AiHelper#callQwen} 内部通过 {@code log.info} 打印。
     */
    @Override
    @BusinessLog(
            value = AI_RISK_PREDICTION,
            recordParams = true,
            recordResult = true,
            logLevel = BusinessLog.LogLevel.INFO
    )
    public RiskPredictionVO predictRisk(Long workerId, int limit) {

        // 1. 校验工人存在
        generalValidator.validateIdExist(workerId, workerMapper, WORKER_NOT_EXIST);

        // 2. 校验 limit 范围
        if (limit < recordLimitProperty.getMIN_RECORDS() || limit > recordLimitProperty.getMAX_RECORDS()) {
            throw new BizException(HttpStatus.BAD_REQUEST, LIMIT_INVALID);
        }

        // 3. 查询历史记录（取最新 N 条，倒序后再正序给 AI）
        List<RiskIndicator> poList = riskIndicatorMapper.selectList(
                new LambdaQueryWrapper<RiskIndicator>()
                        .eq(RiskIndicator::getWorkerId, workerId)
                        .orderByDesc(RiskIndicator::getCreateTime)
                        .last(LIMIT + limit)
        );
        java.util.Collections.reverse(poList);

        List<RiskIndicatorVO> history = poList.stream()
                .map(riskIndicatorConverter::poToVo)
                .collect(Collectors.toList());

        if (ObjectUtil.isNull(history) || history.isEmpty()) {
            throw new BizException(HttpStatus.BAD_REQUEST, RISK_INDICATOR_EMPTY);
        }

        log.info("[RiskAi] workerId={} 共 {} 条历史数据，准备发送给 AI", workerId, history.size());

        // 4. 构建 Prompt → 调用 AI → 解析响应
        String prompt = aiHelper.buildPrompt(workerId, history);
        String aiResponse = aiHelper.callQwen(prompt);
        return aiHelper.parseAiResponse(workerId, history.size(), aiResponse);
    }
}