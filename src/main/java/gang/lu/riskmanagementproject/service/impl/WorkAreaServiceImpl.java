package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        // 1. 通用校验：编码+名称非空
        String areaCode = dto.getAreaCode();
        String areaName = dto.getAreaName();
        BusinessVerifyUtil.validateWorkAreaCode(areaCode, "新增工作区域");
        BusinessVerifyUtil.validateWorkAreaName(areaName, "新增工作区域");
        // 2. 校验编码唯一性
        boolean codeExists = lambdaQuery().eq(WorkArea::getAreaCode, areaCode).exists();
        if (codeExists) {
            log.warn("【新增工作区域失败】区域编码重复，编码：{}", areaCode);
            throw new BizException(HttpStatus.CONFLICT, FailureMessages.WORK_AREA_PARAM_DUPLICATE_CODE);
        }
        // 3. 校验风险等级（如果传入的话）
        if (ObjectUtil.isNotNull(dto.getAreaRiskLevel())) {
            BusinessVerifyUtil.validateAreaRiskLevel(dto.getAreaRiskLevel(), "新增工作区域");
        }

        // 3. DTO转PO（处理风险等级枚举→字符串）
        WorkArea workArea = convertToPO(dto);
        // 4. 保存数据并校验结果
        boolean saveSuccess = save(workArea);
        if (!saveSuccess) {
            log.error("【新增工作区域失败】数据库保存异常，编码：{}", areaCode);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }

        log.info("【新增工作区域成功】区域编码：{}，区域名称：{}，风险等级：{}",
                areaCode, areaName, workArea.getAreaRiskLevel());
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
        // 1. 通用校验：ID合法性
        BusinessVerifyUtil.validateId(id, "工作区域ID");
        // 2. 校验记录是否存在
        WorkArea existArea = getById(id);
        if (ObjectUtil.isNull(existArea)) {
            log.warn("【删除工作区域失败】工作区域不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORK_AREA_DATA_NOT_EXIST);
        }
        // 3. 删除数据并校验结果
        boolean deleteSuccess = removeById(id);
        if (!deleteSuccess) {
            log.error("【删除工作区域失败】数据库删除异常，ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }
        log.info("【删除工作区域成功】ID：{}，区域编码：{}", id, existArea.getAreaCode());
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
        // 1. 通用校验：ID合法性
        BusinessVerifyUtil.validateId(id, "工作区域ID");
        // 2. 校验记录是否存在（修复原异常提示错误：原抛重复编码，现抛不存在）
        WorkArea existArea = getById(id);
        if (ObjectUtil.isNull(existArea)) {
            log.warn("【修改工作区域失败】工作区域不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORK_AREA_DATA_NOT_EXIST);
        }
        // 3. 若修改编码，校验新编码唯一性（排除自身）
        String newAreaCode = dto.getAreaCode();
        if (StrUtil.isNotBlank(newAreaCode) && !newAreaCode.equals(existArea.getAreaCode())) {
            boolean codeExists = lambdaQuery()
                    .eq(WorkArea::getAreaCode, newAreaCode)
                    .ne(WorkArea::getId, id)
                    .exists();
            if (codeExists) {
                log.warn("【修改工作区域失败】区域编码重复，ID：{}，新编码：{}", id, newAreaCode);
                throw new BizException(HttpStatus.CONFLICT, FailureMessages.WORK_AREA_PARAM_DUPLICATE_CODE);
            }
        }
        // 4. 可选：校验风险等级（如果传入的话）
        if (ObjectUtil.isNotNull(dto.getAreaRiskLevel())) {
            BusinessVerifyUtil.validateAreaRiskLevel(dto.getAreaRiskLevel(), "修改工作区域");
        }

        // 4. 构建更新对象（处理风险等级枚举→字符串，补充更新时间）
        WorkArea updateArea = convertToPO(dto);
        updateArea.setId(id);
        updateArea.setUpdateTime(LocalDateTime.now());
        updateArea.setCreateTime(existArea.getCreateTime());

        // 5. 更新数据并校验结果
        boolean updateSuccess = updateById(updateArea);
        if (!updateSuccess) {
            log.error("【修改工作区域失败】数据库更新异常，ID：{}", id);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, FailureMessages.COMMON_DATABASE_ERROR);
        }

        log.info("【修改工作区域成功】ID：{}，区域编码：{}，更新后名称：{}",
                id, existArea.getAreaCode(), updateArea.getAreaName());
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
        // 1. 通用校验：ID合法性
        BusinessVerifyUtil.validateId(id, "工作区域ID");

        // 2. 查询数据
        WorkArea workArea = getById(id);
        if (ObjectUtil.isNull(workArea)) {
            log.warn("【查询工作区域失败】工作区域不存在，ID：{}", id);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORK_AREA_DATA_NOT_EXIST);
        }

        log.info("【查询工作区域成功】ID：{}，区域编码：{}", id, workArea.getAreaCode());
        return convertToVO(workArea);
    }

    /**
     * 按区域编码查询工作区域
     *
     * @param areaCode 他的代码
     * @return 展示
     */
    @Override
    public List<WorkAreaVO> getWorkAreaByCode(String areaCode) {
        // 1. 通用校验：编码非空
        BusinessVerifyUtil.validateWorkAreaCode(areaCode, "按编码查询工作区域");

        // 2. 模糊查询
        List<WorkArea> workAreas = lambdaQuery()
                .like(WorkArea::getAreaCode, areaCode)
                .orderByDesc(WorkArea::getUpdateTime)
                .list();

        if (ObjectUtil.isEmpty(workAreas)) {
            log.info("【按编码查询工作区域】编码：{}，暂无匹配记录", areaCode);
            return Collections.emptyList();
        }

        List<WorkAreaVO> voList = convertToVOList(workAreas);
        log.info("【按编码查询工作区域成功】编码：{}，共查询到{}条记录", areaCode, voList.size());
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
        // 1. 通用处理：分页参数（适配工作区域规则）
        Integer[] pageParams = BusinessVerifyUtil.handleWorkAreaPageParams(pageNum, pageSize);
        int finalPageNum = pageParams[0];
        int finalPageSize = pageParams[1];

        // 2. 构建查询条件
        LambdaQueryWrapper<WorkArea> queryWrapper = buildWorkAreaQueryWrapper(areaName, riskLevel);

        // 3. 分页查询
        Page<WorkArea> page = new Page<>(finalPageNum, finalPageSize);
        Page<WorkArea> resultPage = page(page, queryWrapper);

        // 4. PO转VO分页对象
        Page<WorkAreaVO> voPage = convertToVOPage(resultPage);

        log.info("【多条件分页查询工作区域】名称模糊={}，风险等级={}，第{}页，每页{}条，总条数{}",
                areaName, ObjectUtil.isNull(riskLevel) ? "无" : riskLevel.getValue(),
                finalPageNum, finalPageSize, resultPage.getTotal());
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

        // 1. 查询原始统计数据
        Map<String, Map<String, Object>> riskCountMap = workAreaMapper.countWorkAreaByRiskLevel();
        log.info("【统计工作区域风险等级数量】原始数据：{}", riskCountMap);

        // 2. 构建统计VO（安全提取count值，默认0）
        WorkAreaRiskCountVO vo = new WorkAreaRiskCountVO();
        vo.setLowRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, AreaRiskLevel.LOW_RISK.getValue()));
        vo.setMediumRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, AreaRiskLevel.MEDIUM_RISK.getValue()));
        vo.setHighRiskCount(BusinessVerifyUtil.getCountFromMap(riskCountMap, AreaRiskLevel.HIGH_RISK.getValue()));

        // 3. 计算总数量
        int total = vo.getLowRiskCount() + vo.getMediumRiskCount() + vo.getHighRiskCount();
        vo.setTotalCount(total);

        log.info("【统计工作区域风险等级数量完成】总数量：{}，低风险：{}，中风险：{}，高风险：{}",
                total, vo.getLowRiskCount(), vo.getMediumRiskCount(), vo.getHighRiskCount());
        return vo;
    }

    // ========== 私有工具方法 ==========

    /**
     * DTO转PO（修复：直接赋值枚举，无需转换）
     */
    private WorkArea convertToPO(WorkAreaDTO dto) {
        if (ObjectUtil.isNull(dto)) {
            return null;
        }
        WorkArea workArea = BeanUtil.copyProperties(dto, WorkArea.class);
        // 数据库字段是AreaRiskLevel枚举类型，直接赋值即可
        workArea.setAreaRiskLevel(dto.getAreaRiskLevel());
        return workArea;
    }

    /**
     * PO转VO（修复：NPE防护 + 正确转换）
     */
    private WorkAreaVO convertToVO(WorkArea workArea) {
        if (ObjectUtil.isNull(workArea)) {
            return null;
        }
        WorkAreaVO vo = BeanUtil.copyProperties(workArea, WorkAreaVO.class);

        // 修复：先判断areaRiskLevel是否为null，再调用getValue
        if (ObjectUtil.isNotNull(workArea.getAreaRiskLevel())) {
            vo.setAreaRiskLevel(AreaRiskLevel.fromValue(workArea.getAreaRiskLevel().getValue()));
        }
        return vo;
    }

    /**
     * WorkArea集合转换为VO集合
     */
    private List<WorkAreaVO> convertToVOList(List<WorkArea> workAreas) {
        if (ObjectUtil.isEmpty(workAreas)) {
            return Collections.emptyList();
        }
        return workAreas.stream().map(this::convertToVO).toList();
    }

    /**
     * WorkArea分页对象转换为VO分页对象
     */
    private Page<WorkAreaVO> convertToVOPage(Page<WorkArea> poPage) {
        Page<WorkAreaVO> voPage = new Page<>();
        BeanUtil.copyProperties(poPage, voPage);
        voPage.setRecords(convertToVOList(poPage.getRecords()));
        return voPage;
    }

    /**
     * 构建工作区域查询条件
     */
    private LambdaQueryWrapper<WorkArea> buildWorkAreaQueryWrapper(String areaName, AreaRiskLevel areaRiskLevel) {
        LambdaQueryWrapper<WorkArea> queryWrapper = new LambdaQueryWrapper<>();
        // 名称模糊查询
        if (StrUtil.isNotBlank(areaName)) {
            queryWrapper.like(WorkArea::getAreaName, areaName.trim());
        }
        // 修复：数据库字段是枚举类型，直接用枚举作为查询条件
        if (ObjectUtil.isNotNull(areaRiskLevel)) {
            queryWrapper.eq(WorkArea::getAreaRiskLevel, areaRiskLevel);
        }
        // 按更新时间降序
        queryWrapper.orderByDesc(WorkArea::getUpdateTime);
        return queryWrapper;
    }
}
