package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.RiskIndicatorVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.RiskIndicatorMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.RiskIndicatorService;
import gang.lu.riskmanagementproject.util.ParameterVerifyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static gang.lu.riskmanagementproject.common.FailureMessages.*;

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
}
