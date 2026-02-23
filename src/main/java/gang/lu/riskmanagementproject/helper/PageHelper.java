package gang.lu.riskmanagementproject.helper;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gang.lu.riskmanagementproject.domain.dto.query.PageQueryDTO;
import gang.lu.riskmanagementproject.property.PageProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static gang.lu.riskmanagementproject.common.global.GlobalLogConstants.LOG_PAGING;

/**
 * 全局分页工具类
 * 职责：统一处理分页参数、构建分页对象
 *
 * @author Franz Liszt
 * @date 2026/2/9
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class PageHelper {

    private final PageProperty pageProperty;

    /**
     * 处理分页参数（自动适配业务自定义规则）
     *
     * @param pageQuery     分页参数DTO
     * @param businessScene 业务场景（日志用）
     * @return 处理后的分页参数 [pageNum, pageSize]
     */
    public Integer[] handlePageParams(PageQueryDTO pageQuery, String businessScene) {
        // 1. 获取规则（优先业务自定义，无则用全局默认）
        int defaultPageNum = pageProperty.DEFAULT_NUM;
        int defaultPageSize = pageProperty.DEFAULT_SIZE;
        int maxPageSize = pageProperty.MAX_SIZE;
        // 2. 处理参数（空值用默认，超出范围则修正）
        int pageNum = ObjectUtil.defaultIfNull(pageQuery.getPageNum(), defaultPageNum);
        int pageSize = ObjectUtil.defaultIfNull(pageQuery.getPageSize(), defaultPageSize);
        pageNum = Math.max(pageNum, defaultPageNum);
        pageSize = Math.max(1, Math.min(pageSize, maxPageSize));
        log.debug(LOG_PAGING,
                businessScene, pageQuery.getPageNum(), pageQuery.getPageSize(),
                pageNum, pageSize, defaultPageSize, maxPageSize);
        return new Integer[]{pageNum, pageSize};
    }

    /**
     * 构建MyBatis-Plus分页对象（核心复用方法）
     *
     * @param pageQuery     分页参数DTO
     * @param businessScene 业务场景
     * @return 初始化后的Page对象
     */
    public <T> Page<T> buildPage(PageQueryDTO pageQuery, String businessScene) {
        Integer[] params = handlePageParams(pageQuery, businessScene);
        return new Page<>(params[0], params[1]);
    }

    /**
     * 绑定全局默认业务参数）
     *
     * @param queryDTO 查询dto
     */
    public <T extends PageQueryDTO> void bindGlobalDefaultRule(T queryDTO) {
        queryDTO.setPageNum(ObjectUtil.defaultIfNull(queryDTO.getPageNum(), pageProperty.DEFAULT_NUM))
                .setPageSize(ObjectUtil.defaultIfNull(queryDTO.getPageSize(), pageProperty.DEFAULT_SIZE));
    }
}