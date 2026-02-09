package gang.lu.riskmanagementproject.aspect;


import cn.hutool.json.JSONUtil;
import gang.lu.riskmanagementproject.annotation.BusinessLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/9 13:16
 * @description 业务操作日志切面
 * 自动记录方法执行的日志（成功/失败、参数、耗时、请求信息等）
 */
@Slf4j
@Aspect
@Component
public class BusinessLogAspect {

    public static final String PASSWORD = "password";

    /**
     * 切点：拦截所有带有@BusinessLog注解的方法
     */
    @Pointcut("@annotation(gang.lu.riskmanagementproject.annotation.BusinessLog)")
    public void businessLogPointcut() {
    }

    /**
     * 环绕通知：在方法执行前后记录日志
     */
    @Around("businessLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取方法元数据
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);
        String businessName = businessLog.value();
        String methodFullName = String.format("%s.%s", joinPoint.getTarget().getClass().getName(), method.getName());

        // 2. 记录请求基本信息
        HttpServletRequest request = null;
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(attributes)) {
                request = attributes.getRequest();
            }
        } catch (Exception e) {
            // 非Web环境（如定时任务）忽略
        }

        // 3. 记录方法开始执行日志
        long startTime = System.currentTimeMillis();
        log.info("【{}开始】方法：{}", businessName, methodFullName);
        // 记录入参（如果开启）
        if (businessLog.recordParams()) {
            Object[] args = joinPoint.getArgs();
            String paramsStr = Arrays.stream(args)
                    .map(arg -> {
                        // 过滤敏感参数（可自定义，如密码、token）
                        if (arg instanceof String && arg.toString().contains(PASSWORD)) {
                            return "******";
                        }
                        return JSONUtil.toJsonStr(arg);
                    })
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("无参数");
            log.info("【{}入参】{}", businessName, paramsStr);
        }

        Object result = null;
        try {
            // 4. 执行目标方法
            result = joinPoint.proceed();

            // 5. 记录方法执行成功日志
            long costTime = System.currentTimeMillis() - startTime;
            log.info("【{}成功】耗时：{}ms", businessName, costTime);
            // 记录返回值（如果开启）
            if (businessLog.recordResult()) {
                log.info("【{}返回值】{}", businessName, JSONUtil.toJsonStr(result));
            }
            return result;
        } catch (Exception e) {
            // 6. 记录方法执行失败日志
            long costTime = System.currentTimeMillis() - startTime;
            log.error("【{}失败】耗时：{}ms，异常信息：{}", businessName, costTime, e.getMessage(), e);
            // 异常继续抛出，让全局异常处理器处理
            throw e;
        }
    }
}