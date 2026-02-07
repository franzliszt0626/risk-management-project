package gang.lu.riskmanagementproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 实时风险指标表 Mapper 接口
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
public interface RiskIndicatorMapper extends BaseMapper<RiskIndicator> {

    /**
     * 根据工人id找到最新的记录
     * @param workerId 工人id
     * @return 指标信息
     */
    RiskIndicator selectLatestByWorkerId(@Param("workerId") Long workerId);
}
