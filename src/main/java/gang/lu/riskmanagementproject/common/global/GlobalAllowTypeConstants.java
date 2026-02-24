package gang.lu.riskmanagementproject.common.global;

import java.util.Arrays;
import java.util.List;

/**
 * 全局允许的文件类型常量
 * <p>
 * 统一管理各业务模块对上传文件 MIME 类型的白名单限制。
 *
 * <p><b>分区说明：</b>
 * <ol>
 *   <li>视频文件类型</li>
 * </ol>
 *
 * @author Franz Liszt
 * @since 2026-02-23
 */
public interface GlobalAllowTypeConstants {

    // ==============================1. 视频文件类型================================

    String       VIDEO_MP_4    = "video/mp4";
    String       VIDEO         = "video";
    List<String> ALLOWED_TYPES = Arrays.asList(
            "video/mp4", "video/avi", "video/quicktime", "video/x-msvideo"
    );
}