package gang.lu.riskmanagementproject.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import gang.lu.riskmanagementproject.domain.enums.field.AreaRiskLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 工作区域实体，对应数据库表 {@code t_work_area}。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_work_area")
public class WorkArea extends BasePO {

    /**
     * 区域编码，全局唯一
     */
    private String areaCode;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域当前风险等级
     */
    private AreaRiskLevel areaRiskLevel;

    /**
     * 区域描述
     */
    private String description;
}