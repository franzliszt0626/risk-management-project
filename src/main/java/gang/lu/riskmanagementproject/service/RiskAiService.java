package gang.lu.riskmanagementproject.service;

import gang.lu.riskmanagementproject.domain.vo.normal.FatiguePredictionVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskPredictionVO;

/**
 * AI 风险分析服务接口
 * <p>
 * 包含两类预测能力：
 * <ul>
 *   <li>基于 Qwen 大模型的综合风险等级预测（{@link #predictRisk}）；</li>
 *   <li>基于内嵌 LSTM 模型的未来 6 次疲劳百分比预测（{@link #predictFatigue}）。</li>
 * </ul>
 *
 * @author Franz Liszt
 * @since 2026-02-22
 */
public interface RiskAiService {


    /**
     * 根据工人历史风险记录，调用AI预测未来风险并给出建议
     *
     * @param workerId 具体工人id
     * @param limit    历史记录条数
     * @return 预测信息
     */
    RiskPredictionVO predictRisk(Long workerId, int limit);

    /**
     * 基于工人历史生理指标，使用内嵌 LSTM 模型预测未来 6 次疲劳百分比。
     *
     * @param workerId 工人 ID
     * @param limit    参与训练的最近历史记录条数5-200之间
     * @return LSTM 疲劳预测结果 VO，包含预测序列、趋势描述与风险提示
     */
    FatiguePredictionVO predictFatigue(Long workerId, Integer limit);
}
