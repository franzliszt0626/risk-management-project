package gang.lu.riskmanagementproject.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 12:54
 * @description 基本工具类
 */
@Component
@Slf4j
public class BasicUtil {

    private BasicUtil() {
    }


    /**
     * 安全转换Long为Integer（优化默认值处理）
     */
    public static Integer safeLongToInt(Long value, Integer defaultValue) {
        if (ObjectUtil.isNull(value)) {
            return ObjectUtil.defaultIfNull(defaultValue, 0);
        }
        // 防止溢出
        if (value > Integer.MAX_VALUE) {
            log.warn("Long值{}超过Integer最大值，转换为{}", value, Integer.MAX_VALUE);
            return Integer.MAX_VALUE;
        }
        return value.intValue();
    }


}
