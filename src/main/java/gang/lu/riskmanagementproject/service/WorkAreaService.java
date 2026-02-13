package gang.lu.riskmanagementproject.service;

import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkAreaQueryDTO;
import gang.lu.riskmanagementproject.domain.po.WorkArea;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkAreaVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.area.WorkAreaRiskCountVO;

import java.util.List;

/**
 * <p>
 * 工作区域表 服务类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
public interface WorkAreaService extends BaseCrudService<WorkArea, WorkAreaDTO, WorkAreaVO, WorkAreaQueryDTO> {


    /**
     * 按区域编码查询工作区域
     *
     * @param areaCode 他的代码
     * @return 展示
     */
    List<WorkAreaVO> getWorkAreaByCode(String areaCode);


    /**
     * 按风险等级统计工作区域数量
     *
     * @return 风险等级划分的统计数据
     */
    WorkAreaRiskCountVO countWorkAreaByRiskLevel();
}
