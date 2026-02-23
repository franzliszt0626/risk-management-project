package gang.lu.riskmanagementproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.field.FieldEnglishConstants.PERIOD;
import static gang.lu.riskmanagementproject.common.field.FieldEnglishConstants.RISK_LEVEL;

/**
 * <p>
 * 实时风险指标表 Mapper 接口
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Mapper
public interface RiskIndicatorMapper extends BaseMapper<RiskIndicator> {

    /**
     * 根据工人id找到最新的记录
     *
     * @param workerId 工人id
     * @return 指标信息
     */
    RiskIndicator selectLatestByWorkerId(@Param("workerId") Long workerId);

    /**
     * 统计去重工人的风险等级分布
     *
     * @return Map<风险等级, 子Map(包含risk_level和count)>
     */
    @MapKey(RISK_LEVEL)
    Map<String, Map<String, Object>> countDistinctWorkerByRiskLevel();

    /**
     * 统计当日各4小时时间段内高风险（非低风险）的去重工人数
     *
     * @param statDate 统计日期（yyyy-MM-dd）
     * @return Map<时间段标识(0/4/8/12/16/20), 子Map(period=时间段标识, count=人数)>
     */
    @MapKey(PERIOD)
    List<Map<String, Object>> countHighRiskWorkerByTimePeriod(@Param("statDate") String statDate);
}
