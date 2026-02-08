package gang.lu.riskmanagementproject.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.WorkerVO;

import java.util.List;

/**
 * <p>
 * 工人基本信息表 服务类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
public interface WorkerService extends IService<Worker> {

    /**
     * 创建工人
     *
     * @param dto 工人DTO
     */
    void createWorker(WorkerDTO dto);

    /**
     * 根据ID删除工人
     *
     * @param id 工人id
     */
    void deleteWorker(Long id);

    /**
     * 更新工人信息
     *
     * @param dto 工人DTO
     * @param id  工人id
     */
    void updateWorker(Long id, WorkerDTO dto);

    /**
     * 根据ID查询（返回VO）
     * @param id  工人id
     * @return WorkerVO
     */
    WorkerVO getWorkerById(Long id);

    /**
     * 根据工号查询（精确）
     * @param workerCode 工号
     * @return WorkerVO
     */
    WorkerVO getWorkerByCode(String workerCode);

    /**
     * 按姓名模糊查询
     * @param name 姓名
     * @return List<WorkerVO>
     */
    List<WorkerVO> searchWorkersByName(String name);

    /**
     * 按状态查询
     * @param status 工人状态
     * @return List<WorkerVO>
     */
    List<WorkerVO> getWorkersByStatus(Status status);

    /**
     * 按岗位查询
     * @param position 岗位
     * @return List<WorkerVO>
     */
    List<WorkerVO> getWorkersByPosition(String position);

    /**
     * 分页得到所有工人信息
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<WorkerVO>
     */
    Page<WorkerVO> getAllWorkers(Integer pageNum, Integer pageSize);

    /**
     * 按照工作种类查询
     * @param workType 工种
     * @return 集合
     */
    List<WorkerVO> getWorkersByWorkType(WorkType workType);
}
