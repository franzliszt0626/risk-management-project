package gang.lu.riskmanagementproject.converter;

import gang.lu.riskmanagementproject.domain.dto.RiskIndicatorDTO;
import gang.lu.riskmanagementproject.domain.po.RiskIndicator;
import gang.lu.riskmanagementproject.domain.vo.normal.RiskIndicatorVO;
import gang.lu.riskmanagementproject.helper.EnumMappingHelper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import static gang.lu.riskmanagementproject.common.global.GlobalSimbolConstants.SPRING;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 01:09
 * @description 风险记录转换器
 */
@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = EnumMappingHelper.class

)
public interface RiskIndicatorConverter extends PageConverter<RiskIndicator, RiskIndicatorDTO, RiskIndicatorVO> {
}