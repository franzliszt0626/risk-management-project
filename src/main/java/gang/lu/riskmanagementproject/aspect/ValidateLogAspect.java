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

        // 2. 记录校验开始日志（可选，也可只记录失败日志）
        log.debug("【{}开始】方法：{}", validateName, methodFullName);
        if (validateLog.recordParams()) {
            String paramsStr = Arrays.stream(joinPoint.getArgs())
                    .map(JSONUtil::toJsonStr)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("无参数");
            log.debug("【{}入参】{}", validateName, paramsStr);
        }

        try {
            // 3. 执行校验方法
            Object result = joinPoint.proceed();

            // 4. 校验成功日志（debug级别，避免日志冗余）
            log.debug("【{}成功】方法：{}", validateName, methodFullName);
            return result;
        } catch (Exception e) {
            // 5. 校验失败日志（warn级别，重点关注）
            log.warn("【{}失败】方法：{}，异常信息：{}", validateName, methodFullName, e.getMessage());
            // 异常继续抛出，让全局异常处理器处理
            throw e;
        }
    }
}