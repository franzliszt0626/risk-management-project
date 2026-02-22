package gang.lu.riskmanagementproject.domain.enums.field;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import lombok.Getter;

/**
 * 工人工作种类枚举。
 * <p>
 * 对应数据库 {@code t_worker.work_type} 字段：
 * {@code ENUM('高空作业', '受限空间', '设备操作', '正常作业')}。
 * 不同工种对应不同的安全风险策略，前端应通过下拉菜单选择，禁止用户自由输入。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Getter
public enum WorkType implements ValueEnum<String>, IEnum<String> {

    /**
     * 高空作业，需配备防坠落装备
     */
    HIGH_ALTITUDE("高空作业"),

    /**
     * 受限空间作业，需监测氧气及有毒气体浓度
     */
    CONFINED_SPACE("受限空间"),

    /**
     * 设备操作，需防机械伤害
     */
    EQUIPMENT_OPERATION("设备操作"),

    /**
     * 正常作业，风险相对较低
     */
    NORMAL_WORK("正常作业");

    /**
     * 存储到数据库及序列化给前端的中文值
     */
    @JsonValue
    private final String value;

    WorkType(String value) {
        this.value = value;
    }
}