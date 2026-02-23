package gang.lu.riskmanagementproject.service;

import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 14:28
 * @description 分析视频服务
 */
public interface VideoAnalysisService {
    /**
     * 返回算法数据给前端
     *
     * @param workerCode 对应的工号
     * @param video    视频
     * @return 算法分析的数据
     */
    RiskIndicatorVO analyzeAndSave(String workerCode, MultipartFile video);
}
