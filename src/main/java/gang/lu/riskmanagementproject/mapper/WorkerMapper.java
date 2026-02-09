package gang.lu.riskmanagementproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gang.lu.riskmanagementproject.domain.po.Worker;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工人基本信息表 Mapper 接口
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
public interface WorkerMapper extends BaseMapper<Worker> {


    /**
     * 按工人状态统计数量（返回List<Map>接收多条结果）
     *
     * @return 每条结果包含 status 和 count 字段
     */
    @Select("SELECT status, COUNT(*) AS count FROM t_worker GROUP BY status")
    List<Map<String, Object>> countWorkerByStatus();

    /**
     * 按工种统计数量
     *
     * @return 按状态统计的结果列表
     */
    @Select("SELECT work_type, COUNT(*) AS count FROM t_worker GROUP BY work_type")
    List<Map<String, Object>> countWorkerByWorkType();

}
