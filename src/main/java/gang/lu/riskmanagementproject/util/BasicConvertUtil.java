package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础类型转换工具类
 * <p>
 * 提供 Long → Integer 安全转换、{@link Iterable} → {@link List} 安全转换等通用方法。
 * 所有方法均为静态方法，直接调用即可，无需注入。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Slf4j
public class BasicConvertUtil {

    private BasicConvertUtil() {
        // 工具类禁止实例化
    }

    /**
     * 将 {@code Long} 安全转换为 {@code Integer}。
     * <p>
     * 处理以下边界情况：
     * <ul>
     *   <li>value 为 null → 返回 defaultValue（defaultValue 本身为 null 时返回 0）；</li>
     *   <li>value 超过 {@link Integer#MAX_VALUE} → 记录 WARN 日志并返回 {@link Integer#MAX_VALUE}。</li>
     * </ul>
     *
     * @param value        被转换的 Long 值
     * @param defaultValue value 为 null 时的默认值
     * @return 转换后的 Integer 值
     */
    public static Integer safeLongToInt(Long value, Integer defaultValue) {
        if (ObjectUtil.isNull(value)) {
            return ObjectUtil.defaultIfNull(defaultValue, 0);
        }
        if (value > Integer.MAX_VALUE) {
            log.warn("[BasicUtil] Long 值 {} 超过 Integer 最大值，截断为 {}", value, Integer.MAX_VALUE);
            return Integer.MAX_VALUE;
        }
        return value.intValue();
    }

    /**
     * 将 {@code Iterable<Long>} 安全转换为 {@code List<Long>}，自动过滤 null 元素。
     *
     * @param ids 原始 ID 集合，允许为 null
     * @return 不含 null 元素的 List（ids 为 null 时返回空列表）
     */
    public static List<Long> safeConvertToList(Iterable<Long> ids) {
        List<Long> result = new ArrayList<>();
        if (ids == null) {
            return result;
        }
        for (Long id : ids) {
            if (ObjectUtil.isNotNull(id)) {
                result.add(id);
            }
        }
        return result;
    }
}