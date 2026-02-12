package gang.lu.riskmanagementproject.converter;

import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
import gang.lu.riskmanagementproject.helper.EnumMappingHelper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 01:06
 * @description 工人转换器
 */
@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring", // 生成Spring Bean
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = EnumMappingHelper.class
)
public interface WorkerConverter extends PageConverter<Worker, WorkerDTO, WorkerVO> {
}
