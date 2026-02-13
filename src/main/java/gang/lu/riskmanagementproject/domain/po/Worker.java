package gang.lu.riskmanagementproject.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:07
 * @description 工人信息实体
 */
@Data
@Accessors(chain = true)
@TableName("t_worker")
public class Worker implements BasePO {

    /**
     * 主键，工人id，默认自增长
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工人工号，唯一标识
     */
    private String workerCode;

    /**
     * 工人姓名
     */
    private String name;

    /**
     * 工人岗位
     */
    private String position;

    /**
     * 工龄
     */
    private Integer workYears;

    /**
     * 工作种类
     */
    private WorkType workType;

    /**
     * 当前状态
     */
    private Status status;

    @TableField(fill = FieldFill.DEFAULT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.DEFAULT)
    private LocalDateTime updateTime;
}

