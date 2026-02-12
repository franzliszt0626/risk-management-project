package gang.lu.riskmanagementproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkAreaQueryDTO;
import gang.lu.riskmanagementproject.domain.po.WorkArea;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
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
public interface WorkAreaService extends IService<WorkArea> {

    /**
     * 新增工作区域
     *
     * @param dto 数据传输
     * @return 是否成功
     */
    boolean addWorkArea(WorkAreaDTO dto);

    /**
     * 删除工作区域（按ID）
     *
     * @param id 根据id删除
     * @return 是否成功
     */
    boolean deleteWorkArea(Long id);

    /**
     * 修改工作区域
     *
     * @param dto 数据传输
     * @param id  根据id更新
     * @return 是否成功
     */
    boolean updateWorkArea(Long id, WorkAreaDTO dto);


    /**
     * 据id精确查询工作区域信息
     *
     * @param id 他的id
     * @return 展示
     * @author Franz Liszt
     *
     */
    WorkAreaVO getWorkAreaById(Long id);

    /**
     * 按区域编码查询工作区域
     *
     * @param areaCode 他的代码
     * @return 展示
     */
    List<WorkAreaVO> getWorkAreaByCode(String areaCode);

    /**
     * 分页查询工作区域（支持多条件筛选，无条件则查全部）
     * @param queryDTO 分页DTO
     * @return 分页结果VO
     */
    PageVO<WorkAreaVO> searchWorkAreas(WorkAreaQueryDTO queryDTO);

    /**
     * 按风险等级统计工作区域数量
     * @return 风险等级划分的统计数据
     */
    WorkAreaRiskCountVO countWorkAreaByRiskLevel();
}
