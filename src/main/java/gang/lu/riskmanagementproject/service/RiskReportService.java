package gang.lu.riskmanagementproject.service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 15:55
 * @description 生成风险分析PDF
 */
public interface RiskReportService {

    /**
     * 生成并输出PDF到响应流
     *
     * @param workerId  id
     * @param limit     选取条数
     * @param includeAi 是否包含ai内容
     * @param response  响应体
     */
    void exportPdf(Long workerId, Integer limit, Boolean includeAi, HttpServletResponse response);
}
