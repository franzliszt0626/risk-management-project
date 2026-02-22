package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.converter.RiskIndicatorConverter;
import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.dto.query.RiskIndicatorQueryDTO;
import gang.lu.riskmanagementproject.domain.enums.field.RiskLevel;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskLevelCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskTimePeriodCountVO;
import gang.lu.riskmanagementproject.helper.QueryWrapperHelper;
import gang.lu.riskmanagementproject.mapper.RiskIndicatorMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.RiskIndicatorService;
import gang.lu.riskmanagementproject.util.StatisticalUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import gang.lu.riskmanagementproject.validator.MedicalValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.BusinessConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * <p>
 * 风险指标表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Service
public class RiskIndicatorServiceImpl
        extends BaseCrudServiceImpl<RiskIndicator, RiskIndicatorDTO, RiskIndicatorVO, RiskIndicatorQueryDTO, RiskIndicatorMapper, RiskIndicatorConverter>
        implements RiskIndicatorService {

    private final WorkerMapper workerMapper;
    private final MedicalValidator medicalValidator;

    public RiskIndicatorServiceImpl(RiskIndicatorMapper riskIndicatorMapper,
                                    RiskIndicatorConverter riskIndicatorConverter,
                                    GeneralValidator generalValidator,
                                    WorkerMapper workerMapper,
                                    MedicalValidator medicalValidator) {
        super(riskIndicatorMapper, riskIndicatorConverter, generalValidator);
        this.medicalValidator = medicalValidator;
        this.workerMapper = workerMapper;
    }

    // ======================== 通用CRUD ========================

    /**
     * 新增风险指标
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = ADD_RISK_INDICATOR, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public RiskIndicatorVO add(RiskIndicatorDTO dto) {
        return super.add(dto);
    }

    /**
     * 删除风险指标
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = DELETE_RISK_INDICATOR, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void delete(Long id) {
        super.delete(id);
    }

    /**
     * 批量删除风险指标
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = BATCH_DELETE_RISK_INDICATOR, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void batchDelete(Iterable<Long> ids) {
        super.batchDelete(ids);
    }

    /**
     * 修改风险指标
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = UPDATE_RISK_INDICATOR, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public RiskIndicatorVO update(Long id, RiskIndicatorDTO dto) {
        return super.update(id, dto);
    }

    /**
     * 根据ID查询风险指标
     */
    @Override
    @BusinessLog(value = GET_RISK_INDICATOR, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public RiskIndicatorVO getOneById(Long id) {
        return super.getOneById(id);
    }

    /**
     * 多条件分页查询风险指标
     */
    @Override
    @BusinessLog(value = GET_RISK_INDICATOR_BY_MULTIPLY_CONDITION, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public PageVO<RiskIndicatorVO> search(RiskIndicatorQueryDTO queryDTO) {
        return super.search(queryDTO);
    }

    // ======================== 校验方法 ========================

    /**
     * 新增校验
     */
    @Override
    public void validateAdd(RiskIndicatorDTO dto) {
        generalValidator.validateIdExist(dto.getWorkerId(), workerMapper, WORKER_NOT_EXIST);
        medicalValidator.validateHeartRate(dto.getHeartRate());
        medicalValidator.validateRespiratoryRate(dto.getRespiratoryRate());
        medicalValidator.validateFatiguePercent(dto.getFatiguePercent());
    }

    /**
     * 修改校验
     */
    @Override
    public void validateUpdate(Long id, RiskIndicatorDTO dto) {
        if (ObjectUtil.isNotNull(dto.getWorkerId())) {
            generalValidator.validateIdExist(dto.getWorkerId(), workerMapper, WORKER_NOT_EXIST);
        }
        medicalValidator.validateHeartRate(dto.getHeartRate());
        medicalValidator.validateRespiratoryRate(dto.getRespiratoryRate());
        medicalValidator.validateFatiguePercent(dto.getFatiguePercent());
    }

    /**
     * 查询校验
     */
    @Override
    public void validateSearch(RiskIndicatorQueryDTO queryDTO) {
        generalValidator.validateTimeRange(queryDTO.getCreateStartTime(), queryDTO.getCreateEndTime());
        generalValidator.validateMinMaxRange(queryDTO.getMinHeartRate(), queryDTO.getMaxHeartRate(), HEART_RATE);
        generalValidator.validateMinMaxRange(queryDTO.getMinRespiratoryRate(), queryDTO.getMaxRespiratoryRate(), RESPIRATORY_RATE);
        generalValidator.validateMinMaxRange(queryDTO.getMinFatiguePercent(), queryDTO.getMaxFatiguePercent(), FATIGUE_PERCENT);
    }

    /**
     * 构建查询条件
     */
    @Override
    public LambdaQueryWrapper<RiskIndicator> buildQueryWrapper(RiskIndicatorQueryDTO queryDTO) {
        return QueryWrapperHelper.<RiskIndicator>create()
                .eqIfPresent(RiskIndicator::getWorkerId, queryDTO.getWorkerId())
                .eqEnumIfPresent(RiskIndicator::getRiskLevel, queryDTO.getRiskLevelValue(), RiskLevel.class)
                .eqIfPresent(RiskIndicator::getAlertFlag, queryDTO.getAlertFlag())
                .geIfPresent(RiskIndicator::getHeartRate, queryDTO.getMinHeartRate())
                .leIfPresent(RiskIndicator::getHeartRate, queryDTO.getMaxHeartRate())
                .geIfPresent(RiskIndicator::getRespiratoryRate, queryDTO.getMinRespiratoryRate())
                .leIfPresent(RiskIndicator::getRespiratoryRate, queryDTO.getMaxRespiratoryRate())
                .geIfPresent(RiskIndicator::getFatiguePercent, queryDTO.getMinFatiguePercent())
                .leIfPresent(RiskIndicator::getFatiguePercent, queryDTO.getMaxFatiguePercent())
                .geIfPresent(RiskIndicator::getCreateTime, queryDTO.getCreateStartTime())
                .leIfPresent(RiskIndicator::getCreateTime, queryDTO.getCreateEndTime())
                .orderByDesc(RiskIndicator::getCreateTime);
    }

    // ======================== 模板方法 ========================

    @Override
    protected String getNotFoundMsg() {
        return RISK_INDICATOR_NOT_EXIST;
    }

    @Override
    protected String getBatchIdEmptyMsg() {
        return RISK_INDICATOR_DELETE_BATCH_ID_EMPTY;
    }

    @Override
    protected String getBatchNotFoundMsg() {
        return RISK_INDICATOR_DELETE_BATCH_ID_INVALID;
    }

    @Override
    protected String getBusinessScene() {
        return GET_RISK_INDICATOR;
    }

    // ======================== 个性化业务 ========================

    /**
     * 根据工人ID查询最新风险指标
     */
    @Override
    @BusinessLog(value = GET_LATEST_RISK_INDICATOR, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public RiskIndicatorVO getLatestRiskIndicatorByWorkerId(Long workerId) {
        generalValidator.validateIdExist(workerId, workerMapper, WORKER_NOT_EXIST);
        RiskIndicator latest = baseMapper.selectLatestByWorkerId(workerId);
        return ObjectUtil.isNull(latest) ? null : converter.poToVo(latest);
    }

    /**
     * 统计去重工人风险等级人数分布
     */
    @Override
    @BusinessLog(value = GET_RISK_LEVEL_DISTRIBUTION, recordParams = false, logLevel = BusinessLog.LogLevel.INFO)
    public RiskLevelCountVO countDistinctWorkerByRiskLevel() {
        Map<String, Map<String, Object>> riskCountMap = baseMapper.countDistinctWorkerByRiskLevel();
        RiskLevelCountVO vo = new RiskLevelCountVO();
        vo.setLowRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, RiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, RiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, RiskLevel.HIGH_RISK.getValue()));
        vo.setVeryHighRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, RiskLevel.VERY_HIGH_RISK.getValue()));
        vo.setTotalCount(vo.getLowRiskCount() + vo.getMediumRiskCount() + vo.getHighRiskCount() + vo.getVeryHighRiskCount());
        return vo;
    }

    /**
     * 统计当日各时间段高风险工人数
     */
    @Override
    @BusinessLog(value = GET_HIGH_RISK_DISTRIBUTION_IN_PERIOD, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public RiskTimePeriodCountVO countHighRiskWorkerByTimePeriod(LocalDate statDate) {
        LocalDate finalStatDate = ObjectUtil.defaultIfNull(statDate, LocalDate.now());
        String statDateStr = finalStatDate.toString();
        List<Map<String, Object>> periodList = baseMapper.countHighRiskWorkerByTimePeriod(statDateStr);
        RiskTimePeriodCountVO resultVO = new RiskTimePeriodCountVO();
        resultVO.setStatDate(statDateStr);
        resultVO.setPeriodItems(StatisticalUtil.buildTimePeriodItems(periodList));
        return resultVO;
    }
}