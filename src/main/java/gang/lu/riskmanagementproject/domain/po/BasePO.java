package gang.lu.riskmanagementproject.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/13 00:06
 * @description PO通用接口
 */
@Data
public abstract class BasePO implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
}
