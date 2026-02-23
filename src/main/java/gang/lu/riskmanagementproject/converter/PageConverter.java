package gang.lu.riskmanagementproject.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 13:44
 * @description 分页对象转换器（独立抽离，继承通用转换器）统一处理Page<PO> ↔ PageVO<VO> 的转换逻辑
 */
public interface PageConverter<PO, DTO, VO> extends ClassConverter<PO, DTO, VO> {

    /**
     * 将MyBatis-Plus的Page<PO>转换为通用PageVO<VO>
     * @param poPage PO分页对象
     * @return 通用分页VO
     */
    default PageVO<VO> pagePoToPageVO(Page<PO> poPage) {
        PageVO<VO> pageVO = new PageVO<>();
        pageVO.setCurrent(poPage.getCurrent());
        pageVO.setSize(poPage.getSize());
        pageVO.setTotal(poPage.getTotal());
        pageVO.setPages(poPage.getPages());
        pageVO.setRecords(poListToVoList(poPage.getRecords()));
        return pageVO;
    }

    /**
     * 将MyBatis-Plus的Page<PO>转换为Page<VO>（兼容原有返回格式）
     * @param poPage PO分页对象
     * @return Page<VO>
     */
    default Page<VO> pagePoToPageVOCompat(Page<PO> poPage) {
        Page<VO> voPage = new Page<>(poPage.getCurrent(), poPage.getSize(), poPage.getTotal());
        voPage.setPages(poPage.getPages());
        voPage.setRecords(poListToVoList(poPage.getRecords()));
        return voPage;
    }
}
