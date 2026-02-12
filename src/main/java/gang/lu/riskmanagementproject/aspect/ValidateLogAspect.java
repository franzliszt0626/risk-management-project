package gang.lu.riskmanagementproject.aspect;

import cn.hutool.json.JSONUtil;
import gang.lu.riskmanagementproject.annotation.ValidateLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 14:05
 * @description 校验器的AOP切面，记录日志
 */
@Slf4j
@Aspect
@Component
public class ValidateLogAspect {

    /**
     * 切点：拦截所有带有@ValidateLog注解的方法
     */
    @Pointcut("@annotation(gang.lu.riskmanagementproject.annotation.ValidateLog)")
    public void validateLogPointcut() {
    }

    /**
     * 环绕通知：在校验方法执行前后记录日志
     */
    @Around("validateLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
// 1. 获取方法元数据
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ValidateLog validateLog = method.getAnnotation(ValidateLog.class);
        String validateName = validateLog.value();
        String methodFullName = String.format("%s.%s", joinPoint.getTarget().getClass().getName(), method.getName());
        ValidateLog.LogLevel logLevel = validateLog.logLevel();

        // 2. 记录校验开始日志
        logByLevel(logLevel, String.format("【校验操作：%s】开始执行 | 方法：%s", validateName, methodFullName));

        // 3. 记录入参（按需开启）
        if (validateLog.recordParams()) {
            String paramsStr = Arrays.stream(joinPoint.getArgs())
                    .map(arg -> arg == null ? "null" : JSONUtil.toJsonStr(arg))
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("无参数");
            logByLevel(logLevel, String.format("【校验操作：%s】入参：%s", validateName, paramsStr));
        }

        try {
            // 4. 执行校验方法
            Object result = joinPoint.proceed();

            // 5. 校验成功日志
            logByLevel(logLevel, String.format("【校验操作：%s】执行成功 | 方法：%s", validateName, methodFullName));
            return result;
        } catch (Exception e) {
            // 6. 校验失败日志（强制WARN级别）
            log.warn("【校验操作：{}】执行失败 | 方法：{} | 异常类型：{} | 异常信息：{}", validateName, methodFullName, e.getClass().getSimpleName(), e.getMessage());
            // 异常继续抛出，让全局异常处理器处理
            throw e;
        }
    }

    /**
     * 根据指定日志级别输出日志
     */
    private void logByLevel(ValidateLog.LogLevel level, String message) {
        switch (level) {
            case INFO:
                log.info(message);
                break;
            case WARN:
                log.warn(message);
                break;
            case ERROR:
                log.error(message);
                break;
            case DEBUG:
            default:
                log.debug(message);
        }
    }
}