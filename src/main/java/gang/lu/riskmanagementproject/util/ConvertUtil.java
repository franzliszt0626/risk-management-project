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
     * 分页对象转换（MP Page）
     */
    public static <T, R> Page<R> convertPage(Page<T> sourcePage, Class<R> targetClass) {
        Page<R> targetPage = new Page<>();
        BeanUtil.copyProperties(sourcePage, targetPage);
        targetPage.setRecords(convertList(sourcePage.getRecords(), targetClass));
        return targetPage;
    }

    /**
     * 手动拷贝分页参数（兼容手动设置total/pages的场景）
     */
    public static <T, R> Page<R> convertPageWithManualTotal(Page<T> sourcePage, Class<R> targetClass) {
        Page<R> targetPage = convertPage(sourcePage, targetClass);
        // 强制保留手动设置的total和pages
        targetPage.setTotal(sourcePage.getTotal());
        targetPage.setPages(sourcePage.getPages());
        return targetPage;
    }
}
