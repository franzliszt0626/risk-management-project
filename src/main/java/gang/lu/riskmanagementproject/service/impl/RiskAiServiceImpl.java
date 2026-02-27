package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.normal.FatiguePredictionVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskPredictionVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.helper.AiHelper;
import gang.lu.riskmanagementproject.helper.FatiguePredictHelper;
import gang.lu.riskmanagementproject.mapper.RiskIndicatorMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.property.LstmProperty;
import gang.lu.riskmanagementproject.property.RecordLimitProperty;
import gang.lu.riskmanagementproject.property.WarnProperty;
import gang.lu.riskmanagementproject.service.RiskAiService;
import gang.lu.riskmanagementproject.converter.RiskIndicatorConverter;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.ai.LstmWarningConstants.*;
import static gang.lu.riskmanagementproject.common.field.FieldChineseConstants.*;
import static gang.lu.riskmanagementproject.common.global.GlobalBusinessConstants.*;
import static gang.lu.riskmanagementproject.common.global.GlobalLogConstants.LOG_AI_READY_TO_SEND;
import static gang.lu.riskmanagementproject.common.global.GlobalLogConstants.LOG_LSTM_ANALYSING;
import static gang.lu.riskmanagementproject.common.global.GlobalSimbolConstants.LIMIT;
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
    private final LstmProperty lstmProperty;
    private final FatiguePredictHelper fatiguePredictHelper;
    private final WarnProperty warnProperty;

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

        log.info(LOG_AI_READY_TO_SEND, workerId, history.size());

        // 4. 构建 Prompt → 调用 AI → 解析响应
        String prompt = aiHelper.buildPrompt(workerId, history);
        String aiResponse = aiHelper.callQwen(prompt);
        return aiHelper.parseAiResponse(workerId, history.size(), aiResponse);
    }

    /**
     * 基于工人历史生理指标，使用内嵌 LSTM 模型预测未来 6 次疲劳百分比。
     *
     * @param workerId 工人 ID
     * @param limit    参与训练的最近历史记录条数5-200之间
     * @return LSTM 疲劳预测结果 VO，包含预测序列、趋势描述与风险提示
     */
    @Override
    @BusinessLog(
            value = LSTM_FATIGUE_PREDICTION,
            recordParams = true,
            recordResult = true,
            logLevel = BusinessLog.LogLevel.INFO
    )
    public FatiguePredictionVO predictFatigue(Long workerId, Integer limit) {
        // 1. 校验工人存在
        generalValidator.validateIdExist(workerId, workerMapper, WORKER_NOT_EXIST);
        // 2. 校验历史记录条数范围
        if (limit < lstmProperty.getMinHistory()) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(LSTM_HISTORY_TOO_FEW, lstmProperty.getMinHistory()));
        }
        if (limit > lstmProperty.getMaxHistory()) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(LSTM_HISTORY_TOO_MANY, lstmProperty.getMaxHistory()));
        }

        // 3. 查询历史风险指标（时间正序，便于 LSTM 学习时序关系）
        List<RiskIndicator> poList = queryHistoryPO(workerId, limit);

        // 4. 提取疲劳百分比序列（过滤 null 值）
        List<Double> fatigueHistory = poList.stream()
                .filter(p -> p.getFatiguePercent() != null)
                .map(p -> p.getFatiguePercent().doubleValue())
                .collect(Collectors.toList());

        if (fatigueHistory.size() < lstmProperty.getMinHistory()) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(LSTM_HISTORY_TOO_FEW, lstmProperty.getMinHistory()));
        }

        log.info(LOG_LSTM_ANALYSING, workerId, fatigueHistory.size());

        // 5. 调用 LSTM 预测器（即时训练 + 滚动推理）
        List<Double> predicted = fatiguePredictHelper.predict(fatigueHistory);

        // 6. 生成趋势描述与风险提示，组装 VO
        return FatiguePredictionVO.builder()
                .workerId(workerId)
                .historyCount(fatigueHistory.size())
                .predictedFatigueList(predicted)
                .trend(buildTrend(predicted))
                .riskTip(buildRiskTip(predicted))
                .build();
    }

    // ======================== 内部方法 ========================

    /**
     * 查询历史风险指标 PO（取最新 limit 条，正序排列）。
     */
    private List<RiskIndicator> queryHistoryPO(Long workerId, Integer limit) {
        List<RiskIndicator> poList = riskIndicatorMapper.selectList(
                new LambdaQueryWrapper<RiskIndicator>()
                        .eq(RiskIndicator::getWorkerId, workerId)
                        .orderByDesc(RiskIndicator::getCreateTime)
                        .last(LIMIT + limit)
        );
        Collections.reverse(poList);
        return poList;
    }

    /**
     * 查询历史风险指标 VO（取最新 limit 条，正序排列）。
     */
    private List<RiskIndicatorVO> queryHistoryVO(Long workerId, int limit) {
        return queryHistoryPO(workerId, limit).stream()
                .map(riskIndicatorConverter::poToVo)
                .collect(Collectors.toList());
    }

    /**
     * 根据预测序列首尾差值判断整体疲劳趋势。
     * <p>
     * 差值 > 3% → 上升；< -3% → 下降；其余 → 平稳。
     */
    private String buildTrend(List<Double> predicted) {
        if (predicted.size() < warnProperty.getSteady()) {
            return LSTM_STEADY;
        }
        double delta = predicted.get(predicted.size() - 1) - predicted.get(0);
        if (delta > warnProperty.getUpward()) {
            return LSTM_UPWARD;
        }
        if (delta < warnProperty.getDownward()) {
            return LSTM_DOWNWARD;
        }
        return LSTM_STEADY;
    }

    /**
     * 根据预测序列中的疲劳峰值生成分级风险提示文案。
     */
    private String buildRiskTip(List<Double> predicted) {
        double peak = predicted.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);

        if (peak >= warnProperty.getHighThreshold()) {
            return String.format(FATIGUE_OVER_HIGH_THRESHOLD
                    ,
                    peak, warnProperty.getHighThreshold());
        }
        if (peak >= warnProperty.getWarnThreshold()) {
            return String.format(FATIGUE_OVER_WARN_THRESHOLD
                    ,
                    peak, warnProperty.getWarnThreshold());
        }
        return String.format(
                FATIGUE_NORMAL, peak);
    }
}