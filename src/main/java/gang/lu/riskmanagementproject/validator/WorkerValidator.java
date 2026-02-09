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

import static gang.lu.riskmanagementproject.common.FieldName.WORKER_CODE;
import static gang.lu.riskmanagementproject.common.IdName.WORKER_ID;

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
     * 统一校验工人ID合法性+存在性
     *
     * @param id            工人ID
     * @param businessScene 业务场景（可为null，null时返回Worker对象）
     * @return 存在的工人对象（businessScene为null时）
     */
    @ValidateLog("工人ID存在性校验")
    public Worker validateWorkerExist(Long id, String businessScene) {
        // 1、id非空非负
        generalValidator.validateId(id, WORKER_ID);
        // 2、校验id对应的工人是否存在
        Worker worker = workerMapper.selectById(id);
        if (ObjectUtil.isNull(worker)) {
            // 2.1 不存在的话抛异常
            String errorMsg = ObjectUtil.isNull(businessScene)
                    ? FailureMessages.WORKER_NOT_EXIST
                    : String.format(FailureMessages.WORKER_NOT_EXIST_WITH_PARAM, businessScene, id);
            throw new BizException(HttpStatus.NOT_FOUND, errorMsg);
        }
        // 2.2 存在的话直接返回
        return worker;
    }

    public Worker validateWorkerExist(Long id) {
        return validateWorkerExist(id, null);
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
            throw new BizException(HttpStatus.CONFLICT,
                    String.format(FailureMessages.WORKER_CODE_DUPLICATE,
                            businessScene,
                            workerCode)
            );
        }
    }
}
