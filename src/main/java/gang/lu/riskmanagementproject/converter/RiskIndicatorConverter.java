package gang.lu.riskmanagementproject.converter;

import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.util.EnumMappingHelper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 01:09
 * @description 风险记录转换器
 */
@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = EnumMappingHelper.class

)
public interface RiskIndicatorConverter extends PageConverter<RiskIndicator, RiskIndicatorDTO, RiskIndicatorVO> {

    /**
     * String转riskLevel（MapStruct自动调用）
     *
     * @param value 前端传来的字符串
     * @return 序列化的枚举值
     */
    default RiskLevel stringToRiskLevel(String value) {
        return ValueEnum.fromValue(RiskLevel.class, value);
    }


    /**
     * String转riskLevel（MapStruct自动调用）
     *
     * @param riskLevel 风险等级
     * @return 字符串
     */
    default String RiskLevelToString(RiskLevel riskLevel) {
        return riskLevel == null ? null : riskLevel.getValue();
    }
}