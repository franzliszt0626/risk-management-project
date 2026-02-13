package gang.lu.riskmanagementproject.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import gang.lu.riskmanagementproject.util.EnumConvertUtil;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 23:23
 * @description 通用条件构建器
 */
public class QueryWrapperHelper<T> extends LambdaQueryWrapper<T> {

    private QueryWrapperHelper() {
        super();
    }

    public static <T> QueryWrapperHelper<T> create() {
        return new QueryWrapperHelper<>();
    }

    /**
     * 字符串非空 → like （链式）
     */
    public QueryWrapperHelper<T> likeIfPresent(SFunction<T, ?> field, String value) {
        if (StrUtil.isNotBlank(value)) {
            super.like(field, value.trim());
        }
        return this;
    }

    /**
     * 值非空 → eq （链式）
     */
    public QueryWrapperHelper<T> eqIfPresent(SFunction<T, ?> field, Object value) {
        if (ObjectUtil.isNotNull(value)) {
            super.eq(field, value);
        }
        return this;
    }

    /**
     * 字符串转枚举 → eq （链式）
     */
    public <E extends Enum<E> & ValueEnum<String>> QueryWrapperHelper<T> eqEnumIfPresent(
            SFunction<T, E> field,
            String value,
            Class<E> enumClass
    ) {
        if (StrUtil.isNotBlank(value)) {
            E enumVal = EnumConvertUtil.toEnum(value, enumClass);
            if (enumVal != null) {
                super.eq(field, enumVal);
            }
        }
        return this;
    }

    /**
     * 范围 ≥ （链式）
     */
    public QueryWrapperHelper<T> geIfPresent(SFunction<T, ? extends Comparable<?>> field, Object value) {
        if (ObjectUtil.isNotNull(value)) {
            super.ge(field, value);
        }
        return this;
    }

    /**
     * 范围 ≤ （链式）
     */
    public QueryWrapperHelper<T> leIfPresent(SFunction<T, ? extends Comparable<?>> field, Object value) {
        if (ObjectUtil.isNotNull(value)) {
            super.le(field, value);
        }
        return this;
    }

    /**
     * 排序（链式）
     */
    @Override
    public QueryWrapperHelper<T> orderByDesc(SFunction<T, ?> field) {
        super.orderByDesc(field);
        return this;
    }
}
