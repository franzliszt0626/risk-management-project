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
 * 校验器 AOP 切面。
 * <p>
 * 优化点：
 * <ol>
 *   <li>增加耗时统计，方便发现慢校验（如数据库查询型校验）。</li>
 *   <li>统一日志格式与 {@link BusinessLogAspect} 对齐。</li>
 * </ol>
 *
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22
 */
@Slf4j
@Aspect
@Component
public class ValidateLogAspect {

    @Pointcut("@annotation(gang.lu.riskmanagementproject.annotation.ValidateLog)")
    public void validateLogPointcut() {
    }

    @Around("validateLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ValidateLog annotation = method.getAnnotation(ValidateLog.class);

        String name = annotation.value();
        String fullName = joinPoint.getTarget().getClass().getName() + "." + method.getName();
        ValidateLog.LogLevel level = annotation.logLevel();

        logByLevel(level, String.format("【校验: %s】开始 | 方法: %s", name, fullName));

        if (annotation.recordParams()) {
            String params = Arrays.stream(joinPoint.getArgs())
                    .map(arg -> arg == null ? "null" : JSONUtil.toJsonStr(arg))
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("无参数");
            logByLevel(level, String.format("【校验: %s】入参: %s", name, params));
        }

        long startMs = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long costMs = System.currentTimeMillis() - startMs;
            logByLevel(level, String.format("【校验: %s】通过 | 耗时: %d ms | 方法: %s",
                    name, costMs, fullName));
            return result;
        } catch (Exception e) {
            long costMs = System.currentTimeMillis() - startMs;
            log.warn("【校验: {}】不通过 | 耗时: {} ms | 方法: {} | 异常: {} - {}",
                    name, costMs, fullName, e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

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
            default:
                log.debug(message);
                break;
        }
    }
}