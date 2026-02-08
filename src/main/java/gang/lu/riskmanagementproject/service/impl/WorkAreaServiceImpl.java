package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.enums.AreaRiskLevel;
import gang.lu.riskmanagementproject.domain.po.WorkArea;
import gang.lu.riskmanagementproject.domain.vo.WorkAreaRiskCountVO;
import gang.lu.riskmanagementproject.domain.vo.WorkAreaVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkAreaMapper;
import gang.lu.riskmanagementproject.service.WorkAreaService;
import gang.lu.riskmanagementproject.util.BusinessVerifyUtil;
import gang.lu.riskmanagementproject.util.ConvertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gang.lu.riskmanagementproject.common.BusinessScene.ADD_WORK_AREA;
import static gang.lu.riskmanagementproject.common.BusinessScene.GET_WORK_AREA_BY_CODE;
import static gang.lu.riskmanagementproject.common.IdName.WORK_AREA_ID;

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

    /**
     * 新增工作区域
     *
     * @param dto 数据传输
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addWorkArea(WorkAreaDTO dto) {
// 1. 通用校验
        String areaCode = dto.getAreaCode();
        String areaName = dto.getAreaName();
        BusinessVerifyUtil.validateWorkAreaField(areaCode, "区域编码", ADD_WORK_AREA);
        BusinessVerifyUtil.validateWorkAreaField(areaName, "区域名称", ADD_WORK_AREA);

        // 2. 编码唯一性
        if (lambdaQuery().eq(WorkArea::getAreaCode, areaCode).exists()) {
            log.warn("【新增工作区域失败】编码重复：{}", areaCode);
            throw new BizException(HttpStatus.CONFLICT, FailureMessages.WORK_AREA_PARAM_DUPLICATE_CODE);
        }

        // 3. 风险等级默认值
        if (ObjectUtil.isNull(dto.getAreaRiskLevel())) {
            dto.setAreaRiskLevel(AreaRiskLevel.LOW_RISK);
            log.info("【新增工作区域】默认风险等级：低风险，编码：{}", areaCode);
        }

        // 4. 转换+保存
        WorkArea workArea = ConvertUtil.convert(dto, WorkArea.class);
        workArea.setCreateTime(LocalDateTime.now());
        workArea.setUpdateTime(LocalDateTime.now());

        boolean saveSuccess = save(workArea);
        if (!saveSuccess) {
            log.error("【新增工作区域失败】数据库异常，编码：{}", areaCode);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }

        log.info("【新增工作区域成功】编码：{}，名称：{}", areaCode, areaName);
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
    public boolean deleteWorkArea(Long id) {
        WorkArea existArea = validateWorkAreaExist(id);
        boolean deleteSuccess = removeById(id);
        if (!deleteSuccess) {
            log.error("【删除工作区域失败】数据库异常，ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }
        log.info("【删除工作区域成功】ID：{}，编码：{}", id, existArea.getAreaCode());
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
    public boolean updateWorkArea(Long id, WorkAreaDTO dto) {
        WorkArea existArea = validateWorkAreaExist(id);

        // 编码唯一性校验
        String newAreaCode = dto.getAreaCode();
        if (StrUtil.isNotBlank(newAreaCode) && !newAreaCode.equals(existArea.getAreaCode())) {
            boolean codeExists = lambdaQuery()
                    .eq(WorkArea::getAreaCode, newAreaCode)
                    .ne(WorkArea::getId, id)
                    .exists();
            if (codeExists) {
                log.warn("【修改工作区域失败】编码重复：{}", newAreaCode);
                throw new BizException(HttpStatus.CONFLICT, FailureMessages.WORK_AREA_PARAM_DUPLICATE_CODE);
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
            log.error("【修改工作区域失败】数据库异常，ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }

        log.info("【修改工作区域成功】ID：{}，编码：{}", id, existArea.getAreaCode());
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
    public WorkAreaVO getWorkAreaById(Long id) {
        WorkArea workArea = validateWorkAreaExist(id);
        log.info("【查询工作区域成功】ID：{}，编码：{}", id, workArea.getAreaCode());
        return ConvertUtil.convert(workArea, WorkAreaVO.class);
    }

    /**
     * 按区域编码查询工作区域
     *
     * @param areaCode 他的代码
     * @return 展示
     */
    @Override
    public List<WorkAreaVO> getWorkAreaByCode(String areaCode) {
        BusinessVerifyUtil.validateWorkAreaField(areaCode, "区域编码", GET_WORK_AREA_BY_CODE);

        List<WorkArea> workAreas = lambdaQuery()
                .like(WorkArea::getAreaCode, areaCode)
                .orderByDesc(WorkArea::getUpdateTime)
                .list();

        if (ObjectUtil.isEmpty(workAreas)) {
            log.info("【按编码查询工作区域】编码：{} 暂无记录", areaCode);
            return Collections.emptyList();
        }

        List<WorkAreaVO> voList = ConvertUtil.convertList(workAreas, WorkAreaVO.class);
        log.info("【按编码查询工作区域成功】编码：{}，共{}条", areaCode, voList.size());
        return voList;
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
    public Page<WorkAreaVO> queryWorkAreas(Integer pageNum, Integer pageSize, String areaName, AreaRiskLevel riskLevel) {
        // 分页参数处理
        Integer[] pageParams = BusinessVerifyUtil.handleWorkAreaPageParams(pageNum, pageSize);
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
        Page<WorkAreaVO> voPage = ConvertUtil.convertPageWithManualTotal(poPage, WorkAreaVO.class);
        log.info("【多条件分页查询工作区域】名称={}，风险等级={}，总条数={}", areaName, riskLevel, total);
        return voPage;
    }

    /**
     * 分页查询所有工作区域（无筛选条件）
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页后的所有工作区域VO列表
     */
    @Override
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
    public WorkAreaRiskCountVO countWorkAreaByRiskLevel() {
        log.info("【统计工作区域风险等级数量】开始执行");
        Map<String, Map<String, Object>> riskCountMap = workAreaMapper.countWorkAreaByRiskLevel();

        WorkAreaRiskCountVO vo = new WorkAreaRiskCountVO();
        vo.setLowRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, AreaRiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, AreaRiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, AreaRiskLevel.HIGH_RISK.getValue()));
        vo.setTotalCount(vo.getLowRiskCount() + vo.getMediumRiskCount() + vo.getHighRiskCount());

        log.info("【统计工作区域风险等级数量完成】总数量：{}", vo.getTotalCount());
        return vo;
    }

    // ========== 私有工具方法 ==========

    /**
     * 校验工作区域ID合法性+记录存在性（封装重复逻辑）
     */
    private WorkArea validateWorkAreaExist(Long id) {
        BusinessVerifyUtil.validateId(id, WORK_AREA_ID);
        WorkArea workArea = getById(id);
        if (ObjectUtil.isNull(workArea)) {
            log.warn("【工作区域操作失败】记录不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORK_AREA_DATA_NOT_EXIST);
        }
        return workArea;
    }
}
