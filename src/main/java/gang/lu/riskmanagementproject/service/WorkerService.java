package gang.lu.riskmanagementproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkerQueryDTO;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerStatusCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.worker.WorkerTypeCountVO;

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
     * 组合条件分页查询工人
     * @param workerQueryDTO 查询数据传输实体（继承PageQueryDTO）
     * @return 分页结果VO
     */
    PageVO<WorkerVO> searchWorkers(WorkerQueryDTO workerQueryDTO);

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
     * 按工人状态统计数量
     * @return 状态统计结果VO
     */
    WorkerStatusCountVO countWorkerByStatus();


    /**
     * 按工人工种统计数量
     * @return 状态统计结果VO
     */
    WorkerTypeCountVO countWorkerByWorkType();

    /**
     * 批量删除工人
     * @param ids id们
     */
    void batchDeleteWorkers(List<Long> ids);



}
