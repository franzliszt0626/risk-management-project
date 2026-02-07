package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.domain.dto.WorkAreaDTO;
import gang.lu.riskmanagementproject.domain.enums.RiskLevel;
import gang.lu.riskmanagementproject.domain.po.WorkArea;
import gang.lu.riskmanagementproject.domain.vo.WorkAreaVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkAreaMapper;
import gang.lu.riskmanagementproject.service.WorkAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static gang.lu.riskmanagementproject.common.FailureMessages.*;

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
public class WorkAreaServiceImpl extends ServiceImpl<WorkAreaMapper, WorkArea> implements WorkAreaService {


    /**
     * 新增工作区域
     *
     * @param dto 数据传输
     * @return 是否成功
     */
    @Override
    public boolean addWorkArea(WorkAreaDTO dto) {
        // 1. 参数校验
        if (ObjectUtil.isNull(dto) || ObjectUtil.isEmpty(dto.getAreaCode())) {
            throw new BizException(EMPTY_WORK_AREA_CODE_ERROR_MESSAGE);
        }
        // 校验编码唯一性
        if (lambdaQuery().eq(WorkArea::getAreaCode, dto.getAreaCode()).exists()) {
            throw new BizException(DUPLICATE_WORK_AREA_CODE_ERROR_MESSAGE);
        }

        // 2. DTO转PO
        WorkArea workArea = BeanUtil.copyProperties(dto, WorkArea.class);
        // 枚举值转换（RiskLevel → String）
        if (ObjectUtil.isNotNull(dto.getRiskLevel())) {
            workArea.setRiskLevel(dto.getRiskLevel().getValue());
        }

        // 3. 保存数据
        boolean save = save(workArea);
        log.info("新增工作区域：{}，结果：{}", dto.getAreaCode(), save);
        return save;
    }

    /**
     * 删除工作区域（按ID）
     *
     * @param id 根据id删除
     * @return 是否成功
     */
    @Override
    public boolean deleteWorkArea(Long id) {
        if (ObjectUtil.isNull(id)) {
            throw new BizException(EMPTY_WORK_AREA_ID_ERROR_MESSAGE);
        }
        boolean remove = removeById(id);
        log.info("删除工作区域ID：{}，结果：{}", id, remove);
        return remove;
    }

    /**
     * 修改工作区域
     *
     * @param id  根据id更新
     * @param dto 数据传输
     * @return 是否成功
     */
    @Override
    public boolean updateWorkArea(Long id, WorkAreaDTO dto) {
        // 1. 参数校验
        if (ObjectUtil.isNull(id) || ObjectUtil.isNull(dto)) {
            throw new BizException(MISSING_KEY_WORK_AREA_PARAMETER_ERROR_MESSAGE);
        }
        // 校验是否存在
        WorkArea existArea = getById(id);
        if (ObjectUtil.isNull(existArea)) {
            throw new BizException(MISSING_WORK_AREA_ERROR_MESSAGE);
        }

        // 2. 构建更新对象
        WorkArea updateArea = BeanUtil.copyProperties(dto, WorkArea.class);
        updateArea.setId(id);
        updateArea.setUpdateTime(LocalDateTime.now());
        // 枚举值转换
        if (ObjectUtil.isNotNull(dto.getRiskLevel())) {
            updateArea.setRiskLevel(dto.getRiskLevel().getValue());
        }

        // 3. 执行更新（仅更新非空字段）
        boolean update = updateById(updateArea);
        log.info("修改工作区域ID：{}，结果：{}", id, update);
        return update;
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
        if (ObjectUtil.isNull(id)) {
            throw new BizException(EMPTY_WORK_AREA_ID_ERROR_MESSAGE);
        }
        WorkArea workArea = getById(id);
        return BeanUtil.copyProperties(workArea, WorkAreaVO.class);
    }

    /**
     * 按区域编码查询工作区域
     *
     * @param areaCode 他的代码
     * @return 展示
     */
    @Override
    public List<WorkAreaVO> getWorkAreaByCode(String areaCode) {
        if (ObjectUtil.isEmpty(areaCode)) {
            throw new BizException(EMPTY_WORK_AREA_CODE_ERROR_MESSAGE);
        }
        List<WorkArea> workAreas = lambdaQuery().like(WorkArea::getAreaCode, areaCode).list();
        if (ObjectUtil.isNotEmpty(workAreas)) {
            return BeanUtil.copyToList(workAreas, WorkAreaVO.class);
        }
        return Collections.emptyList();
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
    public Page<WorkAreaVO> queryWorkAreas(Integer pageNum, Integer pageSize, String areaName, RiskLevel riskLevel) {
        // 1. 分页参数处理
        pageNum = ObjectUtil.defaultIfNull(pageNum, 1);
        pageSize = ObjectUtil.defaultIfNull(pageSize, 20);
        pageSize = Math.min(pageSize, 50);

        // 2. 构建查询条件
        LambdaQueryWrapper<WorkArea> queryWrapper = new LambdaQueryWrapper<>();
        // 名称模糊查询
        if (ObjectUtil.isNotEmpty(areaName)) {
            queryWrapper.like(WorkArea::getAreaName, areaName);
        }
        // 风险等级精准查询（枚举转字符串）
        if (ObjectUtil.isNotNull(riskLevel)) {
            queryWrapper.eq(WorkArea::getRiskLevel, riskLevel.getValue());
        }
        // 按更新时间降序
        queryWrapper.orderByDesc(WorkArea::getUpdateTime);

        // 3. 分页查询
        Page<WorkArea> page = new Page<>(pageNum, pageSize);
        Page<WorkArea> resultPage = page(page, queryWrapper);

        // 4. PO转VO
        Page<WorkAreaVO> voPage = new Page<>();
        BeanUtil.copyProperties(resultPage, voPage);
        voPage.setRecords(BeanUtil.copyToList(resultPage.getRecords(), WorkAreaVO.class));

        log.info("分页查询工作区域：名称模糊={}，风险等级={}，第{}页，共{}条",
                areaName, riskLevel, pageNum, resultPage.getTotal());
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
        // 1. 分页参数处理（和多条件查询保持一致的规则）
        pageNum = ObjectUtil.defaultIfNull(pageNum, 1);
        pageSize = ObjectUtil.defaultIfNull(pageSize, 20);
        pageSize = Math.min(pageSize, 50);

        // 2. 构建分页对象 + 无筛选条件查询（仅按更新时间降序）
        Page<WorkArea> page = new Page<>(pageNum, pageSize);
        Page<WorkArea> resultPage = lambdaQuery()
                .orderByDesc(WorkArea::getUpdateTime)
                .page(page);

        // 3. PO转VO（复用分页对象拷贝逻辑）
        Page<WorkAreaVO> voPage = new Page<>();
        BeanUtil.copyProperties(resultPage, voPage);
        voPage.setRecords(BeanUtil.copyToList(resultPage.getRecords(), WorkAreaVO.class));

        log.info("分页查询所有工作区域：第{}页，每页{}条，总条数{}",
                pageNum, pageSize, resultPage.getTotal());
        return voPage;
    }
}
