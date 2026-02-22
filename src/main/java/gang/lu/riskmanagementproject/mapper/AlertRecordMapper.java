package gang.lu.riskmanagementproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.statistical.alert.AlertUnhandledCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

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
     * 标记预警记录为已处理
     *
     * @param id         记录的id
     * @param handledBy  被谁处理了
     * @param handleTime 处理时间
     * @return 是否成功
     */
    int markAsHandled(@Param("id") Long id, @Param("handledBy") String handledBy, @Param("handleTime") LocalDateTime handleTime);

    /**
     * 统计当前未处理的预警记录数量，按预警级别分组
     *
     * @return 各级别未处理数及总数
     */
    AlertUnhandledCountVO countUnhandled();
}
