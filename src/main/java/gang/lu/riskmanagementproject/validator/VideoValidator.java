package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static gang.lu.riskmanagementproject.common.global.GlobalAllowTypeConstants.ALLOWED_TYPES;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 视频文件校验器
 * <p>
 * 对上传的视频文件做三层校验：非空、文件大小、内容类型（MIME）。
 * 合法类型列表由ALLOWED_TYPES统一维护。
 *
 * @author Franz Liszt
 * @since 2026-02-22
 */
@Component
public class VideoValidator {

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024L;
    private static final long BYTES_PER_MB = 1024L * 1024L;

    /**
     * 校验视频文件合法性，依次执行以下三层校验：
     * <ol>
     *   <li>文件不能为空；</li>
     *   <li>文件大小不能超过50 MB；</li>
     *   <li>Content-Type 必须在 ALLOWED_TYPES中。</li>
     * </ol>
     *
     * @param video 上传的视频文件
     * @throws BizException 任意一层校验不通过时抛出 400 异常
     */
    public void validateVideoFile(MultipartFile video) {
        if (video == null || video.isEmpty()) {
            throw new BizException(HttpStatus.BAD_REQUEST, VIDEO_EMPTY);
        }
        if (video.getSize() > MAX_FILE_SIZE) {
            // ★ 原代码直接传 bytes，单位显示有误；此处换算为 MB（保留一位小数）
            String sizeMb = String.format("%.1f", (double) video.getSize() / BYTES_PER_MB);
            throw new BizException(HttpStatus.BAD_REQUEST, String.format(VIDEO_SIZE_INVALID, sizeMb));
        }
        String contentType = video.getContentType();
        if (ObjectUtil.isNull(contentType) || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new BizException(HttpStatus.BAD_REQUEST, VIDEO_TYPE_INVALID);
        }
    }
}