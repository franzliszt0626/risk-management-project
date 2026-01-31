package gang.lu.riskmanagementproject.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:22
 * @description 风险等级，前端需要提供下拉菜单，不可让用户自己输入
 */
@Getter
public enum RiskLevel {
    /*
      风险程度，默认低风险
     */
    LOW_RISK("低风险"),
    MEDIUM_RISK("中风险"),
    HIGH_RISK("高风险"),
    VERY_HIGH_RISK("严重风险");

    @EnumValue
    private final String value;

    RiskLevel(String value) {
        this.value = value;
    }

}
