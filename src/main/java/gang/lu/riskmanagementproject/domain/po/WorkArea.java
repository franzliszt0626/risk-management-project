package gang.lu.riskmanagementproject.domain.po;


import com.baomidou.mybatisplus.annotation.*;
import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


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

    @TableField(fill = FieldFill.DEFAULT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;
}