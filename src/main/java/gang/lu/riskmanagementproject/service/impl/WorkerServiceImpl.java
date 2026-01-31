package gang.lu.riskmanagementproject.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gang.lu.riskmanagementproject.domain.dto.WorkerDTO;
import gang.lu.riskmanagementproject.domain.enums.Status;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.domain.vo.WorkerVO;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import gang.lu.riskmanagementproject.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static gang.lu.riskmanagementproject.common.FailureMessages.*;

/**
 * <p>
 * 工人基本信息表 服务实现类
 * </p>
 *
 * @author Franz Liszt
 * @since 2026-01-31
 */
@Service
@RequiredArgsConstructor
public class WorkerServiceImpl extends ServiceImpl<WorkerMapper, Worker> implements WorkerService {

    private final WorkerMapper workerMapper;

    /**
     * 创建工人
     *
     * @param dto 工人DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWorker(WorkerDTO dto) {
        // 得到工号
        String workerCode = dto.getWorkerCode();
        // 如果工号为空
        if (Objects.isNull(workerCode)) {
            throw new BizException(WORKER_ID_NOT_EXISTING_ERROR_MESSAGE);
        }
        if (ObjUtil.isNotNull(getWorkerByCodeWithOutVerify(workerCode))) {
            throw new BizException(DUPLICATE_WORKER_ID_ERROR_MESSAGE);
        }
        Worker worker = new Worker();
        BeanUtil.copyProperties(dto, worker);
        int inserted = workerMapper.insert(worker);
        if (inserted != 1) {
            throw new BizException(CREATE_WORKER_ERROR_MESSAGE);
        }
    }

    /**
     * 根据ID删除工人
     *
     * @param id 工人id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorker(Long id) {
        // 如果id为空或者小于等于0
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(INVALID_ID_ERROR_MESSAGE);
        }
        Worker existing = workerMapper.selectById(id);
        if (Objects.isNull(existing)) {
            throw new BizException(WORKER_NOT_EXISTING_ERROR_MESSAGE);
        }
        workerMapper.deleteById(id);
    }

    /**
     * 更新工人信息
     *
     * @param id  工人id
     * @param dto 工人DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorker(Long id, WorkerDTO dto) {
        Worker existing = workerMapper.selectById(id);
        if (Objects.isNull(existing)) {
            throw new BizException(WORKER_NOT_EXISTING_ERROR_MESSAGE);
        }
        Worker updated = new Worker();
        updated.setId(id);
        BeanUtil.copyProperties(dto, updated);
        workerMapper.updateById(updated);
    }

    /**
     * 根据ID查询（返回VO）
     *
     * @param id 工人id
     * @return WorkerVO
     */
    @Override
    public WorkerVO getWorkerById(Long id) {
        Worker worker = workerMapper.selectById(id);
        if (Objects.isNull(worker)) {
            throw new BizException(WORKER_NOT_EXISTING_ERROR_MESSAGE);
        }
        return convertToVO(worker);
    }

    /**
     * 根据工号查询（精确）
     *
     * @param workerCode 工号
     * @return WorkerVO
     */
    @Override
    public WorkerVO getWorkerByCode(String workerCode) {
        Worker worker = getWorkerByCodeWithOutVerify(workerCode);
        if (Objects.isNull(worker)) {
            throw new BizException(WORKER_NOT_EXISTING_ERROR_MESSAGE);
        }
        return convertToVO(worker);
    }

    /**
     * 按姓名模糊查询
     *
     * @param name 姓名
     * @return List<WorkerVO>
     */
    @Override
    public List<WorkerVO> searchWorkersByName(String name) {
        // 如果姓名为null或者“”
        if (Objects.isNull(name) || name.trim().isEmpty()) {
            return List.of();
        }
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Worker::getName, name.trim());
        List<Worker> workers = workerMapper.selectList(wrapper);
        return workers.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 按状态查询
     *
     * @param status 工人状态
     * @return List<WorkerVO>
     */
    @Override
    public List<WorkerVO> getWorkersByStatus(Status status) {
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Worker::getStatus, status);
        List<Worker> workers = workerMapper.selectList(wrapper);
        return workers.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 按岗位查询
     *
     * @param position 岗位
     * @return List<WorkerVO>
     */
    @Override
    public List<WorkerVO> getWorkersByPosition(String position) {
        if (Objects.isNull(position) || position.trim().isEmpty()) {
            return List.of();
        }
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Worker::getPosition, position.trim());
        List<Worker> workers = workerMapper.selectList(wrapper);
        return workers.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 工具方法
     */
    private WorkerVO convertToVO(Worker po) {
        WorkerVO vo = new WorkerVO();
        BeanUtil.copyProperties(po, vo);
        return vo;
    }

    /**
     * 仅仅根据工号查询，不做校验和转换
     */
    public Worker getWorkerByCodeWithOutVerify(String workerCode) {
        LambdaQueryWrapper<Worker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Worker::getWorkerCode, workerCode);
        return workerMapper.selectOne(wrapper);
    }
}
