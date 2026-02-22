package gang.lu.riskmanagementproject.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import gang.lu.riskmanagementproject.domain.enums.field.Status;
import gang.lu.riskmanagementproject.domain.enums.field.WorkType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 工人信息实体，对应数据库表 {@code t_worker}。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_worker")
public class Worker extends BasePO {

    /**
     * 工号，全局唯一
     */
    private String workerCode;

    /**
     * 姓名
     */
    private String name;

    /**
     * 岗位名称
     */
    private String position;

    /**
     * 工龄（年）
     */
    private Integer workYears;

    /**
     * 工种
     */
    private WorkType workType;

    /**
     * 当前状态
     */
    private Status status;
}