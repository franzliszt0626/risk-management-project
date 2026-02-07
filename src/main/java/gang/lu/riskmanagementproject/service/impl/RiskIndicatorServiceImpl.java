package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.RiskLevelCountVO;
import gang.lu.riskmanagementproject.domain.vo.RiskTimePeriodCountVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.RiskIndicatorMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.RiskIndicatorService;
import gang.lu.riskmanagementproject.util.ParameterVerifyUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static gang.lu.riskmanagementproject.common.FailureMessages.*;
import static gang.lu.riskmanagementproject.util.StatisticUtil.getCountFromMap;

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

    private final WorkerMapper workerMapper;

    /**
     * 由后台算法生成，前端调用，插入风险指标信息
     *
     * @param riskIndicatorDTO 风险指标数据传输体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRiskIndicator(RiskIndicatorDTO riskIndicatorDTO) {
        // 得到工人id，但是不能是工人表中没有的
        Long workerId = riskIndicatorDTO.getWorkerId();
        if (ObjectUtil.isNull(workerId) || ObjectUtil.isNull(workerMapper.selectById(workerId))) {
            throw new BizException(WORKER_NOT_EXISTING_ERROR_MESSAGE);
        }
        // 一系列的参数校验
        ParameterVerifyUtil.validateHeartRate(riskIndicatorDTO.getHeartRate());
        ParameterVerifyUtil.validateRespiratoryRate(riskIndicatorDTO.getRespiratoryRate());
        ParameterVerifyUtil.validateFatiguePercent(riskIndicatorDTO.getFatiguePercent());

        // 得到当前风险等级
        RiskLevel riskLevel = riskIndicatorDTO.getRiskLevel();
        if (ObjectUtil.isNull(riskLevel)) {
            throw new BizException(NON_RISK_LEVEL_ERROR_MESSAGE);
        }
        // 插入数据
        RiskIndicator riskIndicator = new RiskIndicator();
        BeanUtils.copyProperties(riskIndicatorDTO, riskIndicator);
        int inserted = riskIndicatorMapper.insert(riskIndicator);
        if (inserted != 1) {
            throw new BizException(CREATE_RISK_INDICATOR_ERROR_MESSAGE);
        }
    }

    /**
     * 由工人id查询最新一次的风险指标信息
     *
     * @param workerId 前端传来的工人id
     * @return RiskIndicatorVO
     */
    @Override
    public RiskIndicatorVO getLatestRiskIndicatorByWorkerId(Long workerId) {
        // 如果工人的id为空，传出工人不存在的信息
        if (ObjectUtil.isNull(workerId)) {
            throw new BizException(WORKER_NOT_EXISTING_ERROR_MESSAGE);
        }
        // 执行查询
        RiskIndicator latestRiskIndicator = riskIndicatorMapper.selectLatestByWorkerId(workerId);
        // 转为VO
        if (ObjectUtil.isNull(latestRiskIndicator)) {
            throw new BizException(LATEST_RISK_INDICATOR_NOT_EXISTING_ERROR_MESSAGE);
        }
        return BeanUtil.copyProperties(latestRiskIndicator, RiskIndicatorVO.class);
    }

    /**
     * 由工人id查询他历史所有的风险指标信息，按照时间先后，新的在前
     *
     * @param workerId 工人id
     * @return 风险信息集合
     */
    @Override
    public List<RiskIndicatorVO> getRiskIndicatorsByWorkerId(Long workerId) {
        // 如果为空
        if (ObjectUtil.isNull(workerId)) {
            log.warn("查询风险指标失败：工人ID为空");
            throw new BizException(WORKER_ID_NOT_EXISTING_ERROR_MESSAGE);
        }
        List<RiskIndicator> riskIndicators = lambdaQuery().eq(RiskIndicator::getWorkerId, workerId)
                .orderByDesc(RiskIndicator::getRecordTime)
                .list();
        // 如果查到为null，说明记录不存在
        if (CollectionUtil.isEmpty(riskIndicators)) {
            log.info("工人ID={}未查询到历史风险指标记录", workerId);
            return Collections.emptyList();
        }
        log.info("工人ID={}查询到{}条历史风险指标记录", workerId, riskIndicators.size());
        // 转VO集合
        return BeanUtil.copyToList(riskIndicators, RiskIndicatorVO.class);
    }

    /**
     * 统计去重工人的风险等级人数分布
     *
     * @return 各种风险的人数信息集合
     */
    @Override
    public RiskLevelCountVO countDistinctWorkerByRiskLevel() {
        // 1. 调用Mapper获取分组统计结果
        Map<String, Map<String, Object>> riskCountMap = riskIndicatorMapper.countDistinctWorkerByRiskLevel();
        log.info("风险等级人数统计原始结果：{}", riskCountMap);

        // 2. 初始化VO并赋值（默认0，避免空指针）
        RiskLevelCountVO vo = new RiskLevelCountVO();
        // 提取风险人数：子Map.get("count")得到Object，转成Integer
        vo.setLowRiskCount(getCountFromMap(riskCountMap, "低风险"));
        vo.setMediumRiskCount(getCountFromMap(riskCountMap, "中风险"));
        vo.setHighRiskCount(getCountFromMap(riskCountMap, "高风险"));
        vo.setVeryHighRiskCount(getCountFromMap(riskCountMap, "严重风险"));

        // 3. 计算总人数
        int total = vo.getLowRiskCount() + vo.getMediumRiskCount() +
                vo.getHighRiskCount() + vo.getVeryHighRiskCount();
        vo.setTotalCount(total);

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
        // 1. 处理日期，不传默认当天
        if (ObjectUtil.isNull(statDate)) {
            statDate = LocalDate.now();
        }
        Date statDateDate = Date.from(statDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String statDateStr = DateUtil.format(statDateDate, "yyyy-MM-dd");
        log.info("开始统计{}各4小时时间段高风险工人数", statDateStr);

        // 2. 调用Mapper（返回List<Map>，包含6个时间段的完整数据）
        List<Map<String, Object>> periodList = riskIndicatorMapper.countHighRiskWorkerByTimePeriod(statDateStr);
        log.info("时间段统计原始结果：{}", periodList);

        // 3. 构建VO
        RiskTimePeriodCountVO resultVO = new RiskTimePeriodCountVO();
        resultVO.setStatDate(statDateStr);
        List<RiskTimePeriodCountVO.TimePeriodItem> itemList = new ArrayList<>();

        // 4. 遍历返回的6条数据（顺序固定：0/4/8/12/16/20）
        for (Map<String, Object> itemMap : periodList) {
            // 修正1：先转Long再转Integer（避免直接强转）
            Long periodLong = (Long) itemMap.get("period");
            Integer period = periodLong.intValue();

            // 修正2：count同样先转Long再转Integer（或直接toString后解析）
            Long countLong = (Long) itemMap.get("count");
            Integer count = countLong.intValue();

            RiskTimePeriodCountVO.TimePeriodItem item = getTimePeriodItem(period, count);
            itemList.add(item);
        }

        resultVO.setPeriodItems(itemList);
        log.info("{}时间段高风险工人数统计完成：{}", statDateStr, resultVO);
        return resultVO;
    }

    private static RiskTimePeriodCountVO.@NonNull TimePeriodItem getTimePeriodItem(Integer period, Integer count) {
        RiskTimePeriodCountVO.TimePeriodItem item = new RiskTimePeriodCountVO.TimePeriodItem();
        // 根据period匹配时间段描述
        switch (period) {
            case 0:
                item.setPeriodDesc("00:00-04:00");
                break;
            case 4:
                item.setPeriodDesc("04:00-08:00");
                break;
            case 8:
                item.setPeriodDesc("08:00-12:00");
                break;
            case 12:
                item.setPeriodDesc("12:00-16:00");
                break;
            case 16:
                item.setPeriodDesc("16:00-20:00");
                break;
            case 20:
                item.setPeriodDesc("20:00-24:00");
                break;
            default:
                item.setPeriodDesc("未知时间段");
        }
        item.setHighRiskCount(count);
        return item;
    }

}
