package gang.lu.riskmanagementproject.converter;

import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import gang.lu.riskmanagementproject.domain.enums.WorkType;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkerVO;
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
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkerConverter extends PageConverter<Worker, WorkerDTO, WorkerVO> {

    /**
     * String转Status（MapStruct自动调用）
     *
     * @param value 前端传来的字符串
     * @return 序列化的枚举值
     */
    default Status stringToStatus(String value) {
        return ValueEnum.fromValue(Status.class, value);
    }


    /**
     * String转Status（MapStruct自动调用）
     *
     * @param status 状态
     * @return 字符串
     */
    default String statusToString(Status status) {
        return status == null ? null : status.getValue();
    }

    /**
     * String转WorkType（MapStruct自动调用）
     *
     * @param value 前端传来的字符串
     * @return 序列化的枚举值
     */
    default WorkType stringToWorkType(String value) {
        return ValueEnum.fromValue(WorkType.class, value);
    }


    /**
     * String转workType（MapStruct自动调用）
     *
     * @param workType 工种
     * @return 字符串
     */
    default String workTypeToString(WorkType workType) {
        return workType == null ? null : workType.getValue();
    }
}
