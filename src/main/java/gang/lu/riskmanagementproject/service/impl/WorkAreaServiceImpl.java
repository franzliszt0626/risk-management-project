package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import gang.lu.riskmanagementproject.domain.po.WorkArea;
import gang.lu.riskmanagementproject.domain.vo.WorkAreaRiskCountVO;
import gang.lu.riskmanagementproject.domain.vo.WorkAreaVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkAreaMapper;
import gang.lu.riskmanagementproject.service.WorkAreaService;
import gang.lu.riskmanagementproject.util.BasicUtil;
import gang.lu.riskmanagementproject.util.ConvertUtil;
import gang.lu.riskmanagementproject.validator.WorkAreaValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.BusinessScene.*;
import static gang.lu.riskmanagementproject.common.FieldName.WORK_AREA_CODE;
import static gang.lu.riskmanagementproject.common.FieldName.WORK_AREA_NAME;
import static gang.lu.riskmanagementproject.util.BasicUtil.handleCustomPageParams;

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

    private final WorkAreaValidator workAreaValidator;

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
        workAreaValidator.validateWorkAreaField(areaCode, WORK_AREA_CODE, ADD_WORK_AREA);
        workAreaValidator.validateWorkAreaField(areaName, WORK_AREA_NAME, ADD_WORK_AREA);
        // 2. 编码唯一性
        if (lambdaQuery().eq(WorkArea::getAreaCode, areaCode).exists()) {
            throw new BizException(HttpStatus.CONFLICT, FailureMessages.WORK_AREA_CODE_DUPLICATE);
        }
        // 3. 风险等级默认值
        if (ObjectUtil.isNull(dto.getAreaRiskLevel())) {
            dto.setAreaRiskLevel(AreaRiskLevel.LOW_RISK);
        }
        // 4. 转换+保存
        WorkArea workArea = ConvertUtil.convert(dto, WorkArea.class);
        workArea.setCreateTime(LocalDateTime.now());
        workArea.setUpdateTime(LocalDateTime.now());

        boolean saveSuccess = save(workArea);
        if (!saveSuccess) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }
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
        workAreaValidator.validateWorkAreaExist(id);
        boolean deleteSuccess = removeById(id);
        if (!deleteSuccess) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }
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
        WorkArea existArea = workAreaValidator.validateWorkAreaExist(id);

        // 编码唯一性校验
        String newAreaCode = dto.getAreaCode();
        if (StrUtil.isNotBlank(newAreaCode) && !newAreaCode.equals(existArea.getAreaCode())) {
            boolean codeExists = lambdaQuery()
                    .eq(WorkArea::getAreaCode, newAreaCode)
                    .ne(WorkArea::getId, id)
                    .exists();
            if (codeExists) {
                throw new BizException(HttpStatus.CONFLICT, FailureMessages.WORK_AREA_CODE_DUPLICATE);
            }
        }
        // 风险等级默认值
        if (ObjectUtil.isNull(dto.getAreaRiskLevel())) {
            dto.setAreaRiskLevel(existArea.getAreaRiskLevel());
        }

        // 转换+更新
        WorkArea updateArea = ConvertUtil.convert(dto, WorkArea.class);
        updateArea.setId(id);
        updateArea.setCreateTime(existArea.getCreateTime());
        updateArea.setUpdateTime(LocalDateTime.now());

        boolean updateSuccess = updateById(updateArea);
        if (!updateSuccess) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }
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
        WorkArea workArea = workAreaValidator.validateWorkAreaExist(id);
        return ConvertUtil.convert(workArea, WorkAreaVO.class);
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
        workAreaValidator.validateWorkAreaField(areaCode, WORK_AREA_CODE, GET_WORK_AREA_BY_CODE);
        List<WorkArea> workAreas = lambdaQuery()
                .like(WorkArea::getAreaCode, areaCode)
                .orderByDesc(WorkArea::getUpdateTime)
                .list();
        if (ObjectUtil.isEmpty(workAreas)) {
            return Collections.emptyList();
        }
        return ConvertUtil.convertList(workAreas, WorkAreaVO.class);
    }

    /**
     * 多条件分页查询工作区域
     *
     * @param pageNum   页码
     * @param pageSize  每页条数
     * @param areaName  区域名称（模糊）
     * @param riskLevel 风险等级（枚举）
     * @return 分页结果
     */
    @Override
    @BusinessLog(value = "多条件分页查询工作区域", recordParams = true)
    public Page<WorkAreaVO> queryWorkAreas(Integer pageNum, Integer pageSize, String areaName, AreaRiskLevel riskLevel) {
        // 分页参数处理
        Integer[] pageParams = handleCustomPageParams(pageNum, pageSize, GET_WORK_AREA);
        ;
        int finalPageNum = pageParams[0];
        int finalPageSize = pageParams[1];

        // 构建查询条件
        LambdaQueryWrapper<WorkArea> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(areaName)) {
            queryWrapper.like(WorkArea::getAreaName, areaName.trim());
        }
        if (ObjectUtil.isNotNull(riskLevel)) {
            queryWrapper.eq(WorkArea::getAreaRiskLevel, riskLevel);
        }
        queryWrapper.orderByDesc(WorkArea::getUpdateTime);

        // 分页查询
        long total = count(queryWrapper);
        Page<WorkArea> poPage = new Page<>(finalPageNum, finalPageSize);
        poPage = page(poPage, queryWrapper);
        poPage.setTotal(total);
        poPage.setPages(total > 0 ? (total + finalPageSize - 1) / finalPageSize : 0);

        // 转换分页VO
        return ConvertUtil.convertPageWithManualTotal(poPage, WorkAreaVO.class);
    }

    /**
     * 分页查询所有工作区域（无筛选条件）
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页后的所有工作区域VO列表
     */
    @Override
    @BusinessLog(value = "分页查询所有工作区域", recordParams = true)
    public Page<WorkAreaVO> getAllWorkAreas(Integer pageNum, Integer pageSize) {
        // 复用多条件查询逻辑（无筛选条件）
        return queryWorkAreas(pageNum, pageSize, null, null);
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
        vo.setLowRiskCount(BasicUtil.getCountFromMap(riskCountMap, AreaRiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(BasicUtil.getCountFromMap(riskCountMap, AreaRiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(BasicUtil.getCountFromMap(riskCountMap, AreaRiskLevel.HIGH_RISK.getValue()));
        vo.setTotalCount(vo.getLowRiskCount() + vo.getMediumRiskCount() + vo.getHighRiskCount());
        return vo;
    }

}
