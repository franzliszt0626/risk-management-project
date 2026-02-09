package gang.lu.riskmanagementproject.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import gang.lu.riskmanagementproject.common.FailureMessages;
import lombok.Getter;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:13
 * @description 工人的工作种类，前端需要提供下拉菜单，不可让用户自己输入
 */
@Getter
public enum WorkType {

    /*
     * 工人工作类型枚举
     * 对应数据库 t_worker.work_type 字段：
     * ENUM('高空作业', '受限空间', '设备操作', '正常作业')
     */
    HIGH_ALTITUDE("高空作业"),
    CONFINED_SPACE("受限空间"),
    EQUIPMENT_OPERATION("设备操作"),
    NORMAL_WORK("正常作业");

    /**
     * MyBatis-Plus 注解：指定存储到数据库的值
     */
    @EnumValue
    private final String value;

    WorkType(String value) {
        this.value = value;
    }

    /**
     * 工具方法：字符串转 WorkType 枚举
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static WorkType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(FailureMessages.WORKER_TYPE_EMPTY);
        }
        String trimValue = value.trim();
        for (WorkType type : values()) {
            if (type.value.equals(trimValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                String.format(FailureMessages.WORKER_TYPE_INVALID, trimValue)
        );
    }
}
