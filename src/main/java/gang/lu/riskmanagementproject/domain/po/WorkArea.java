package gang.lu.riskmanagementproject.domain.po;


import com.baomidou.mybatisplus.annotation.TableName;
import gang.lu.riskmanagementproject.domain.enums.field.AreaRiskLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:29
 * @description 工作地点
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("t_work_area")
public class WorkArea extends BasePO {


    private String areaCode;

    private String areaName;

    private AreaRiskLevel areaRiskLevel;

    private String description;
}