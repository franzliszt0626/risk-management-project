package gang.lu.riskmanagementproject.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import gang.lu.riskmanagementproject.domain.dto.query.PageQueryDTO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;

/**
 * 通用CRUD服务接口
 *
 * @param <PO>  持久层实体
 * @param <DTO> 数据传输对象
 * @param <VO>  视图对象
 * @param <Q>   查询DTO（继承PageQueryDTO）
 * @author Franz Liszt
 */
public interface BaseCrudService<PO, DTO, VO, Q extends PageQueryDTO> extends IService<PO> {

    // ======================== 通用CRUD接口 ========================


    /**
     * 新增
     *
     * @param dto DTO
     * @return VO
     */
    VO add(DTO dto);


    /**
     * 单条删除
     *
     * @param id 根据id删
     */
    void delete(Long id);

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    void batchDelete(Iterable<Long> ids);

    /**
     * 修改
     *
     * @param id  根据id更新
     * @param dto dto视图
     * @return VO视图
     */
    VO update(Long id, DTO dto);

    /**
     * 单条查询
     *
     * @param id 根据id精确查询
     * @return VO
     */
    VO getOneById(Long id);

    /**
     * 分页组合查询
     *
     * @param queryDTO 查询dto
     * @return 分页视图
     */
    PageVO<VO> search(Q queryDTO);

    // ======================== 子类需实现的差异化方法 ========================


    /**
     * 构建分页查询条件（子类实现）
     *
     * @param queryDTO 分页dto
     * @return LambdaQueryWrapper
     */
    LambdaQueryWrapper<PO> buildQueryWrapper(Q queryDTO);


    /**
     * 新增前的业务校验（子类实现，默认空实现）
     *
     * @param dto 视图
     */
    default void validateAdd(DTO dto) {
    }


    /**
     * 修改前的业务校验（子类实现，默认空实现）
     *
     * @param id  ID
     * @param dto 视图
     */
    default void validateUpdate(Long id, DTO dto) {
    }


    /**
     * 删除前的业务校验（子类实现，默认空实现）
     *
     * @param id ID
     */
    default void validateDelete(Long id) {
    }

    /**
     * 批量删除前的业务校验（子类实现，默认空实现）
     *
     * @param ids id集合
     */
    default void validateBatchDelete(Iterable<Long> ids) {
    }

    /**
     * 查询前的业务校验（子类实现，默认空实现）
     * 例如：时间范围校验等
     *
     * @param queryDTO 查询DTO
     *
     */
    default void validateSearch(Q queryDTO) {

    }
}