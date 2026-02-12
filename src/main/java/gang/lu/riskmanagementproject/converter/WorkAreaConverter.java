package gang.lu.riskmanagementproject.converter;

import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import gang.lu.riskmanagementproject.domain.enums.ValueEnum;
import gang.lu.riskmanagementproject.domain.po.WorkArea;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkAreaVO;
import gang.lu.riskmanagementproject.util.EnumMappingHelper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 01:08
 * @description 工作区域转换器
 */

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = EnumMappingHelper.class
)
public interface WorkAreaConverter extends PageConverter<WorkArea, WorkAreaDTO, WorkAreaVO> {

    /**
     * String转areaRiskLevel（MapStruct自动调用）
     *
     * @param value 前端传来的字符串
     * @return 序列化的枚举值
     */
    default AreaRiskLevel stringToAreaAlertLevel(String value) {
        return ValueEnum.fromValue(AreaRiskLevel.class, value);
    }


    /**
     * String转areaRiskLevel（MapStruct自动调用）
     *
     * @param areaRiskLevel 风险等级
     * @return 字符串
     */
    default String areaAlertLevelToString(AreaRiskLevel areaRiskLevel) {
        return areaRiskLevel == null ? null : areaRiskLevel.getValue();
    }

}