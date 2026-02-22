package gang.lu.riskmanagementproject.service;

import gang.lu.riskmanagementproject.domain.dto.AlgorithmResultDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 14:20
 * @description 接受算法数据、调用算法
 */
public interface AlgorithmService {

    /**
     * 调用算法分析视频
     *
     * @param video 前端上传的视频
     * @return AlgorithmResultDTO，算法得出的数据
     */
    AlgorithmResultDTO analyzeVideo(MultipartFile video);
}
