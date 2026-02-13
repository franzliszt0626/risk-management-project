package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.converter.PageConverter;
import gang.lu.riskmanagementproject.domain.dto.query.PageQueryDTO;
import gang.lu.riskmanagementproject.domain.po.BasePO;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.helper.PageHelper;
import gang.lu.riskmanagementproject.service.BaseCrudService;
import gang.lu.riskmanagementproject.util.BasicUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * 通用CRUD服务实现（模板方法）
 *
 * @param <PO>        持久层实体
 * @param <DTO>       数据传输对象
 * @param <VO>        视图对象
 * @param <Q>         查询DTO
 * @param <Mapper>    Mapper类型
 * @param <Converter> 转换器类型（继承PageConverter）
 * @author Franz Liszt
 */
@RequiredArgsConstructor
public abstract class BaseCrudServiceImpl<
        PO extends BasePO,
        DTO,
        VO,
        Q extends PageQueryDTO,
        Mapper extends BaseMapper<PO>,
        Converter extends PageConverter<PO, DTO, VO>>
        extends ServiceImpl<Mapper, PO> implements BaseCrudService<PO, DTO, VO, Q> {

    protected final Mapper baseMapper;
    protected final Converter converter;
    protected final GeneralValidator generalValidator;


    @Override
    public VO add(DTO dto) {
        // 1. 业务特有校验（子类实现）
        this.validateAdd(dto);
        // 2. DTO转PO
        PO po = converter.dtoToPo(dto);
        // 4. 插入数据库
        baseMapper.insert(po);
        generalValidator.validateDbOperateResult(1);
        // 5. PO转VO返回
        return converter.poToVo(po);
    }

    @Override
    public void delete(Long id) {
        // 1. 业务特有校验
        this.validateDelete(id);
        // 2. 通用存在性校验
        generalValidator.validateIdExist(id, baseMapper, getNotFoundMsg());
        // 3. 删除
        baseMapper.deleteById(id);
        generalValidator.validateDbOperateResult(1);
    }

    @Override
    public void batchDelete(Iterable<Long> ids) {
        // 1. 业务特有校验
        this.validateBatchDelete(ids);
        // 2. 安全转换（无强转警告）
        List<Long> idList = BasicUtil.safeConvertToList(ids);
        // 3. 通用空校验
        if (CollUtil.isEmpty(idList)) {
            throw new BizException(HttpStatus.BAD_REQUEST, getBatchIdEmptyMsg());
        }
        // 4. 通用批量存在性校验（无强转）
        generalValidator.validateBatchIdsExist(idList, baseMapper, getBatchNotFoundMsg());
        // 5. 批量删除
        int affectedRows = baseMapper.deleteBatchIds(idList);
        generalValidator.validateBatchDbOperateResult(affectedRows, idList.size());
    }

    @Override
    public VO update(Long id, DTO dto) {
        // 1. 业务特有校验
        this.validateUpdate(id, dto);
        // 2. 通用存在性校验
        generalValidator.validateIdExist(id, baseMapper, getNotFoundMsg());
        // 3. DTO更新PO（空值不覆盖）
        PO updatePo = converter.dtoToPo(dto);
        converter.updatePoFromDto(dto, updatePo);
        updatePo.setId(id);
        // 5. 更新数据库
        baseMapper.updateById(updatePo);
        generalValidator.validateDbOperateResult(1);
        // 6. 返回最新VO
        PO updated = baseMapper.selectById(id);
        return converter.poToVo(updated);
    }

    @Override
    public VO getOneById(Long id) {
        PO po = generalValidator.validateIdExist(id, baseMapper, getNotFoundMsg());
        return converter.poToVo(po);
    }

    @Override
    public PageVO<VO> search(Q queryDTO) {
        // 1. 业务特有校验（例如时间范围校验）
        this.validateSearch(queryDTO);
        // 2. 通用分页对象构建
        Page<PO> poPage = PageHelper.buildPage(queryDTO, getBusinessScene());
        // 3. 子类构建查询条件
        LambdaQueryWrapper<PO> wrapper = buildQueryWrapper(queryDTO);
        // 4. 通用分页查询
        poPage = this.page(poPage, wrapper);
        // 5. 通用PO分页转VO分页
        return converter.pagePoToPageVO(poPage);
    }


    // ======================== 子类必须实现的抽象方法（差异化文案） ========================

    /**
     * 获取「不存在」提示文案（子类实现）
     *
     * @return 未找到的信息
     */
    protected abstract String getNotFoundMsg();

    /**
     * 获取「批量ID空」提示文案（子类实现）
     *
     * @return 未找到的信息
     */
    protected abstract String getBatchIdEmptyMsg();

    /**
     * 获取「批量不存在」提示文案（子类实现）
     *
     * @return 未找到的信息
     */
    protected abstract String getBatchNotFoundMsg();

    /**
     * 获取业务场景标识（用于PageHelper）
     *
     * @return 业务信息
     */
    protected abstract String getBusinessScene();
}

