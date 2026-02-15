package gang.lu.riskmanagementproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
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

}
