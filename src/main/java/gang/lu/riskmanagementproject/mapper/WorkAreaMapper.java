package gang.lu.riskmanagementproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gang.lu.riskmanagementproject.domain.po.WorkArea;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>
 * 工作区域表 Mapper 接口
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Mapper
public interface WorkAreaMapper extends BaseMapper<WorkArea> {

    /**
     * 按风险等级统计工作区域数量
     *
     * @return Map<风险等级, 子Map(包含risk_level和count)>
     * 指定外层Map的key为risk_level列
     */
    @MapKey("risk_level")
    Map<String, Map<String, Object>> countWorkAreaByRiskLevel();
}
