package gang.lu.riskmanagementproject.converter;

import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;
import gang.lu.riskmanagementproject.util.EnumMappingHelper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 01:07
 * @description 预警记录转换器
 */
@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = EnumMappingHelper.class
)
public interface AlertRecordConverter extends PageConverter<AlertRecord, AlertRecordDTO, AlertRecordVO> {

    /**
     * String转AlertLevel（MapStruct自动调用）
     *
     * @param value 前端传来的字符串
     * @return 序列化的枚举值
     */
    default AlertLevel stringToAlertLevel(String value) {
        return ValueEnum.fromValue(AlertLevel.class, value);
    }


    /**
     * String转AlertLevel（MapStruct自动调用）
     *
     * @param alertLevel 风险等级
     * @return 字符串
     */
    default String alertLevelToString(AlertLevel alertLevel) {
        return alertLevel == null ? null : alertLevel.getValue();
    }

}