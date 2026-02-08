package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.AlertRecordVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.AlertRecordMapper;
import gang.lu.riskmanagementproject.service.AlertRecordService;
import gang.lu.riskmanagementproject.util.BusinessVerifyUtil;
import gang.lu.riskmanagementproject.util.ConvertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static gang.lu.riskmanagementproject.common.BusinessScene.*;
import static gang.lu.riskmanagementproject.common.IdName.ALERT_RECORD_ID;


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

    /**
     * 新增一条预警记录
     *
     * @param dto dto实体
     * @return VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AlertRecordVO addAlertRecord(AlertRecordDTO dto) {
// 1. 校验工人存在性
        Long workerId = dto.getWorkerId();
        BusinessVerifyUtil.validateWorkerExist(workerId, ADD_ALERT_RECORD);

        // 2. 校验预警等级
        BusinessVerifyUtil.validateAlertLevel(dto.getAlertLevel(), ADD_ALERT_RECORD);

        // 3. DTO转PO + 插入
        AlertRecord alertRecord = ConvertUtil.convert(dto, AlertRecord.class);
        int inserted = alertRecordMapper.insert(alertRecord);
        if (inserted != 1) {
            log.error("【新增预警记录失败】数据库插入异常，工人ID：{}", workerId);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }

        log.info("【新增预警记录成功】预警记录ID：{}，工人ID：{}，预警等级：{}",
                alertRecord.getId(), workerId, alertRecord.getAlertLevel().getValue());
        return ConvertUtil.convert(alertRecord, AlertRecordVO.class);
    }

    /**
     * 删除预警记录（根据ID）
     *
     * @param id id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlertRecord(Long id) {
        AlertRecord alertRecord = validateAlertRecordExist(id);
        int deleted = alertRecordMapper.deleteById(id);
        if (deleted != 1) {
            log.error("【删除预警记录失败】数据库删除异常，预警记录ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }
        log.info("【删除预警记录成功】预警记录ID：{}", id);
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
        AlertRecord alertRecord = validateAlertRecordExist(id);

        // 校验工人ID
        Long newWorkerId = dto.getWorkerId();
        if (ObjectUtil.isNotNull(newWorkerId)) {
            BusinessVerifyUtil.validateWorkerExist(newWorkerId, UPDATE_ALERT_RECORD);
        } else {
            BusinessVerifyUtil.validateWorkerExist(alertRecord.getWorkerId(), UPDATE_ALERT_RECORD);
        }

        // 校验预警等级（若传入）
        if (ObjectUtil.isNotNull(dto.getAlertLevel())) {
            BusinessVerifyUtil.validateAlertLevel(dto.getAlertLevel(), UPDATE_ALERT_RECORD);
        }

        // DTO转PO（覆盖非空字段）
        BeanUtil.copyProperties(dto, alertRecord, "id", "createdTime");
        int updated = alertRecordMapper.updateById(alertRecord);
        if (updated != 1) {
            log.error("【修改预警记录失败】数据库更新异常，预警记录ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }

        log.info("【修改预警记录成功】预警记录ID：{}", id);
        AlertRecord latestRecord = alertRecordMapper.selectById(id);
        return ConvertUtil.convert(latestRecord, AlertRecordVO.class);
    }


    /**
     * 根据ID查询（查不到抛BizException）
     *
     * @param id 预警记录id
     * @return VO
     */
    @Override
    public AlertRecordVO getAlertRecordById(Long id) {
        AlertRecord alertRecord = validateAlertRecordExist(id);
        log.info("【查询预警记录成功】预警记录ID：{}", id);
        return ConvertUtil.convert(alertRecord, AlertRecordVO.class);
    }

    /**
     * 根据工人ID查询（查不到返回空）
     *
     * @param workerId 工人id
     * @return 集合
     */
    @Override
    public List<AlertRecordVO> getAlertRecordsByWorkerId(Long workerId) {
        BusinessVerifyUtil.validateWorkerExist(workerId, GET_ALERT_RECORD);

        List<AlertRecord> alertRecords = alertRecordMapper.selectByWorkerId(workerId);
        if (ObjectUtil.isEmpty(alertRecords)) {
            log.info("【查询工人预警记录】工人ID：{} 暂无预警记录", workerId);
            return Collections.emptyList();
        }

        List<AlertRecordVO> voList = ConvertUtil.convertList(alertRecords, AlertRecordVO.class);
        log.info("【查询工人预警记录成功】工人ID：{}，共{}条预警记录", workerId, voList.size());
        return voList;
    }

    /**
     * 根据预警级别查询
     *
     * @param alertLevel 级别
     * @return 结果集合
     */
    @Override
    public List<AlertRecordVO> getAlertRecordsByAlertLevel(AlertLevel alertLevel) {
        BusinessVerifyUtil.validateAlertLevel(alertLevel, GET_ALERT_RECORD);

        List<AlertRecord> alertRecords = alertRecordMapper.selectByAlertLevel(alertLevel.getValue());
        if (ObjectUtil.isEmpty(alertRecords)) {
            log.info("【查询预警记录】预警级别：{} 暂无预警记录", alertLevel.getValue());
            return Collections.emptyList();
        }

        List<AlertRecordVO> voList = ConvertUtil.convertList(alertRecords, AlertRecordVO.class);
        log.info("【查询预警记录成功】预警级别：{}，共{}条预警记录", alertLevel.getValue(), voList.size());
        return voList;
    }

    /**
     * 根据预警类型模糊查询
     *
     * @param alertType 类型
     * @return 结果集合
     */
    @Override
    public List<AlertRecordVO> getAlertRecordsByAlertTypeLike(String alertType) {
        BusinessVerifyUtil.validateStringNotBlank(alertType, "预警类型", GET_ALERT_RECORD);

        List<AlertRecord> alertRecords = alertRecordMapper.selectByAlertTypeLike(alertType);
        if (ObjectUtil.isEmpty(alertRecords)) {
            log.info("【查询预警记录】预警类型（模糊）：{} 暂无预警记录", alertType);
            return Collections.emptyList();
        }

        List<AlertRecordVO> voList = ConvertUtil.convertList(alertRecords, AlertRecordVO.class);
        log.info("【查询预警记录成功】预警类型（模糊）：{}，共{}条预警记录", alertType, voList.size());
        return voList;
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

        validateAlertRecordExist(id);
        BusinessVerifyUtil.validateStringNotBlank(handledBy, "处理人", "标记预警记录已处理");

        int rows = alertRecordMapper.markAsHandled(id, handledBy, LocalDateTime.now());
        if (rows == 0) {
            log.error("【标记预警记录已处理失败】数据库更新异常，预警记录ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.ALERT_OPERATE_HANDLE_ERROR);
        }

        log.info("【标记预警记录已处理成功】预警记录ID：{}，处理人：{}", id, handledBy);
    }

    /**
     * 校验预警记录ID合法性+记录存在性
     */
// 仅保留核心校验逻辑，删除冗余的枚举校验
    private AlertRecord validateAlertRecordExist(Long id) {
        BusinessVerifyUtil.validateId(id, ALERT_RECORD_ID);
        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        if (ObjectUtil.isNull(alertRecord)) {
            log.warn("【预警记录操作失败】预警记录不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_DATA_NOT_FOUND);
        }
        return alertRecord;
    }
}
