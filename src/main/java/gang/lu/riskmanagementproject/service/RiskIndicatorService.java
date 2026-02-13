package gang.lu.riskmanagementproject.service;

import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.dto.query.RiskIndicatorQueryDTO;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskLevelCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskTimePeriodCountVO;

import java.time.LocalDate;

/**
 * <p>
 * 实时风险指标表 服务类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
public interface RiskIndicatorService extends BaseCrudService<RiskIndicator, RiskIndicatorDTO, RiskIndicatorVO, RiskIndicatorQueryDTO> {

    /**
     * 由工人id查询对应工人最新一次的风险指标信息
     *
     * @param workerId 前端传来的工人id
     * @return RiskIndicatorVO
     */
    RiskIndicatorVO getLatestRiskIndicatorByWorkerId(Long workerId);


    /**
     * 统计去重工人的风险等级人数分布
     *
     * @return 各种风险的人数信息集合
     */
    RiskLevelCountVO countDistinctWorkerByRiskLevel();

    /**
     * 统计当日各4小时时间段高风险工人数
     *
     * @param statDate 统计日期（不传则默认当天）
     * @return 时间段统计结果
     */
    RiskTimePeriodCountVO countHighRiskWorkerByTimePeriod(LocalDate statDate);
}
