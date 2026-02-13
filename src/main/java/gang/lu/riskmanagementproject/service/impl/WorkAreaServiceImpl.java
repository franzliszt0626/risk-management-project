package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.common.BusinessConstants;
import gang.lu.riskmanagementproject.converter.WorkAreaConverter;
import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.dto.query.WorkAreaQueryDTO;
import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import gang.lu.riskmanagementproject.domain.po.WorkArea;
import gang.lu.riskmanagementproject.domain.vo.normal.PageVO;
import gang.lu.riskmanagementproject.domain.vo.normal.WorkAreaVO;
import gang.lu.riskmanagementproject.domain.vo.statistical.area.WorkAreaRiskCountVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.helper.QueryWrapperHelper;
import gang.lu.riskmanagementproject.mapper.WorkAreaMapper;
import gang.lu.riskmanagementproject.service.WorkAreaService;
import gang.lu.riskmanagementproject.util.StatisticalUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.BusinessConstants.*;
import static gang.lu.riskmanagementproject.common.FailedMessages.*;

/**
 * <p>
 * 工作区域表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Service
public class WorkAreaServiceImpl
        extends BaseCrudServiceImpl<WorkArea, WorkAreaDTO, WorkAreaVO, WorkAreaQueryDTO, WorkAreaMapper, WorkAreaConverter>
        implements WorkAreaService {

    public WorkAreaServiceImpl(WorkAreaMapper baseMapper,
                               WorkAreaConverter converter,
                               GeneralValidator generalValidator) {
        super(baseMapper, converter, generalValidator);
    }

    // ======================== 通用CRUD ========================

    /**
     * 新增工作区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = ADD_WORK_AREA, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public WorkAreaVO add(WorkAreaDTO dto) {
        return super.add(dto);
    }

    /**
     * 删除工作区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = DELETE_WORK_AREA, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void delete(Long id) {
        super.delete(id);
    }

    /**
     * 批量删除工作区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = BATCH_DELETE_WORK_AREA, recordParams = true, logLevel = BusinessLog.LogLevel.WARN)
    public void batchDelete(Iterable<Long> ids) {
        super.batchDelete(ids);
    }

    /**
     * 修改工作区域
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = UPDATE_WORK_AREA, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public WorkAreaVO update(Long id, WorkAreaDTO dto) {
        return super.update(id, dto);
    }

    /**
     * 根据ID查询工作区域
     */
    @Override
    @BusinessLog(value = GET_WORK_AREA_BY_ID, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public WorkAreaVO getOneById(Long id) {
        return super.getOneById(id);
    }

    /**
     * 多条件分页查询工作区域
     */
    @Override
    @BusinessLog(value = GET_WORK_AREA_BY_MULTIPLY_CONDITION, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public PageVO<WorkAreaVO> search(WorkAreaQueryDTO queryDTO) {
        return super.search(queryDTO);
    }

    // ======================== 校验方法 ========================

    /**
     * 新增校验
     */
    @Override
    public void validateAdd(WorkAreaDTO dto) {
        String areaCode = dto.getAreaCode();
        String areaName = dto.getAreaName();
        generalValidator.validateStringNotBlank(areaCode, BusinessConstants.WORK_AREA_CODE, ADD_WORK_AREA);
        generalValidator.validateStringNotBlank(areaName, BusinessConstants.WORK_AREA_NAME, ADD_WORK_AREA);

        if (lambdaQuery().eq(WorkArea::getAreaCode, areaCode).exists()) {
            throw new BizException(HttpStatus.CONFLICT, String.format(WORK_AREA_CODE_DUPLICATE, dto.getAreaCode()));
        }
    }

    /**
     * 修改校验
     */
    @Override
    public void validateUpdate(Long id, WorkAreaDTO dto) {
        WorkArea existArea = generalValidator.validateIdExist(id, baseMapper, WORK_AREA_NOT_EXIST);
        String newAreaCode = dto.getAreaCode();

        if (StrUtil.isNotBlank(newAreaCode) && !newAreaCode.equals(existArea.getAreaCode())) {
            boolean exists = lambdaQuery()
                    .eq(WorkArea::getAreaCode, newAreaCode)
                    .ne(WorkArea::getId, id)
                    .exists();
            if (exists) {
                throw new BizException(HttpStatus.CONFLICT, String.format(WORK_AREA_CODE_DUPLICATE, UPDATE_WORK_AREA));
            }
        }
    }

    /**
     * 构建查询条件
     */
    @Override
    public LambdaQueryWrapper<WorkArea> buildQueryWrapper(WorkAreaQueryDTO queryDTO) {
        return QueryWrapperHelper.<WorkArea>create()
                .likeIfPresent(WorkArea::getAreaCode, queryDTO.getAreaCode())
                .likeIfPresent(WorkArea::getAreaName, queryDTO.getAreaName())
                .eqEnumIfPresent(WorkArea::getAreaRiskLevel, queryDTO.getAreaRiskLevelValue(), AreaRiskLevel.class)
                .likeIfPresent(WorkArea::getDescription, queryDTO.getDescription())
                .orderByDesc(WorkArea::getUpdateTime);
    }

    // ======================== 模板方法 ========================

    @Override
    protected String getNotFoundMsg() {
        return WORK_AREA_NOT_EXIST;
    }

    @Override
    protected String getBatchIdEmptyMsg() {
        return WORK_AREA_DELETE_BATCH_ID_EMPTY;
    }

    @Override
    protected String getBatchNotFoundMsg() {
        return WORK_AREA_DELETE_BATCH_ID_INVALID;
    }

    @Override
    protected String getBusinessScene() {
        return GET_WORK_AREA;
    }

    // ======================== 个性化业务 ========================

    /**
     * 根据区域编码查询工作区域
     */
    @Override
    @BusinessLog(value = GET_WORK_AREA_BY_CODE, recordParams = true, logLevel = BusinessLog.LogLevel.INFO)
    public List<WorkAreaVO> getWorkAreaByCode(String areaCode) {
        generalValidator.validateStringNotBlank(areaCode, BusinessConstants.WORK_AREA_CODE, GET_WORK_AREA_BY_CODE);

        List<WorkArea> workAreas = lambdaQuery()
                .like(WorkArea::getAreaCode, areaCode)
                .orderByDesc(WorkArea::getUpdateTime)
                .list();

        return ObjectUtil.isEmpty(workAreas)
                ? Collections.emptyList()
                : converter.poListToVoList(workAreas);
    }

    /**
     * 按风险等级统计工作区域数量
     */
    @Override
    @BusinessLog(value = GET_WORK_AREA_DISTRIBUTION_BY_RISK_LEVEL, recordParams = false, logLevel = BusinessLog.LogLevel.INFO)
    public WorkAreaRiskCountVO countWorkAreaByRiskLevel() {
        Map<String, Map<String, Object>> riskCountMap = baseMapper.countWorkAreaByRiskLevel();
        WorkAreaRiskCountVO vo = new WorkAreaRiskCountVO();
        vo.setLowRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, AreaRiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, AreaRiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, AreaRiskLevel.HIGH_RISK.getValue()));
        vo.setTotalCount(vo.getLowRiskCount() + vo.getMediumRiskCount() + vo.getHighRiskCount());
        return vo;
    }
}