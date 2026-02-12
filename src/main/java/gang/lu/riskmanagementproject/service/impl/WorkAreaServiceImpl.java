package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import gang.lu.riskmanagementproject.mapper.WorkAreaMapper;
import gang.lu.riskmanagementproject.service.WorkAreaService;
import gang.lu.riskmanagementproject.util.PageHelper;
import gang.lu.riskmanagementproject.util.StatisticalUtil;
import gang.lu.riskmanagementproject.validator.GeneralValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.BusinessConstants.GET_WORK_AREA;
import static gang.lu.riskmanagementproject.common.FailedMessages.WORK_AREA_CODE_DUPLICATE;
import static gang.lu.riskmanagementproject.common.FailedMessages.WORK_AREA_NOT_EXIST;

/**
 * <p>
 * 工作区域表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkAreaServiceImpl extends ServiceImpl<WorkAreaMapper, WorkArea> implements WorkAreaService {

    private final WorkAreaMapper workAreaMapper;
    private final GeneralValidator generalValidator;
    private final WorkAreaConverter workAreaConverter;

    /**
     * 新增工作区域
     *
     * @param dto 数据传输
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "新增工作区域", recordParams = true)
    public boolean addWorkArea(WorkAreaDTO dto) {
        // 1. 通用校验
        String areaCode = dto.getAreaCode();
        String areaName = dto.getAreaName();
        generalValidator.validateStringNotBlank(areaCode, BusinessConstants.WORK_AREA_CODE, BusinessConstants.ADD_WORK_AREA);
        generalValidator.validateStringNotBlank(areaName, BusinessConstants.WORK_AREA_NAME, BusinessConstants.ADD_WORK_AREA);
        // 2. 编码唯一性校验
        if (lambdaQuery().eq(WorkArea::getAreaCode, areaCode).exists()) {
            throw new BizException(HttpStatus.CONFLICT, WORK_AREA_CODE_DUPLICATE);
        }
        // 4. 转换+保存
        WorkArea workArea = workAreaConverter.dtoToPo(dto);
        workArea.setCreateTime(LocalDateTime.now());
        workArea.setUpdateTime(LocalDateTime.now());
        // 5. 插入数据库并校验结果
        boolean saveSuccess = save(workArea);
        generalValidator.validateDbOperateResult(saveSuccess ? 1 : 0);
        return true;
    }

    /**
     * 删除工作区域（按ID）
     *
     * @param id 根据id删除
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "删除工作区域", recordParams = true)
    public boolean deleteWorkArea(Long id) {
        generalValidator.validateIdExist(id, workAreaMapper, WORK_AREA_NOT_EXIST);
        boolean deleteSuccess = removeById(id);
        generalValidator.validateDbOperateResult(deleteSuccess ? 1 : 0);
        return true;
    }

    /**
     * 修改工作区域
     *
     * @param id  根据id更新
     * @param dto 数据传输
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @BusinessLog(value = "修改工作区域", recordParams = true)
    public boolean updateWorkArea(Long id, WorkAreaDTO dto) {
        // 1. 校验工作区域存在性
        WorkArea existArea = generalValidator.validateIdExist(id, workAreaMapper, WORK_AREA_NOT_EXIST);
        // 编码唯一性校验
        String newAreaCode = dto.getAreaCode();
        if (StrUtil.isNotBlank(newAreaCode) && !newAreaCode.equals(existArea.getAreaCode())) {
            boolean codeExists = lambdaQuery()
                    .eq(WorkArea::getAreaCode, newAreaCode)
                    .ne(WorkArea::getId, id)
                    .exists();
            if (codeExists) {
                throw new BizException(HttpStatus.CONFLICT, WORK_AREA_CODE_DUPLICATE);
            }
        }
        // 4. 用DTO更新PO（空值不覆盖）
        WorkArea updateArea = new WorkArea();
        workAreaConverter.updatePoFromDto(dto, updateArea);
        updateArea.setId(id);
        updateArea.setCreateTime(existArea.getCreateTime());
        updateArea.setUpdateTime(LocalDateTime.now());
        // 5. 更新数据库并校验结果
        boolean updateSuccess = updateById(updateArea);
        generalValidator.validateDbOperateResult(updateSuccess ? 1 : 0);
        return true;
    }


    /**
     * 据id精确查询工作区域信息
     *
     * @param id 他的id
     * @return 展示
     * @author Franz Liszt
     *
     */
    @Override
    @BusinessLog(value = "查询工作区域（ID）", recordParams = true)
    public WorkAreaVO getWorkAreaById(Long id) {
        // 1. 校验工作区域存在性
        WorkArea workArea = generalValidator.validateIdExist(id, workAreaMapper, WORK_AREA_NOT_EXIST);
        // 2. PO→VO转换
        return workAreaConverter.poToVo(workArea);
    }

    /**
     * 按区域编码查询工作区域
     *
     * @param areaCode 他的代码
     * @return 展示
     */
    @Override
    @BusinessLog(value = "按编码查询工作区域", recordParams = true)
    public List<WorkAreaVO> getWorkAreaByCode(String areaCode) {
        // 1. 字段非空校验
        generalValidator.validateStringNotBlank(areaCode, BusinessConstants.WORK_AREA_CODE, BusinessConstants.GET_WORK_AREA_BY_CODE);
        // 2. 模糊查询
        List<WorkArea> workAreas = lambdaQuery()
                .like(WorkArea::getAreaCode, areaCode)
                .orderByDesc(WorkArea::getUpdateTime)
                .list();
        // 3. 批量PO→VO转换（空值返回空列表）
        return ObjectUtil.isEmpty(workAreas)
                ? Collections.emptyList()
                : workAreaConverter.poListToVoList(workAreas);
    }

    /**
     * 多条件分页查询工作区域
     *
     * @param queryDTO 分页传输
     * @return 分页结果
     */
    @Override
    @BusinessLog(value = "多条件查询工作区域", recordParams = true)
    public PageVO<WorkAreaVO> searchWorkAreas(WorkAreaQueryDTO queryDTO) {
        // 1. 构建分页对象
        Page<WorkArea> poPage = PageHelper.buildPage(queryDTO, GET_WORK_AREA);
        // 2. 构建通用多条件查询wrapper
        LambdaQueryWrapper<WorkArea> wrapper = buildWorkAreaQueryWrapper(queryDTO);
        // 3. 分页查询
        poPage = page(poPage, wrapper);
        // 4. 转换为VO分页对象
        return workAreaConverter.pagePoToPageVO(poPage);
    }

    /**
     * 按风险等级统计工作区域数量
     *
     * @return 风险等级划分的统计数据
     */
    @Override
    @BusinessLog(value = "统计工作区域风险等级数量", recordParams = false)
    public WorkAreaRiskCountVO countWorkAreaByRiskLevel() {
        Map<String, Map<String, Object>> riskCountMap = workAreaMapper.countWorkAreaByRiskLevel();

        WorkAreaRiskCountVO vo = new WorkAreaRiskCountVO();
        vo.setLowRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, AreaRiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, AreaRiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(StatisticalUtil.getCountFromMap(riskCountMap, AreaRiskLevel.HIGH_RISK.getValue()));
        vo.setTotalCount(vo.getLowRiskCount() + vo.getMediumRiskCount() + vo.getHighRiskCount());
        return vo;
    }

    private LambdaQueryWrapper<WorkArea> buildWorkAreaQueryWrapper(WorkAreaQueryDTO queryDTO) {
        LambdaQueryWrapper<WorkArea> wrapper = new LambdaQueryWrapper<>();

        // 2. 区域编码（模糊查询）
        if (StrUtil.isNotBlank(queryDTO.getAreaCode())) {
            wrapper.like(WorkArea::getAreaCode, queryDTO.getAreaCode().trim());
        }

        // 3. 区域名称（模糊查询）
        if (StrUtil.isNotBlank(queryDTO.getAreaName())) {
            wrapper.like(WorkArea::getAreaName, queryDTO.getAreaName().trim());
        }

        // 4. 风险等级（String转枚举）
        if (StrUtil.isNotBlank(queryDTO.getAreaRiskLevelValue())) {
            AreaRiskLevel riskLevel = workAreaConverter.stringToAreaAlertLevel(queryDTO.getAreaRiskLevelValue());
            wrapper.eq(WorkArea::getAreaRiskLevel, riskLevel);
        }

        // 5. 描述（模糊查询）
        if (StrUtil.isNotBlank(queryDTO.getDescription())) {
            wrapper.like(WorkArea::getDescription, queryDTO.getDescription().trim());
        }

        // 6. 排序（默认按更新时间降序）
        wrapper.orderByDesc(WorkArea::getUpdateTime);

        return wrapper;
    }
}
