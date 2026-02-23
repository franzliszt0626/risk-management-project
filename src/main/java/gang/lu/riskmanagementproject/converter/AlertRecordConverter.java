package gang.lu.riskmanagementproject.converter;

import gang.lu.riskmanagementproject.domain.dto.AlertRecordDTO;
import gang.lu.riskmanagementproject.domain.po.AlertRecord;
import gang.lu.riskmanagementproject.domain.vo.normal.AlertRecordVO;
import gang.lu.riskmanagementproject.helper.EnumMappingHelper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import static gang.lu.riskmanagementproject.common.global.GlobalSimbolConstants.SPRING;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 01:07
 * @description 预警记录转换器
 */
@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = EnumMappingHelper.class
)
public interface AlertRecordConverter extends PageConverter<AlertRecord, AlertRecordDTO, AlertRecordVO> {
}