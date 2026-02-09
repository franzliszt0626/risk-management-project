package gang.lu.riskmanagementproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;

import java.util.List;

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
     * 根据工人ID查询（查不到返回空）
     * @param workerId 工人id
     * @return 集合
     */
    List<AlertRecordVO> getAlertRecordsByWorkerId(Long workerId);

    /**
     * 根据预警级别查询
     * @param alertLevel 级别
     * @return 结果集合
     */
    List<AlertRecordVO> getAlertRecordsByAlertLevel(AlertLevel alertLevel);

    /**
     * 根据预警类型模糊查询
     * @param alertType 类型
     * @return 结果集合
     */
    List<AlertRecordVO> getAlertRecordsByAlertTypeLike(String alertType);

    /**
     * 标记预警记录为已处理
     * @param id id
     * @param handledBy 由谁处理
     */
    void markAlertRecordAsHandled(Long id, String handledBy);

}
