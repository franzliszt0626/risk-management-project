package gang.lu.riskmanagementproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 预警记录表 Mapper 接口
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Mapper
public interface AlertRecordMapper extends BaseMapper<AlertRecord> {
    /**
     * 根据工人ID查询预警记录
     * @param workerId 工人id
     * @return 预警记录集合
     */
    List<AlertRecord> selectByWorkerId(@Param("workerId") Long workerId);

    /**
     * 根据预警级别查询
     * @param alertLevel 级别
     * @return 预警记录
     */
    List<AlertRecord> selectByAlertLevel(@Param("alertLevel") String alertLevel);

    /**
     * 根据预警类型模糊查询
     * @param alertType 类型
     * @return 信息集合
     */
    List<AlertRecord> selectByAlertTypeLike(@Param("alertType") String alertType);

    /**
     * 标记预警记录为已处理
     * @param id 记录的id
     * @param handledBy 被谁处理了
     * @param handleTime 处理时间
     * @return 是否成功
     */
    @Update("UPDATE t_alert_record SET is_handled = 1, handled_by = #{handledBy}, handle_time = #{handleTime} WHERE id = #{id}")
    int markAsHandled(@Param("id") Long id, @Param("handledBy") String handledBy, @Param("handleTime") LocalDateTime handleTime);

}
