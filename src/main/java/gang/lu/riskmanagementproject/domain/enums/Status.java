package gang.lu.riskmanagementproject.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:15
 * @description 工人状态类，前端需要提供下拉菜单，不可让用户自己输入
 */
@Getter
public enum Status implements ValueEnum<String> {
    /**
     * 状态枚举
     */
    NORMAL("正常"),
    ABNORMAL("异常"),
    OFFLINE("离线");

    @EnumValue
    private final String value;

    Status(String value) {
        this.value = value;
    }
}