package gang.lu.riskmanagementproject.domain.enums;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 00:40
 * @description 通用枚举接口
 */
public interface ValueEnum<T> {
    /**
     * 获取枚举的value值（如"低风险"）
     *
     * @return 枚举的值
     */
    T getValue();

    /**
     * 根据值查找枚举（通用默认方法）
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @return 枚举对象（空则返回null）
     */
    static <E extends Enum<E> & ValueEnum<T>, T> E fromValue(Class<E> enumClass, T value) {
        if (value == null) {
            return null;
        }
        for (E enumConstant : enumClass.getEnumConstants()) {
            if (value.equals(enumConstant.getValue())) {
                return enumConstant;
            }
        }
        return null;
    }
}
