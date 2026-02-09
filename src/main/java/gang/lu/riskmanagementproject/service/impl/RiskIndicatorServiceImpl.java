package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.RiskLevelCountVO;
import gang.lu.riskmanagementproject.domain.vo.RiskTimePeriodCountVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.RiskIndicatorMapper;
import gang.lu.riskmanagementproject.service.RiskIndicatorService;
import gang.lu.riskmanagementproject.util.BasicUtil;
import gang.lu.riskmanagementproject.util.ConvertUtil;
import gang.lu.riskmanagementproject.util.MedicalUtil;
import gang.lu.riskmanagementproject.validator.RiskValidator;
import gang.lu.riskmanagementproject.validator.WorkerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.BusinessScene.*;
import static gang.lu.riskmanagementproject.util.BasicUtil.buildTimePeriodItems;

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

    private final WorkerValidator workerValidator;

    private final RiskValidator riskValidator;


    /**
     * 由后台算法生成，前端调用，插入风险指标信息
     *
     * @param riskIndicatorDTO 风险指标数据传输体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "新增风险指标", recordParams = true)
    public RiskIndicatorVO addRiskIndicator(RiskIndicatorDTO riskIndicatorDTO) {
        // 1. 校验工人存在性
        Long workerId = riskIndicatorDTO.getWorkerId();
        workerValidator.validateWorkerExist(workerId, ADD_RISK_INDICATOR);

        // 2. 参数校验
        MedicalUtil.validateHeartRate(riskIndicatorDTO.getHeartRate());
        MedicalUtil.validateRespiratoryRate(riskIndicatorDTO.getRespiratoryRate());
        MedicalUtil.validateFatiguePercent(riskIndicatorDTO.getFatiguePercent());

        // 3. 风险等级校验
        riskValidator.validateRiskLevel(riskIndicatorDTO.getRiskLevel(), ADD_RISK_INDICATOR);

        // 4. 转换+默认值
        RiskIndicator riskIndicator = ConvertUtil.convert(riskIndicatorDTO, RiskIndicator.class);
        if (ObjectUtil.isNull(riskIndicator.getRecordTime())) {
            riskIndicator.setRecordTime(java.time.LocalDateTime.now());
        }
        // 5. 插入
        int inserted = riskIndicatorMapper.insert(riskIndicator);
        if (inserted != 1) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.RISK_OPERATE_CREATE_ERROR);
        }
        return ConvertUtil.convert(riskIndicator, RiskIndicatorVO.class);
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
        workerValidator.validateWorkerExist(workerId, GET_LATEST_RISK_INDICATOR);
        RiskIndicator latest = riskIndicatorMapper.selectLatestByWorkerId(workerId);
        if (ObjectUtil.isNull(latest)) {
            return null;
        }
        return ConvertUtil.convert(latest, RiskIndicatorVO.class);
    }

    /**
     * 由工人id查询他历史所有的风险指标信息，按照时间先后，新的在前
     *
     * @param workerId 工人id
     * @return 风险信息集合
     */
    @Override
    @BusinessLog(value = "查询历史风险指标（工人ID）", recordParams = true)
    public List<RiskIndicatorVO> getRiskIndicatorsByWorkerId(Long workerId, Integer pageNum, Integer pageSize) {
        workerValidator.validateWorkerExist(workerId, GET_HISTORY_RISK_INDICATOR);
        // 复用通用分页参数
        Integer[] pageParams = BasicUtil.handleCustomPageParams(pageNum, pageSize, GET_HISTORY_RISK_INDICATOR_BY_WORKER_ID);
        IPage<RiskIndicator> page = lambdaQuery()
                .eq(RiskIndicator::getWorkerId, workerId)
                .orderByDesc(RiskIndicator::getRecordTime)
                .page(new Page<>(pageParams[0], pageParams[1]));

        if (ObjectUtil.isEmpty(page.getRecords())) {
            return Collections.emptyList();
        }
        return ConvertUtil.convertList(page.getRecords(), RiskIndicatorVO.class);
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
        vo.setLowRiskCount(BasicUtil.getCountFromMap(riskCountMap, RiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(BasicUtil.getCountFromMap(riskCountMap, RiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(BasicUtil.getCountFromMap(riskCountMap, RiskLevel.HIGH_RISK.getValue()));
        vo.setVeryHighRiskCount(BasicUtil.getCountFromMap(riskCountMap, RiskLevel.VERY_HIGH_RISK.getValue()));
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
        resultVO.setPeriodItems(buildTimePeriodItems(periodList));
        return resultVO;
    }

}
