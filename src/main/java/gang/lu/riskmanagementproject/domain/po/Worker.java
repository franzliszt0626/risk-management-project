package gang.lu.riskmanagementproject.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:07
 * @description 工人信息实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("t_worker")
public class Worker extends BasePO {

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

