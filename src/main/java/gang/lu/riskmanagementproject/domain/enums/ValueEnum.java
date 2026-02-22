package gang.lu.riskmanagementproject.domain.enums;

/**
 * 通用枚举值接口。
 * <p>
 * 所有业务枚举均应实现此接口，以便统一通过 {@link #getValue()} 获取
 * 存储到数据库或序列化给前端的中文值，并使用 {@link #fromValue} 进行反向查找。
 *
 * @param <T> 枚举值的类型（通常为 {@link String}）
 * @author Franz Liszt
 * @since 2026-02-12
 */
public interface ValueEnum<T> {

    /**
     * 获取枚举对应的业务值（如 "低风险"、"正常"）。
     *
     * @return 枚举业务值
     */
    T getValue();

    /**
     * 根据业务值反查枚举常量。
     * <p>
     * value 为 null 或在枚举中找不到对应项时返回 null，不抛出异常。
     *
     * @param enumClass 目标枚举类
     * @param value     待匹配的业务值
     * @param <E>       枚举类型，须同时实现 {@link ValueEnum}
     * @param <T>       业务值类型
     * @return 匹配的枚举常量，未找到时返回 null
     */
    static <E extends Enum<E> & ValueEnum<T>, T> E fromValue(Class<E> enumClass, T value) {
        if (value == null) {
            return null;
        }
        for (E constant : enumClass.getEnumConstants()) {
            if (value.equals(constant.getValue())) {
                return constant;
            }
        }
        return null;
    }
}