package gang.lu.riskmanagementproject.service;

import gang.lu.riskmanagementproject.domain.vo.normal.RiskPredictionVO;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 15:24
 * @description 使用ai预测风险等级
 */
public interface RiskAiService {


    /**
     * 根据工人历史风险记录，调用AI预测未来风险并给出建议
     *
     * @param workerId 具体工人id
     * @param limit 历史记录条数
     * @return 预测信息
     */
    RiskPredictionVO predictRisk(Long workerId, int limit);
}
