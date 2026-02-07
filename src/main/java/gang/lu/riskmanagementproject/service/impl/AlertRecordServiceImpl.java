package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.common.AlertConstants;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.AlertRecordVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.AlertRecordMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.AlertRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 预警记录表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlertRecordServiceImpl extends ServiceImpl<AlertRecordMapper, AlertRecord> implements AlertRecordService {

    private final AlertRecordMapper alertRecordMapper;

    private final WorkerMapper workerMapper;

    /**
     * 新增一条预警记录
     *
     * @param dto dto实体
     * @return VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AlertRecordVO addAlertRecord(AlertRecordDTO dto) {
        // 取出工人id
        Long workerId = dto.getWorkerId();
        if (Objects.isNull(workerId)) {
            throw new BizException(FailureMessages.INVALID_ID_ERROR_MESSAGE);
        }
        // 如果工人不存在
        if (Objects.isNull(workerMapper.selectById(workerId))) {
            throw new BizException(FailureMessages.WORKER_NOT_EXISTING_ERROR_MESSAGE);
        }
        // 1. DTO转PO
        AlertRecord alertRecord = BeanUtil.copyProperties(dto, AlertRecord.class);
        // 2. 默认未处理
        if (alertRecord.getIsHandled() == null) {
            alertRecord.setIsHandled(AlertConstants.UNHANDLED);
        }
        // 3. 设置创建时间（若未传）
        if (alertRecord.getCreatedTime() == null) {
            alertRecord.setCreatedTime(LocalDateTime.now());
        }
        // 4. 保存
        alertRecordMapper.insert(alertRecord);
        log.info("新增预警记录成功，ID：{}", alertRecord.getId());
        // 5. PO转VO返回
        return BeanUtil.copyProperties(alertRecord, AlertRecordVO.class);
    }

    /**
     * 删除预警记录（根据ID）
     *
     * @param id id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlertRecord(Long id) {
        // 1. 校验是否存在
        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        if (alertRecord == null) {
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_RECORD_NOT_FOUND_ERROR_MESSAGE);
        }
        // 2. 删除
        alertRecordMapper.deleteById(id);
        log.info("删除预警记录成功，ID：{}", id);
    }

    /**
     * 修改预警记录
     *
     * @param id  id
     * @param dto DTO
     * @return VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AlertRecordVO updateAlertRecord(Long id, AlertRecordDTO dto) {
        // 1. 校验是否存在
        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        if (alertRecord == null) {
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_RECORD_NOT_FOUND_ERROR_MESSAGE);
        }
        // 检查工人是否存在
        if (ObjectUtil.isNull(workerMapper.selectById(alertRecord.getWorkerId()))) {
            throw new BizException(FailureMessages.WORKER_NOT_EXISTING_ERROR_MESSAGE);
        }
        // 2. DTO转PO（覆盖非空字段）
        BeanUtil.copyProperties(dto, alertRecord, "id", "createdTime");
        // 3. 更新
        alertRecordMapper.updateById(alertRecord);
        log.info("修改预警记录成功，ID：{}", id);
        // 4. 返回最新数据
        return BeanUtil.copyProperties(alertRecord, AlertRecordVO.class);
    }

    /**
     * 根据ID查询（查不到抛BizException）
     *
     * @param id 预警记录id
     * @return VO
     */
    @Override
    public AlertRecordVO getAlertRecordById(Long id) {
        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        if (alertRecord == null) {
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_RECORD_NOT_FOUND_ERROR_MESSAGE);
        }
        return BeanUtil.copyProperties(alertRecord, AlertRecordVO.class);
    }

    /**
     * 根据工人ID查询（查不到返回空）
     *
     * @param workerId 工人id
     * @return 集合
     */
    @Override
    public List<AlertRecordVO> getAlertRecordsByWorkerId(Long workerId) {
        List<AlertRecord> alertRecords = alertRecordMapper.selectByWorkerId(workerId);
        if (alertRecords.isEmpty()) {
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_ALERT_RECORD_NOT_FOUND_ERROR_MESSAGE);
        }
        // PO转VO
        return BeanUtil.copyToList(alertRecords, AlertRecordVO.class);
    }

    /**
     * 根据预警级别查询
     *
     * @param alertLevel 级别
     * @return 结果集合
     */
    @Override
    public List<AlertRecordVO> getAlertRecordsByAlertLevel(String alertLevel) {
        List<AlertRecord> alertRecords = alertRecordMapper.selectByAlertLevel(alertLevel);
        if (alertRecords.isEmpty()) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(alertRecords, AlertRecordVO.class);
    }

    /**
     * 根据预警类型模糊查询
     *
     * @param alertType 类型
     * @return 结果集合
     */
    @Override
    public List<AlertRecordVO> getAlertRecordsByAlertTypeLike(String alertType) {
        List<AlertRecord> alertRecords = alertRecordMapper.selectByAlertTypeLike(alertType);
        if (alertRecords.isEmpty()) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(alertRecords, AlertRecordVO.class);
    }

    /**
     * 标记预警记录为已处理
     *
     * @param id        id
     * @param handledBy 由谁处理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAlertRecordAsHandled(Long id, String handledBy) {
        // 1. 校验是否存在
        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        if (alertRecord == null) {
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_RECORD_NOT_FOUND_ERROR_MESSAGE);
        }
        // 2. 标记为已处理
        int rows = alertRecordMapper.markAsHandled(id, handledBy, LocalDateTime.now());
        if (rows == 0) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.FAILED_HANDLED_ERROR_MESSAGE);
        }
        log.info("预警记录{}已标记为已处理，处理人：{}", id, handledBy);
    }
}
