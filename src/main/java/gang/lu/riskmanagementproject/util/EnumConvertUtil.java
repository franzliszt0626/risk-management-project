package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.StrUtil;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;

/**
 * 枚举转换工具类
 * <p>
 * 基于 {@link ValueEnum#fromValue(Class, Object)} 提供字符串到枚举的安全转换，
 * 屏蔽空值和无效值，统一返回 null 而非抛出异常。
 *
 * @author Franz Liszt
 * @since 2026-02-12
 */
public class EnumConvertUtil {

    private EnumConvertUtil() {
        // 工具类禁止实例化
    }

    /**
     * 将字符串转换为目标枚举值。
     * <p>
     * 自动去除首尾空格；若 value 为空或在枚举中找不到对应项，则返回 null（不抛出异常）。
     *
     * @param value     待转换的字符串
     * @param enumClass 目标枚举类
     * @param <T>       枚举类型，须同时实现 {@link ValueEnum}{@code <String>}
     * @return 对应的枚举值，空值或无效值时返回 null
     */
    public static <T extends Enum<T> & ValueEnum<String>> T toEnum(String value, Class<T> enumClass) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        return ValueEnum.fromValue(enumClass, value.trim());
    }
}