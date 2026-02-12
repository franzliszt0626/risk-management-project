package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.StrUtil;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 20:13
 * @description 枚举转换工具
 */
public class EnumConvertUtil {
    /**
     * 字符串转枚举（空值/无效值返回null）
     */
    public static <T extends Enum<T> & ValueEnum<String>> T toEnum(String value, Class<T> enumClass) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        return ValueEnum.fromValue(enumClass, value.trim());
    }
}
