package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.ObjectUtil;
import gang.lu.riskmanagementproject.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static gang.lu.riskmanagementproject.common.BusinessConstants.ALLOWED_TYPES;
import static gang.lu.riskmanagementproject.common.BusinessConstants.MAX_FILE_SIZE;
import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22 14:37
 */
@Component
public class VideoValidator {

    public void validateVideoFile(MultipartFile video) {
        if (video == null || video.isEmpty()) {
            throw new BizException(HttpStatus.BAD_REQUEST, VIDEO_EMPTY);
        }
        if (video.getSize() > MAX_FILE_SIZE) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    String.format(VIDEO_SIZE_INVALID, video.getSize()));
        }
        String contentType = video.getContentType();
        if (ObjectUtil.isNull(contentType) || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new BizException(HttpStatus.BAD_REQUEST,
                    VIDEO_TYPE_INVALID);
        }
    }
}
