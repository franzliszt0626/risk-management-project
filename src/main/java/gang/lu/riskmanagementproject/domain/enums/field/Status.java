package gang.lu.riskmanagementproject.domain.enums.field;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import lombok.Getter;

/**
 * 工人在线状态枚举。
 * <p>
 * 对应数据库 {@code t_worker.status} 字段。
 * 前端应通过下拉菜单选择，禁止用户自由输入。
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Getter
public enum Status implements ValueEnum<String>, IEnum<String> {

    /**
     * 正常在岗
     */
    NORMAL("正常"),

    /**
     * 状态异常，需关注
     */
    ABNORMAL("异常"),

    /**
     * 已离线或未上报数据
     */
    OFFLINE("离线");

    /**
     * 存储到数据库及序列化给前端的中文值
     */
    @JsonValue
    private final String value;

    Status(String value) {
        this.value = value;
    }
}