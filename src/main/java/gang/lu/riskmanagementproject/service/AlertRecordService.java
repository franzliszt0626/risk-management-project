package gang.lu.riskmanagementproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.dto.query.AlertRecordQueryDTO;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;

/**
 * <p>
 * 预警记录表 服务类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
public interface AlertRecordService extends IService<AlertRecord> {

    /**
     * 新增一条预警记录
     * @param dto dto实体
     * @return VO
     */
    AlertRecordVO addAlertRecord(AlertRecordDTO dto);

    /**
     * 删除预警记录（根据ID）
     * @param id id
     */
    void deleteAlertRecord(Long id);

    /**
     * 修改预警记录
     * @param id id
     * @param dto DTO
     * @return VO
     */
    AlertRecordVO updateAlertRecord(Long id, AlertRecordDTO dto);

    /**
     * 根据ID查询（查不到抛BizException）
     * @param id 预警记录id
     * @return VO
     */
    AlertRecordVO getAlertRecordById(Long id);


    /**
     * 多条件组合分页查询预警记录
     * @param queryDTO 查询条件（含分页+多维度筛选）
     * @return 分页结果VO
     */
    PageVO<AlertRecordVO> searchAlertRecords(AlertRecordQueryDTO queryDTO);


    /**
     * 标记预警记录为已处理
     * @param id id
     * @param handledBy 由谁处理
     */
    void markAlertRecordAsHandled(Long id, String handledBy);

}
