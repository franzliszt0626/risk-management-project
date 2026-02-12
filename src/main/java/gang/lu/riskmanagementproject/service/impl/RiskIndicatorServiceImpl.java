package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.converter.RiskIndicatorConverter;
import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.dto.query.RiskIndicatorQueryDTO;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskLevelCountVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.indicator.RiskTimePeriodCountVO;
import gang.lu.riskmanagementproject.mapper.RiskIndicatorMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.RiskIndicatorService;
import gang.lu.riskmanagementproject.util.MedicalUtil;
import gang.lu.riskmanagementproject.util.PageHelper;
import gang.lu.riskmanagementproject.util.StatisticalUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.BusinessConstants.GET_RISK_INDICATOR;
import static gang.lu.riskmanagementproject.common.FailedMessages.WORKER_NOT_EXIST;

/**
 * <p>
 * 实时风险指标表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Service
@RequiredArgsConstructor
public class RiskIndicatorServiceImpl extends ServiceImpl<RiskIndicatorMapper, RiskIndicator> implements RiskIndicatorService {

    private final RiskIndicatorMapper riskIndicatorMapper;
    private final RiskIndicatorConverter riskIndicatorConverter;
    private final GeneralValidator generalValidator;
    private final WorkerMapper workerMapper;


    /**
     * 组合分页查询
     *
     * @param queryDTO 工人id
     * @return 风险信息集合
     */
    @Override
    @BusinessLog(value = "多条件查询风险指标", recordParams = true)
    public PageVO<RiskIndicatorVO> searchRiskIndicators(RiskIndicatorQueryDTO queryDTO) {
        // 1. 校验工人存在性（仅当传了workerId时）
        if (queryDTO.getWorkerId() != null) {
            generalValidator.validateIdExist(queryDTO.getWorkerId(), workerMapper, WORKER_NOT_EXIST);
        }
        // 校验时间
        generalValidator.validateTimeRange(queryDTO.getRecordStartTime(), queryDTO.getRecordEndTime());
        // 2. 构建分页对象（自动处理默认值/最大值）
        Page<RiskIndicator> poPage = PageHelper.buildPage(queryDTO, GET_RISK_INDICATOR);
        // 3. 构建多条件查询器
        LambdaQueryWrapper<RiskIndicator> wrapper = buildRiskIndicatorQueryWrapper(queryDTO);
        // 4. 分页查询
        poPage = page(poPage, wrapper);
        // 5. 转换为通用分页VO
        return riskIndicatorConverter.pagePoToPageVO(poPage);
    }

    /**
     * 由后台算法生成，前端调用，插入风险指标信息
     *
     * @param riskIndicatorDTO 风险指标数据传输体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "新增风险指标", recordParams = true)
    public RiskIndicatorVO addRiskIndicator(RiskIndicatorDTO riskIndicatorDTO) {
        // 1. 校验工人存在性（ID格式已由@ValidId校验）
        generalValidator.validateIdExist(riskIndicatorDTO.getWorkerId(), workerMapper, WORKER_NOT_EXIST);
        // 2. 业务参数校验（医疗参数合法性）
        MedicalUtil.validateHeartRate(riskIndicatorDTO.getHeartRate());
        MedicalUtil.validateRespiratoryRate(riskIndicatorDTO.getRespiratoryRate());
        MedicalUtil.validateFatiguePercent(riskIndicatorDTO.getFatiguePercent());
        // 3. 转换DTO→PO + 设置默认值
        RiskIndicator riskIndicator = riskIndicatorConverter.dtoToPo(riskIndicatorDTO);
        riskIndicator.setRecordTime(ObjectUtil.defaultIfNull(riskIndicator.getRecordTime(), LocalDateTime.now()));
        // 4. 插入数据库并校验结果
        generalValidator.validateDbOperateResult(riskIndicatorMapper.insert(riskIndicator));
        // 5. PO→VO返回
        return riskIndicatorConverter.poToVo(riskIndicator);
    }

    /**
     * 由工人id查询最新一次的风险指标信息
     *
     * @param workerId 前端传来的工人id
     * @return RiskIndicatorVO
     */
    @Override
    @BusinessLog(value = "查询最新风险指标（工人ID）", recordParams = true)
    public RiskIndicatorVO getLatestRiskIndicatorByWorkerId(Long workerId) {
        // 校验工人存在性
        generalValidator.validateIdExist(workerId, workerMapper, WORKER_NOT_EXIST);
        // 查询最新记录
        RiskIndicator latest = riskIndicatorMapper.selectLatestByWorkerId(workerId);
        // 转换PO→VO（空值直接返回null）
        return ObjectUtil.isNull(latest) ? null : riskIndicatorConverter.poToVo(latest);
    }

    /**
     * 统计去重工人的风险等级人数分布
     *
     * @return 各种风险的人数信息集合
     */
    @Override
    @BusinessLog(value = "统计风险等级人数分布", recordParams = false)
    public RiskLevelCountVO countDistinctWorkerByRiskLevel() {
        Map<String, Map<String, Object>> riskCountMap = riskIndicatorMapper.countDistinctWorkerByRiskLevel();
        RiskLevelCountVO vo = new RiskLevelCountVO();
        vo.setLowRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, RiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, RiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, RiskLevel.HIGH_RISK.getValue()));
        vo.setVeryHighRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, RiskLevel.VERY_HIGH_RISK.getValue()));
        vo.setTotalCount(vo.getLowRiskCount() + vo.getMediumRiskCount() + vo.getHighRiskCount() + vo.getVeryHighRiskCount());
        return vo;
    }

    /**
     * 统计当日各4小时时间段高风险工人数
     *
     * @param statDate 统计日期（不传则默认当天）
     * @return 时间段统计结果
     */
    @Override
    @BusinessLog(value = "统计时间段高风险人数", recordParams = true)
    public RiskTimePeriodCountVO countHighRiskWorkerByTimePeriod(LocalDate statDate) {
        LocalDate finalStatDate = ObjectUtil.defaultIfNull(statDate, LocalDate.now());
        String statDateStr = finalStatDate.toString();
        List<Map<String, Object>> periodList = riskIndicatorMapper.countHighRiskWorkerByTimePeriod(statDateStr);
        RiskTimePeriodCountVO resultVO = new RiskTimePeriodCountVO();
        resultVO.setStatDate(statDateStr);
        resultVO.setPeriodItems(StatisticalUtil.buildTimePeriodItems(periodList));
        return resultVO;
    }

    private LambdaQueryWrapper<RiskIndicator> buildRiskIndicatorQueryWrapper(RiskIndicatorQueryDTO queryDTO) {
        LambdaQueryWrapper<RiskIndicator> wrapper = new LambdaQueryWrapper<>();

        // 1. 工人ID（精确匹配，原按工人ID查历史的核心逻辑）
        if (queryDTO.getWorkerId() != null) {
            wrapper.eq(RiskIndicator::getWorkerId, queryDTO.getWorkerId());
        }

        // 2. 风险等级（枚举转换）
        if (StrUtil.isNotBlank(queryDTO.getRiskLevelValue())) {
            RiskLevel riskLevel = riskIndicatorConverter.stringToRiskLevel(queryDTO.getRiskLevelValue());
            wrapper.eq(RiskIndicator::getRiskLevel, riskLevel);
        }

        // 3. 是否报警
        if (queryDTO.getAlertFlag() != null) {
            wrapper.eq(RiskIndicator::getAlertFlag, queryDTO.getAlertFlag());
        }

        // 4. 心率区间
        if (queryDTO.getMinHeartRate() != null) {
            wrapper.ge(RiskIndicator::getHeartRate, queryDTO.getMinHeartRate());
        }
        if (queryDTO.getMaxHeartRate() != null) {
            wrapper.le(RiskIndicator::getHeartRate, queryDTO.getMaxHeartRate());
        }

        // 5. 呼吸率区间
        if (queryDTO.getMinRespiratoryRate() != null) {
            wrapper.ge(RiskIndicator::getRespiratoryRate, queryDTO.getMinRespiratoryRate());
        }
        if (queryDTO.getMaxRespiratoryRate() != null) {
            wrapper.le(RiskIndicator::getRespiratoryRate, queryDTO.getMaxRespiratoryRate());
        }

        // 6. 疲劳百分比区间
        if (queryDTO.getMinFatiguePercent() != null) {
            wrapper.ge(RiskIndicator::getFatiguePercent, queryDTO.getMinFatiguePercent());
        }
        if (queryDTO.getMaxFatiguePercent() != null) {
            wrapper.le(RiskIndicator::getFatiguePercent, queryDTO.getMaxFatiguePercent());
        }

        // 7. 记录时间区间（前端日历选择的核心逻辑）
        if (queryDTO.getRecordStartTime() != null) {
            wrapper.ge(RiskIndicator::getRecordTime, queryDTO.getRecordStartTime());
        }
        if (queryDTO.getRecordEndTime() != null) {
            wrapper.le(RiskIndicator::getRecordTime, queryDTO.getRecordEndTime());
        }

        // 8. 排序（默认按记录时间降序，最新的在前）
        wrapper.orderByDesc(RiskIndicator::getRecordTime);

        return wrapper;
    }

}
