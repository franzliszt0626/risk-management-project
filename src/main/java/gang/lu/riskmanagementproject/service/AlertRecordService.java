package gang.lu.riskmanagementproject.service;

import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.dto.query.AlertRecordQueryDTO;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.alert.AlertUnhandledCountVO;

/**
 * <p>
 * 预警记录表 服务类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
public interface AlertRecordService extends BaseCrudService<AlertRecord, AlertRecordDTO, AlertRecordVO, AlertRecordQueryDTO> {

    /**
     * 标记预警记录为已处理
     *
     * @param id        id
     * @param handledBy 由谁处理
     */
    void markAlertRecordAsHandled(Long id, String handledBy);

    /**
     * 统计未处理的预警记录个数
     * @return 个数统计实体
     */
    AlertUnhandledCountVO countUnhandledAlerts();
}
