package gang.lu.riskmanagementproject.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 所有持久化实体的公共基类。
 * <p>
 * 提供主键、创建时间、更新时间三个公共字段：
 * <ul>
 *   <li>{@code createTime}：仅在 INSERT 时由 MyBatis-Plus 自动填充，之后不再更新；</li>
 *   <li>{@code updateTime}：由数据库 {@code ON UPDATE CURRENT_TIMESTAMP} 触发，Java 侧不干预。</li>
 * </ul>
 *
 * @author Franz Liszt
 * @since 2026-02-13
 */
@Data
public abstract class BasePO implements Serializable {

    /**
     * 主键 ID，数据库自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间，INSERT 时自动填充，之后不可修改
     */
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新时间，由数据库 ON UPDATE CURRENT_TIMESTAMP 维护，Java 侧不写入
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;
}