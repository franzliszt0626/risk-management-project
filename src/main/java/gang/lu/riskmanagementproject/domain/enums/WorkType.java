package gang.lu.riskmanagementproject.domain.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:13
 * @description 工人的工作种类，前端需要提供下拉菜单，不可让用户自己输入
 */
@Getter
public enum WorkType implements ValueEnum<String>, IEnum<String> {

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
    @JsonValue
    private final String value;

    WorkType(String value) {
        this.value = value;
    }
}
