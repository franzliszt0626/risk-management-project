package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.converter.RiskIndicatorConverter;
import gang.lu.riskmanagementproject.domain.dto.AlgorithmResultDTO;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import gang.lu.riskmanagementproject.domain.enums.field.RiskLevel;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.RiskIndicatorMapper;
import gang.lu.riskmanagementproject.service.AlgorithmService;
import gang.lu.riskmanagementproject.service.VideoAnalysisService;
import gang.lu.riskmanagementproject.service.WorkerService;
import gang.lu.riskmanagementproject.validator.VideoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static gang.lu.riskmanagementproject.common.global.GlobalBusinessConstants.VIDEO_SAVE;
import static gang.lu.riskmanagementproject.common.global.GlobalLogConstants.LOG_UNKNOWN_RISK_LEVEL;
import static gang.lu.riskmanagementproject.common.global.GlobalLogConstants.LOG_VIDEO_SAVE;
import static gang.lu.riskmanagementproject.message.FailedMessages.WORKER_NOT_EXIST_BY_CODE;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoAnalysisServiceImpl implements VideoAnalysisService {

    private final AlgorithmService algorithmService;
    private final RiskIndicatorMapper riskIndicatorMapper;
    private final RiskIndicatorConverter riskIndicatorConverter;
    private final VideoValidator videoValidator;
    private final WorkerService workerService;

    /**
     * 视频分析并持久化结果。
     * <p>
     * {@code recordResult = true}：AOP 打印保存后的 {@link RiskIndicatorVO}（含数据库时间戳）。
     * 算法原始 JSON 响应已在 {@link AlgorithmServiceImpl#analyzeVideo} 内打印。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(
            value = VIDEO_SAVE,
            recordParams = true,
            recordResult = true,
            logLevel = BusinessLog.LogLevel.INFO
    )
    public RiskIndicatorVO analyzeAndSave(String workerCode, MultipartFile video) {
        // 1. 校验工人存在
        Worker existingWorker = workerService.getWorkerByCodeWithOutVerify(workerCode);
        // 如果工人不存在，抛异常
        if (ObjectUtil.isNull(existingWorker)) {
            throw new BizException(HttpStatus.NOT_FOUND, WORKER_NOT_EXIST_BY_CODE);
        }
        // 2. 如果存在则校验视频文件
        videoValidator.validateVideoFile(video);

        // 3. 调用算法服务（内部已打印原始响应）
        AlgorithmResultDTO result = algorithmService.analyzeVideo(video);

        // 4. 构建 PO 并入库
        Long workerId = existingWorker.getId();
        RiskIndicator indicator = buildIndicator(workerId, result);
        riskIndicatorMapper.insert(indicator);

        // 5. 回查，获取完整记录（含 DB 生成的时间戳）
        RiskIndicator saved = riskIndicatorMapper.selectById(indicator.getId());
        log.info(LOG_VIDEO_SAVE, workerId, saved.getRiskLevel());

        return riskIndicatorConverter.poToVo(saved);
    }


    private RiskIndicator buildIndicator(Long workerId, AlgorithmResultDTO result) {
        RiskLevel riskLevel = RiskLevel.LOW_RISK;
        if (ObjectUtil.isNotNull(result)) {
            RiskLevel parsed = ValueEnum.fromValue(RiskLevel.class, result.getRiskLevel());
            if (ObjectUtil.isNotNull(parsed)) {
                riskLevel = parsed;
            } else {
                log.warn(LOG_UNKNOWN_RISK_LEVEL,
                        result.getRiskLevel());
            }
        }
        return new RiskIndicator()
                .setWorkerId(workerId)
                .setHeartRate(result.getHeartRate())
                .setRespiratoryRate(result.getRespiratoryRate())
                .setFatiguePercent(result.getFatiguePercent())
                .setRiskLevel(riskLevel)
                .setAlertFlag(Boolean.TRUE.equals(result.getAlertFlag()));
    }
}