package gang.lu.riskmanagementproject.domain.po;

import java.io.Serializable;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/13 00:06
 * @description PO通用接口
 */
public interface BasePO extends Serializable {
    /**
     * 设置主键ID（适配链式调用）
     *
     * @param id 主键
     * @return 当前PO对象
     */
    <T extends BasePO> T setId(Long id);

    /**
     * 获取主键ID
     *
     * @return 获取id
     */
    Long getId();
}
