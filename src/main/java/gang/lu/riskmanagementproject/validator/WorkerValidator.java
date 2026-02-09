package gang.lu.riskmanagementproject.validator;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import gang.lu.riskmanagementproject.annotation.ValidateLog;
import gang.lu.riskmanagementproject.common.FailureMessages;
import gang.lu.riskmanagementproject.domain.po.Worker;
import gang.lu.riskmanagementproject.exception.BizException;
import gang.lu.riskmanagementproject.mapper.WorkerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static gang.lu.riskmanagementproject.common.BusinessScene.GET_WORKER;
import static gang.lu.riskmanagementproject.common.FieldName.WORKER_CODE;
import static gang.lu.riskmanagementproject.common.IdName.WORKER_ID;
import static gang.lu.riskmanagementproject.util.BasicUtil.handleCustomPageParams;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 11:01
 * @description 工人校验类
 */
@Component
@RequiredArgsConstructor
public class WorkerValidator {


    private final WorkerMapper workerMapper;

    private final GeneralValidator generalValidator;


    /**
     * 校验工人ID合法性+存在性
     */
    @ValidateLog("工人ID合法性校验")
    public void validateWorkerExist(Long workerId, String businessScene) {
        generalValidator.validateId(workerId, WORKER_ID);
        if (ObjectUtil.isNull(workerMapper.selectById(workerId))) {
            String errorMsg = String.format("【%s失败】工人不存在，ID：%s", businessScene, workerId);
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }
    }

    /**
     * 工号校验（非空+唯一性）
     */
    @ValidateLog("工号唯一性校验")
    public void validateWorkerCode(String workerCode, Long excludeId, String businessScene) {
        // 1. 非空校验
        generalValidator.validateStringNotBlank(workerCode, WORKER_CODE, businessScene);
        // 2. 唯一性校验（excludeId=null表示新增）
        boolean exists = ObjectUtil.isNotNull(workerMapper.selectOne(
                new LambdaQueryWrapper<Worker>()
                        .eq(Worker::getWorkerCode, workerCode)
                        .ne(ObjectUtil.isNotNull(excludeId), Worker::getId, excludeId)
        ));
        if (exists) {
            String errorMsg = String.format("【%s失败】工号重复：%s", businessScene, workerCode);
            throw new BizException(HttpStatus.CONFLICT, FailureMessages.WORKER_PARAM_DUPLICATE_CODE);
        }
    }

    /**
     * 工人分页参数处理（默认20条/页，最大50）
     */
    @ValidateLog("工人分页参数处理")
    public Integer[] handleWorkerPageParams(Integer pageNum, Integer pageSize) {
        return handleCustomPageParams(pageNum, pageSize, GET_WORKER);
    }


    /**
     * 核心校验
     */
    @ValidateLog("工人记录存在性校验")
    public Worker validateWorkerExist(Long id) {
        generalValidator.validateId(id, WORKER_ID);
        Worker worker = workerMapper.selectById(id);
        if (ObjectUtil.isNull(worker)) {
            throw new BizException(HttpStatus.NOT_FOUND, FailureMessages.WORKER_DATA_NOT_EXIST);
        }
        return worker;
    }
}
