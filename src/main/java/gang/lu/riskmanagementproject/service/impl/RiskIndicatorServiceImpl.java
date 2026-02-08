package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import gang.lu.riskmanagementproject.util.ParameterVerifyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        // 1、得到工人id，但是不能是工人表中没有的
        Long workerId = riskIndicatorDTO.getWorkerId();
        // 校验工人ID+工人存在性
        BusinessVerifyUtil.validateWorker(workerId, "新增风险指标");
        // 2、一系列的参数校验
        ParameterVerifyUtil.validateHeartRate(riskIndicatorDTO.getHeartRate());
        ParameterVerifyUtil.validateRespiratoryRate(riskIndicatorDTO.getRespiratoryRate());
        ParameterVerifyUtil.validateFatiguePercent(riskIndicatorDTO.getFatiguePercent());

        // 3. 通用校验：风险等级合法性
        BusinessVerifyUtil.validateRiskLevel(riskIndicatorDTO.getRiskLevel(), "新增风险指标");
        // 4. 补充recordTime默认值（未传则取当前时间）
        RiskIndicator riskIndicator = BeanUtil.copyProperties(riskIndicatorDTO, RiskIndicator.class);
        if (ObjectUtil.isNull(riskIndicator.getRecordTime())) {
            riskIndicator.setRecordTime(java.time.LocalDateTime.now());
            log.info("【新增风险指标】工人ID：{}，自动填充记录时间为当前时间：{}", workerId, riskIndicator.getRecordTime());
        }

        // 5. 插入数据并校验结果
        int inserted = riskIndicatorMapper.insert(riskIndicator);
        if (inserted != 1) {
            log.error("【新增风险指标失败】数据库插入异常，工人ID：{}，影响行数：{}", workerId, inserted);
            throw new BizException(FailureMessages.RISK_OPERATE_CREATE_ERROR);
        }

        log.info("【新增风险指标成功】工人ID：{}，风险指标记录ID：{}，风险等级：{}",
                workerId, riskIndicator.getId(), riskIndicator.getRiskLevel().getValue());
        return convertToVO(riskIndicator);
    }

    /**
     * 由工人id查询最新一次的风险指标信息
     *
     * @param workerId 前端传来的工人id
     * @return RiskIndicatorVO
     */
    @Override
    public RiskIndicatorVO getLatestRiskIndicatorByWorkerId(Long workerId) {
        // 复用通用工具：校验工人ID+工人存在性
        BusinessVerifyUtil.validateWorker(workerId, "查询最新风险指标");

        // 执行查询
        RiskIndicator latestRiskIndicator = riskIndicatorMapper.selectLatestByWorkerId(workerId);
        if (ObjectUtil.isNull(latestRiskIndicator)) {
            log.info("【查询最新风险指标】工人ID：{} 暂无最新记录", workerId);
            throw new BizException(String.format(FailureMessages.RISK_INDICATOR_NO_LATEST, workerId));
        }

        RiskIndicatorVO vo = BeanUtil.copyProperties(latestRiskIndicator, RiskIndicatorVO.class);
        log.info("【查询最新风险指标成功】工人ID：{}，记录时间：{}", workerId, vo.getRecordTime());
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
        // 复用通用工具：校验工人ID+工人存在性
        BusinessVerifyUtil.validateWorker(workerId, "查询历史风险指标");

        // 复用通用工具：处理分页参数
        Integer[] pageParams = BusinessVerifyUtil.handlePageParams(pageNum, pageSize);
        int finalPageNum = pageParams[0];
        int finalPageSize = pageParams[1];

        // 分页查询
        log.info("【查询历史风险指标】工人ID：{}，分页：第{}页，每页{}条", workerId, finalPageNum, finalPageSize);
        IPage<RiskIndicator> page = lambdaQuery()
                .eq(RiskIndicator::getWorkerId, workerId)
                .orderByDesc(RiskIndicator::getRecordTime)
                .page(new Page<>(finalPageNum, finalPageSize));

        List<RiskIndicator> riskIndicators = page.getRecords();
        if (ObjectUtil.isEmpty(riskIndicators)) {
            log.info("【查询历史风险指标】工人ID：{} 暂无历史记录", workerId);
            return Collections.emptyList();
        }

        // 4. 转换为VO集合
        List<RiskIndicatorVO> voList = convertToVOList(riskIndicators);
        log.info("【查询历史风险指标成功】工人ID：{}，共查询到{}条历史记录（本页{}条）",
                workerId, page.getTotal(), voList.size());
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
        log.info("【统计风险等级人数分布】原始数据：{}", riskCountMap);

        // 复用通用工具：安全提取count值
        RiskLevelCountVO vo = new RiskLevelCountVO();
        vo.setLowRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, RiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, RiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, RiskLevel.HIGH_RISK.getValue()));
        vo.setVeryHighRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, RiskLevel.VERY_HIGH_RISK.getValue()));

        // 计算总人数
        int total = vo.getLowRiskCount() + vo.getMediumRiskCount() + vo.getHighRiskCount() + vo.getVeryHighRiskCount();
        vo.setTotalCount(total);

        log.info("【统计风险等级人数分布完成】总人数：{}，低风险：{}，中风险：{}，高风险：{}，严重风险：{}",
                total, vo.getLowRiskCount(), vo.getMediumRiskCount(), vo.getHighRiskCount(), vo.getVeryHighRiskCount());
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
// 1. 处理统计日期（默认当天，格式标准化）
        LocalDate finalStatDate = ObjectUtil.defaultIfNull(statDate, LocalDate.now());
        String statDateStr = finalStatDate.toString();
        log.info("【统计时间段高风险人数】开始执行，统计日期：{}", statDateStr);

        // 2. 查询时间段统计数据
        List<Map<String, Object>> periodList = riskIndicatorMapper.countHighRiskWorkerByTimePeriod(statDateStr);
        log.info("【统计时间段高风险人数】原始时间段数据：{}", periodList);

        // 3. 构建返回VO
        RiskTimePeriodCountVO resultVO = new RiskTimePeriodCountVO();
        resultVO.setStatDate(statDateStr);
        resultVO.setPeriodItems(buildTimePeriodItems(periodList));

        log.info("【统计时间段高风险人数完成】日期：{}，共统计{}个时间段数据", statDateStr, resultVO.getPeriodItems().size());
        return resultVO;
    }


    // ========== 私有工具方法：消除重复代码 ==========

    /**
     * 单个RiskIndicator转换为VO
     */
    private RiskIndicatorVO convertToVO(RiskIndicator riskIndicator) {
        if (ObjectUtil.isNull(riskIndicator)) {
            return null;
        }
        return BeanUtil.copyProperties(riskIndicator, RiskIndicatorVO.class);
    }

    /**
     * RiskIndicator集合转换为VO集合
     */
    private List<RiskIndicatorVO> convertToVOList(List<RiskIndicator> riskIndicators) {
        if (ObjectUtil.isEmpty(riskIndicators)) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(riskIndicators, RiskIndicatorVO.class);
    }

    /**
     * 构建时间段统计Item（抽取重复逻辑）
     */
    private List<RiskTimePeriodCountVO.TimePeriodItem> buildTimePeriodItems(List<Map<String, Object>> periodList) {
        List<RiskTimePeriodCountVO.TimePeriodItem> itemList = new ArrayList<>();

        for (Map<String, Object> itemMap : periodList) {
            // 安全转换类型（避免空指针/类型转换异常）
            Integer period = BusinessVerifyUtil.safeLongToInt((Long) itemMap.get("period"), -1);
            Integer count = BusinessVerifyUtil.safeLongToInt((Long) itemMap.get("count"), 0);

            RiskTimePeriodCountVO.TimePeriodItem item = new RiskTimePeriodCountVO.TimePeriodItem();
            item.setHighRiskCount(count);
            item.setPeriodDesc(getPeriodDesc(period));

            itemList.add(item);
        }

        return itemList;
    }

    /**
     * 获取时间段描述（抽取switch逻辑）
     */
    private String getPeriodDesc(Integer period) {
        return switch (period) {
            case 0 -> "00:00-04:00";
            case 4 -> "04:00-08:00";
            case 8 -> "08:00-12:00";
            case 12 -> "12:00-16:00";
            case 16 -> "16:00-20:00";
            case 20 -> "20:00-24:00";
            default -> "未知时间段";
        };
    }
}
