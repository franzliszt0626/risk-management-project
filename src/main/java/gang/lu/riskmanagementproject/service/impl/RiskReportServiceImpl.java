package gang.lu.riskmanagementproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskPredictionVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.helper.PdfHelper;
import gang.lu.riskmanagementproject.mapper.RiskIndicatorMapper;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.RiskAiService;
import gang.lu.riskmanagementproject.service.RiskReportService;
import gang.lu.riskmanagementproject.converter.RiskIndicatorConverter;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.BusinessConstants.*;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RiskReportServiceImpl implements RiskReportService {

    private final RiskIndicatorMapper riskIndicatorMapper;
    private final RiskIndicatorConverter riskIndicatorConverter;
    private final WorkerMapper workerMapper;
    private final RiskAiService riskAiService;
    private final GeneralValidator generalValidator;
    private final PdfHelper pdfHelper;

    /**
     * 导出 PDF 报告。
     * <p>
     * PDF 写入 {@link HttpServletResponse} 流，AOP 无法通过返回值感知大小。
     * 因此在 {@link PdfHelper#writePdfToResponse} 调用前后手动打印字节大小。
     * 若 PdfHelper 改造为返回 {@code byte[]}，则可直接使用 AOP 的
     * {@code formatResult} 自动打印（见 {@link gang.lu.riskmanagementproject.aspect.BusinessLogAspect}）。
     */
    @Override
    @BusinessLog(
            value = LOAD_REPORT,
            recordParams = true,
            recordResult = false,
            recordRequestContext = true,
            logLevel = BusinessLog.LogLevel.INFO
    )
    public void exportPdf(Long workerId, Integer limit, Boolean includeAi,
                          HttpServletResponse response) {

        // 1. 校验工人存在
        generalValidator.validateIdExist(workerId, workerMapper, WORKER_NOT_EXIST);

        // 2. 校验 limit
        if (limit < MIN_RECORDS || limit > MAX_PDF_RECORDS) {
            throw new BizException(HttpStatus.BAD_REQUEST, PDF_LIMIT_INVALID);
        }

        // 3. 查历史数据
        List<RiskIndicatorVO> history = queryHistory(workerId, limit);
        log.info("[RiskReport] workerId={} 查到 {} 条历史记录", workerId, history.size());

        // 4. 按需调 AI（AI 原始 JSON 响应在 RiskAiServiceImpl→AiHelper 中打印）
        RiskPredictionVO prediction = resolveAiPrediction(workerId, limit, includeAi, history);

        // 5. 生成 PDF 并写入响应流，同时打印 PDF 大小
        log.info("[RiskReport] 开始生成 PDF | workerId={} | includeAi={}", workerId, includeAi);
        long before = System.currentTimeMillis();

        pdfHelper.writePdfToResponse(workerId, history, prediction, response);

        // ★ 通过 Content-Length Header 感知 PDF 大小（需 PdfHelper 设置该 Header）
        String contentLength = response.getHeader("Content-Length");
        if (contentLength != null) {
            long bytes = Long.parseLong(contentLength);
            log.info("[RiskReport] PDF 生成完成 | 大小: {} KB ({} bytes) | 耗时: {} ms",
                    bytes / 1024, bytes, System.currentTimeMillis() - before);
        } else {
            log.info("[RiskReport] PDF 生成完成 | 耗时: {} ms（如需打印大小，请在 PdfHelper 中设置 Content-Length Header）",
                    System.currentTimeMillis() - before);
        }
    }


    private List<RiskIndicatorVO> queryHistory(Long workerId, Integer limit) {
        List<RiskIndicator> poList = riskIndicatorMapper.selectList(
                new LambdaQueryWrapper<RiskIndicator>()
                        .eq(RiskIndicator::getWorkerId, workerId)
                        .orderByDesc(RiskIndicator::getCreateTime)
                        .last(LIMIT + limit)
        );
        Collections.reverse(poList);
        return poList.stream()
                .map(riskIndicatorConverter::poToVo)
                .collect(Collectors.toList());
    }

    private RiskPredictionVO resolveAiPrediction(Long workerId, Integer limit,
                                                 Boolean includeAi,
                                                 List<RiskIndicatorVO> history) {
        if (!Boolean.TRUE.equals(includeAi) || history.isEmpty()) {
            return null;
        }
        log.info("[RiskReport] 调用 AI 预测 | workerId={}", workerId);
        return riskAiService.predictRisk(workerId, limit);
    }
}