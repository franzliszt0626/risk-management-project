package gang.lu.riskmanagementproject.converter;


import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/8 16:47
 * @description MapStruct转换接口，空值不覆盖目标属性，避免NPE
 */
public interface ClassConverter<PO, DTO, VO> {

    /**
     * DTO转换为PO
     * @param dto dto
     * @return po
     */
    PO dtoToPo(DTO dto);

    /**
     * PO转换为VO
     * @param po po
     * @return vo
     */
    VO poToVo(PO po);

    /**
     * 批量PO转换为VO
     * @param poList po集合
     * @return vo集合
     */
    List<VO> poListToVoList(List<PO> poList);

    /**
     * 批量DTO转换为PO
     * @param dtoList dto集合
     * @return po集合
     */
    List<PO> dtoListToPoList(List<DTO> dtoList);


    /**
     * 用DTO更新PO（空值不覆盖）
     * @param dto dto
     * @param po po
     */
    void updatePoFromDto(DTO dto, @MappingTarget PO po);


    /**
     * 用PO更新DTO（空值不覆盖）
     * @param po po
     * @param dto dto
     */
    void updateDtoFromPo(PO po, @MappingTarget DTO dto);
}