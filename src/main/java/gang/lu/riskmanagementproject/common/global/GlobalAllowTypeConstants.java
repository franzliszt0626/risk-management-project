package gang.lu.riskmanagementproject.common.global;

import java.util.Arrays;
import java.util.List;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/23 14:32
 * @description 允许的文件类型
 */
public interface GlobalAllowTypeConstants {

    /**
     * 视频文件限制
     */
    String VIDEO_MP_4 = "video/mp4";
    String VIDEO = "video";
    List<String> ALLOWED_TYPES = Arrays.asList(
            "video/mp4", "video/avi", "video/quicktime", "video/x-msvideo"
    );
}
