package gang.lu.riskmanagementproject.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 21:44
 * @description 转换工具
 */
public class ConvertUtil {

    private ConvertUtil() {
    }

    /**
     * 单个对象转换（PO→VO / DTO→PO）
     */
    public static <T, R> R convert(T source, Class<R> targetClass) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        return BeanUtil.copyProperties(source, targetClass);
    }

    /**
     * 集合对象转换
     */
    public static <T, R> List<R> convertList(List<T> sourceList, Class<R> targetClass) {
        if (ObjectUtil.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        return sourceList.stream()
                .map(item -> convert(item, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * 分页对象转换（MP Page）- 简化逻辑，保留所有分页参数
     */
    public static <T, R> Page<R> convertPage(Page<T> sourcePage, Class<R> targetClass) {
        Page<R> targetPage = new Page<>(sourcePage.getCurrent(), sourcePage.getSize(), sourcePage.getTotal());
        targetPage.setPages(sourcePage.getPages());
        targetPage.setRecords(convertList(sourcePage.getRecords(), targetClass));
        return targetPage;
    }

    /**
     * 兼容旧方法（保持向下兼容）
     */
    public static <T, R> Page<R> convertPageWithManualTotal(Page<T> sourcePage, Class<R> targetClass) {
        return convertPage(sourcePage, targetClass);
    }
}
