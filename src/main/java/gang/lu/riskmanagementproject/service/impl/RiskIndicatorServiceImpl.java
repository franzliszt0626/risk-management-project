package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import gang.lu.riskmanagementproject.util.BusinessVerifyUtil;
import gang.lu.riskmanagementproject.util.ConvertUtil;
import gang.lu.riskmanagementproject.util.ParameterVerifyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.BusinessScene.*;

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
@Slf4j
public class RiskIndicatorServiceImpl extends ServiceImpl<RiskIndicatorMapper, RiskIndicator> implements RiskIndicatorService {

    private final RiskIndicatorMapper riskIndicatorMapper;


    /**
     * 由后台算法生成，前端调用，插入风险指标信息
     *
     * @param riskIndicatorDTO 风险指标数据传输体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RiskIndicatorVO addRiskIndicator(RiskIndicatorDTO riskIndicatorDTO) {
        // 1. 校验工人存在性
        Long workerId = riskIndicatorDTO.getWorkerId();
        BusinessVerifyUtil.validateWorkerExist(workerId, ADD_RISK_INDICATOR);

        // 2. 参数校验
        ParameterVerifyUtil.validateHeartRate(riskIndicatorDTO.getHeartRate());
        ParameterVerifyUtil.validateRespiratoryRate(riskIndicatorDTO.getRespiratoryRate());
        ParameterVerifyUtil.validateFatiguePercent(riskIndicatorDTO.getFatiguePercent());

        // 3. 风险等级校验
        BusinessVerifyUtil.validateRiskLevel(riskIndicatorDTO.getRiskLevel(), ADD_RISK_INDICATOR);

        // 4. 转换+默认值
        RiskIndicator riskIndicator = ConvertUtil.convert(riskIndicatorDTO, RiskIndicator.class);
        if (ObjectUtil.isNull(riskIndicator.getRecordTime())) {
            riskIndicator.setRecordTime(java.time.LocalDateTime.now());
            log.info("【新增风险指标】工人ID：{}，自动填充记录时间", workerId);
        }

        // 5. 插入
        int inserted = riskIndicatorMapper.insert(riskIndicator);
        if (inserted != 1) {
            log.error("【新增风险指标失败】数据库插入异常，工人ID：{}", workerId);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.RISK_OPERATE_CREATE_ERROR);
        }

        log.info("【新增风险指标成功】工人ID：{}，风险等级：{}", workerId, riskIndicator.getRiskLevel().getValue());
        return ConvertUtil.convert(riskIndicator, RiskIndicatorVO.class);
    }

    /**
     * 由工人id查询最新一次的风险指标信息
     *
     * @param workerId 前端传来的工人id
     * @return RiskIndicatorVO
     */
    @Override
    public RiskIndicatorVO getLatestRiskIndicatorByWorkerId(Long workerId) {
        BusinessVerifyUtil.validateWorkerExist(workerId, GET_LATEST_RISK_INDICATOR);

        RiskIndicator latest = riskIndicatorMapper.selectLatestByWorkerId(workerId);
        if (ObjectUtil.isNull(latest)) {
            log.info("【查询最新风险指标】工人ID：{} 暂无记录", workerId);
            return null;
        }

        RiskIndicatorVO vo = ConvertUtil.convert(latest, RiskIndicatorVO.class);
        log.info("【查询最新风险指标成功】工人ID：{}", workerId);
        return vo;
    }

    /**
     * 由工人id查询他历史所有的风险指标信息，按照时间先后，新的在前
     *
     * @param workerId 工人id
     * @return 风险信息集合
     */
    @Override
    public List<RiskIndicatorVO> getRiskIndicatorsByWorkerId(Long workerId, Integer pageNum, Integer pageSize) {
        BusinessVerifyUtil.validateWorkerExist(workerId, GET_HISTORY_RISK_INDICATOR);

        // 复用通用分页参数
        Integer[] pageParams = BusinessVerifyUtil.handleCommonPageParams(pageNum, pageSize);
        IPage<RiskIndicator> page = lambdaQuery()
                .eq(RiskIndicator::getWorkerId, workerId)
                .orderByDesc(RiskIndicator::getRecordTime)
                .page(new Page<>(pageParams[0], pageParams[1]));

        if (ObjectUtil.isEmpty(page.getRecords())) {
            log.info("【查询历史风险指标】工人ID：{} 暂无记录", workerId);
            return Collections.emptyList();
        }

        List<RiskIndicatorVO> voList = ConvertUtil.convertList(page.getRecords(), RiskIndicatorVO.class);
        log.info("【查询历史风险指标成功】工人ID：{}，本页{}条", workerId, voList.size());
        return voList;
    }

    /**
     * 统计去重工人的风险等级人数分布
     *
     * @return 各种风险的人数信息集合
     */
    @Override
    public RiskLevelCountVO countDistinctWorkerByRiskLevel() {
        log.info("【统计风险等级人数分布】开始执行");
        Map<String, Map<String, Object>> riskCountMap = riskIndicatorMapper.countDistinctWorkerByRiskLevel();

        RiskLevelCountVO vo = new RiskLevelCountVO();
        vo.setLowRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, RiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, RiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, RiskLevel.HIGH_RISK.getValue()));
        vo.setVeryHighRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, RiskLevel.VERY_HIGH_RISK.getValue()));
        vo.setTotalCount(vo.getLowRiskCount() + vo.getMediumRiskCount() + vo.getHighRiskCount() + vo.getVeryHighRiskCount());

        log.info("【统计风险等级人数分布完成】总人数：{}", vo.getTotalCount());
        return vo;
    }

    /**
     * 统计当日各4小时时间段高风险工人数
     *
     * @param statDate 统计日期（不传则默认当天）
     * @return 时间段统计结果
     */
    @Override
    public RiskTimePeriodCountVO countHighRiskWorkerByTimePeriod(LocalDate statDate) {
        LocalDate finalStatDate = ObjectUtil.defaultIfNull(statDate, LocalDate.now());
        String statDateStr = finalStatDate.toString();
        log.info("【统计时间段高风险人数】开始执行，日期：{}", statDateStr);

        List<Map<String, Object>> periodList = riskIndicatorMapper.countHighRiskWorkerByTimePeriod(statDateStr);
        RiskTimePeriodCountVO resultVO = new RiskTimePeriodCountVO();
        resultVO.setStatDate(statDateStr);
        resultVO.setPeriodItems(buildTimePeriodItems(periodList));

        log.info("【统计时间段高风险人数完成】日期：{}，共{}个时间段", statDateStr, resultVO.getPeriodItems().size());
        return resultVO;
    }


    // ========== 私有工具方法：消除重复代码 ==========

    /**
     * 构建时间段统计Item
     */
    private List<RiskTimePeriodCountVO.TimePeriodItem> buildTimePeriodItems(List<Map<String, Object>> periodList) {
        List<RiskTimePeriodCountVO.TimePeriodItem> itemList = new ArrayList<>();
        for (Map<String, Object> itemMap : periodList) {
            Integer period = BusinessVerifyUtil.safeLongToInt((Long) itemMap.get("period"), -1);
            Integer count = BusinessVerifyUtil.safeLongToInt((Long) itemMap.get("count"), 0);

            RiskTimePeriodCountVO.TimePeriodItem item = new RiskTimePeriodCountVO.TimePeriodItem();
            item.setHighRiskCount(count);
            item.setPeriodDesc(BusinessVerifyUtil.getPeriodDesc(period));
            itemList.add(item);
        }
        return itemList;
    }
}
