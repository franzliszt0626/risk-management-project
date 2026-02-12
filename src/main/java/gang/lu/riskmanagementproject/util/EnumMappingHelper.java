package gang.lu.riskmanagementproject.util;

import gang.lu.riskmanagementproject.domain.enums.ValueEnum;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 17:29
 * @description 枚举转换器
 */
public class EnumMappingHelper {

    public static <T extends Enum<T> & ValueEnum<String>> T strToEnum(String val, Class<T> enumClass) {
        return ValueEnum.fromValue(enumClass, val);
    }

    public static <T extends Enum<T> & ValueEnum<?>> String enumToStr(T e) {
        return e == null ? null : e.getValue().toString();
    }
}