package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


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
        // 得到工人的id
        Long workerId = dto.getWorkerId();
        // 使用复用通用工具，校验工人ID合法性+工人存在性
        BusinessVerifyUtil.validateWorker(workerId, "新增预警记录");

        // 2. 校验预警等级（核心逻辑：适配枚举）
        validateAlertLevel(dto.getAlertLevel(), workerId);

        // DTO转PO
        AlertRecord alertRecord = BeanUtil.copyProperties(dto, AlertRecord.class);

        // 4. 插入数据并校验结果
        int inserted = alertRecordMapper.insert(alertRecord);
        if (inserted != 1) {
            log.error("【新增预警记录失败】数据库插入异常，工人ID：{}，影响行数：{}", workerId, inserted);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }

        log.info("【新增预警记录成功】预警记录ID：{}，工人ID：{}，预警等级：{}",
                alertRecord.getId(), workerId, alertRecord.getAlertLevel().getValue());
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
        // 1、复用通用工具：校验预警记录ID合法性
        BusinessVerifyUtil.validateId(id, "预警记录ID");

        // 2、校验要删除的预警记录是否存在
        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        if (ObjectUtil.isNull(alertRecord)) {
            log.warn("【删除预警记录失败】预警记录不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_DATA_NOT_FOUND);
        }

        // 3、存在的话删除数据并校验结果
        int deleted = alertRecordMapper.deleteById(id);
        if (deleted != 1) {
            log.error("【删除预警记录失败】数据库删除异常，预警记录ID：{}，影响行数：{}", id, deleted);
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
        // 1. 校验预警记录ID合法性+存在性
        BusinessVerifyUtil.validateId(id, "预警记录ID");
        // 2、拿到要修改的id对应的预警记录
        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        // 3、如果对应的预警记录为空，返回异常
        if (ObjectUtil.isNull(alertRecord)) {
            log.warn("【修改预警记录失败】预警记录不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_DATA_NOT_FOUND);
        }


        // 4、若DTO传入了工人ID，校验工人存在性
        Long newWorkerId = dto.getWorkerId();
        // 4.1、如果前端传了工人id
        if (ObjectUtil.isNotNull(newWorkerId)) {
            // 校验该工人是否存在？
            BusinessVerifyUtil.validateWorker(newWorkerId, "修改预警记录");
        } else {
            // 若未传新工人ID，校验原工人ID是否存在
            BusinessVerifyUtil.validateWorker(alertRecord.getWorkerId(), "修改预警记录");
        }

        // 3. 校验预警等级（如果传入了新等级）
        if (ObjectUtil.isNotNull(dto.getAlertLevel())) {
            validateAlertLevel(dto.getAlertLevel(), id);
        }

        // 3. DTO转PO（覆盖非空字段，排除主键和创建时间）
        BeanUtil.copyProperties(dto, alertRecord, "id", "createdTime");

        // 4. 更新数据并校验结果
        int updated = alertRecordMapper.updateById(alertRecord);
        if (updated != 1) {
            log.error("【修改预警记录失败】数据库更新异常，预警记录ID：{}，影响行数：{}", id, updated);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }

        log.info("【修改预警记录成功】预警记录ID：{}", id);
        // 5. 返回最新数据（重新查询，避免内存对象和数据库不一致）
        AlertRecord latestRecord = alertRecordMapper.selectById(id);
        return BeanUtil.copyProperties(latestRecord, AlertRecordVO.class);
    }


    /**
     * 根据ID查询（查不到抛BizException）
     *
     * @param id 预警记录id
     * @return VO
     */
    @Override
    public AlertRecordVO getAlertRecordById(Long id) {
        // 复用通用工具：校验预警记录ID合法性
        BusinessVerifyUtil.validateId(id, "预警记录ID");

        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        if (ObjectUtil.isNull(alertRecord)) {
            log.warn("【查询预警记录失败】预警记录不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_DATA_NOT_FOUND);
        }

        log.info("【查询预警记录成功】预警记录ID：{}", id);
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
        // 复用通用工具：校验工人ID合法性+存在性
        BusinessVerifyUtil.validateWorker(workerId, "查询工人预警记录");

        List<AlertRecord> alertRecords = alertRecordMapper.selectByWorkerId(workerId);
        if (ObjectUtil.isEmpty(alertRecords)) {
            log.info("【查询工人预警记录】工人ID：{} 暂无预警记录", workerId);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_DATA_WORKER_NOT_FOUND);
        }

        List<AlertRecordVO> voList = BeanUtil.copyToList(alertRecords, AlertRecordVO.class);
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
        // 补充预警级别空值校验
        if (ObjectUtil.isNull(alertLevel)) {
            log.warn("【查询预警记录失败】预警级别为空");
            throw new BizException(HttpStatus.BAD_REQUEST, FailureMessages.COMMON_REQUEST_PARAM_ERROR);
        }

        List<AlertRecord> alertRecords = alertRecordMapper.selectByAlertLevel(alertLevel.getValue());
        if (ObjectUtil.isEmpty(alertRecords)) {
            log.info("【查询预警记录】预警级别：{} 暂无预警记录", alertLevel.getValue());
            return Collections.emptyList();
        }

        List<AlertRecordVO> voList = BeanUtil.copyToList(alertRecords, AlertRecordVO.class);
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
        // 补充预警类型空值校验
        if (StrUtil.isBlank(alertType)) {
            log.warn("【查询预警记录失败】预警类型为空");
            throw new BizException(HttpStatus.BAD_REQUEST, FailureMessages.COMMON_REQUEST_PARAM_ERROR);
        }

        List<AlertRecord> alertRecords = alertRecordMapper.selectByAlertTypeLike(alertType);
        if (ObjectUtil.isEmpty(alertRecords)) {
            log.info("【查询预警记录】预警类型（模糊）：{} 暂无预警记录", alertType);
            return Collections.emptyList();
        }

        List<AlertRecordVO> voList = BeanUtil.copyToList(alertRecords, AlertRecordVO.class);
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
        // 1. 校验预警记录ID合法性+存在性
        BusinessVerifyUtil.validateId(id, "预警记录ID");
        AlertRecord alertRecord = alertRecordMapper.selectById(id);
        if (ObjectUtil.isNull(alertRecord)) {
            log.warn("【标记预警记录已处理失败】预警记录不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.ALERT_DATA_NOT_FOUND);
        }

        // 2. 校验处理人非空
        if (StrUtil.isBlank(handledBy)) {
            log.warn("【标记预警记录已处理失败】处理人为空，预警记录ID：{}", id);
            throw new BizException(HttpStatus.BAD_REQUEST, FailureMessages.COMMON_REQUEST_PARAM_ERROR);
        }

        // 3. 标记为已处理并校验结果
        int rows = alertRecordMapper.markAsHandled(id, handledBy, LocalDateTime.now());
        if (rows == 0) {
            log.error("【标记预警记录已处理失败】数据库更新异常，预警记录ID：{}，处理人：{}", id, handledBy);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.ALERT_OPERATE_HANDLE_ERROR);
        }

        log.info("【标记预警记录已处理成功】预警记录ID：{}，处理人：{}", id, handledBy);
    }

    /**
     * 校验预警等级合法性（适配枚举类型）
     */
    private void validateAlertLevel(AlertLevel alertLevel, Long businessId) {
        // 1. 空值校验
        if (ObjectUtil.isNull(alertLevel)) {
            log.warn("【预警等级校验失败】预警等级为空，业务ID：{}", businessId);
            throw new BizException(FailureMessages.ALERT_PARAM_EMPTY_LEVEL);
        }

        // 2. 枚举有效性兜底校验（通过fromValue反向验证）
        try {
            AlertLevel.fromValue(alertLevel.getValue());
        } catch (IllegalArgumentException e) {
            log.warn("【预警等级校验失败】预警等级不合法，业务ID：{}，传入等级：{}",
                    businessId, alertLevel.getValue(), e);
            throw new BizException(e.getMessage());
        }
    }
}
