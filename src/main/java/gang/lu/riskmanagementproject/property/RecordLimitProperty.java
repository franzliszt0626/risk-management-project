package gang.lu.riskmanagementproject.property;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/23 15:00
 * @description pdf/ai分析记录限制
 */
@Data
@Component
public class RecordLimitProperty {

    /**
     * 最小记录条数
     */
    private int MIN_RECORDS = 1;

    /**
     * 最大记录条数
     */
    private int MAX_RECORDS = 100;
}
