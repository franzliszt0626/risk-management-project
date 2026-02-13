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
public interface WorkerService extends BaseCrudService<Worker, WorkerDTO, WorkerVO, WorkerQueryDTO> {


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


}
