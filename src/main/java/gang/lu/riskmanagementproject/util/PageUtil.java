package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 全局分页工具类
 * 职责：统一处理分页参数、构建分页对象，全局复用
 *
 * @author Franz Liszt
 * @date 2026/2/9
 */
@Slf4j
@Component
public final class PageUtil {


    private static Integer defaultPageNum;
    private static Integer defaultPageSize;
    private static Integer maxPageSize;

    @Value("${spring.page.default-num}")
    public void setDefaultPageNum(Integer defaultPageNum) {
        PageUtil.defaultPageNum = defaultPageNum;
    }

    @Value("${spring.page.default-size}")
    public void setDefaultPageSize(Integer defaultPageSize) {
        PageUtil.defaultPageSize = defaultPageSize;
    }

    @Value("${spring.page.max-size}")
    public void setMaxPageSize(Integer maxPageSize) {
        PageUtil.maxPageSize = maxPageSize;
    }

    private PageUtil() {
    }


    /**
     * 通用分页参数处理（配置化，适配所有业务）
     *
     * @param pageNum       原始页码
     * @param pageSize      原始页大小
     * @param businessScene 业务场景（用于日志）
     * @return 处理后的分页参数 [finalPageNum, finalPageSize]
     */
    public static Integer[] handlePageParams(Integer pageNum, Integer pageSize, String businessScene) {
        // 使用配置文件的默认值
        int finalPageNum = ObjectUtil.defaultIfNull(pageNum, defaultPageNum);
        int finalPageSize = ObjectUtil.defaultIfNull(pageSize, defaultPageSize);

        // 边界校验
        finalPageNum = Math.max(finalPageNum, defaultPageNum);
        finalPageSize = Math.max(1, Math.min(finalPageSize, maxPageSize));

        log.debug("[{}] 分页参数处理：原始[{}, {}] → 处理后[{}, {}]",
                businessScene, pageNum, pageSize, finalPageNum, finalPageSize);
        return new Integer[]{finalPageNum, finalPageSize};
    }

    /**
     * 构建MyBatis-Plus分页对象（复用分页查询逻辑）
     *
     * @param pageNum       页码
     * @param pageSize      页大小
     * @param businessScene 业务场景
     * @return 初始化后的Page对象
     */
    public static <T> Page<T> buildPage(Integer pageNum, Integer pageSize, String businessScene) {
        Integer[] pageParams = handlePageParams(pageNum, pageSize, businessScene);
        return new Page<>(pageParams[0], pageParams[1]);
    }


    /**
     * 补充分页总数和总页数（解决手动分页时的总数计算）
     *
     * @param page  MyBatis-Plus分页对象
     * @param total 总记录数
     */
    public static <T> void fillPageTotal(Page<T> page, long total) {
        page.setTotal(total);
        // 计算总页数（避免除零）
        long pages = total > 0 ? (total + page.getSize() - 1) / page.getSize() : 0;
        page.setPages(pages);
    }

}