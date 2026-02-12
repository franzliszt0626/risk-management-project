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
        BusinessLog.LogLevel logLevel = businessLog.logLevel();
        // 2. 构建基础日志上下文
        StringBuilder logContext = new StringBuilder();
        logContext.append(String.format("【业务操作：%s】方法：%s", businessName, methodFullName));
        // 3. 获取并记录请求上下文（Web环境）
        if (businessLog.recordRequestContext()) {
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (Objects.nonNull(attributes)) {
                    HttpServletRequest request = attributes.getRequest();
                    // 获取客户端IP（原生Servlet方式，不依赖hutool）
                    String clientIp = request.getRemoteAddr();
                    // 补充请求信息
                    logContext.append(String.format(
                            " | 请求IP：%s | 请求方式：%s | 请求路径：%s | 客户端浏览器：%s",
                            clientIp,
                            request.getMethod(),
                            request.getRequestURI(),
                            request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "未知"
                    ));
                } else {
                    logContext.append(" | 非Web环境（如定时任务）");
                }
            } catch (Exception e) {
                logContext.append(" | 请求上下文获取失败：").append(e.getMessage());
            }
        }
        // 4. 记录方法开始执行日志
        long startTime = System.currentTimeMillis();
        logByLevel(logLevel, logContext.append(" | 执行开始").toString());
        // 记录入参（如果开启）
        // 5. 记录入参（过滤敏感信息）
        if (businessLog.recordParams()) {
            Object[] args = joinPoint.getArgs();
            String paramsStr = Arrays.stream(args)
                    .map(arg -> {
                        // 过滤敏感参数（密码/令牌等）
                        if (arg instanceof String && arg.toString().contains(PASSWORD)) {
                            return "******";
                        }
                        // 复杂对象转JSON（空值处理）
                        return arg == null ? "null" : JSONUtil.toJsonStr(arg);
                    })
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("无参数");
            logByLevel(logLevel, String.format("【业务操作：%s】入参：%s", businessName, paramsStr));
        }
        Object result;
        try {
            // 6. 执行目标方法
            result = joinPoint.proceed();

            // 7. 记录方法执行成功日志
            long costTime = System.currentTimeMillis() - startTime;
            logByLevel(logLevel, String.format(
                    "【业务操作：%s】执行成功 | 耗时：%dms | 方法：%s",
                    businessName, costTime, methodFullName
            ));

            // 8. 记录返回值（按需开启）
            if (businessLog.recordResult()) {
                String resultStr = result == null ? "null" : JSONUtil.toJsonStr(result);
                logByLevel(logLevel, String.format("【业务操作：%s】返回值：%s", businessName, resultStr));
            }
            return result;
        } catch (Exception e) {
            // 9. 记录方法执行失败日志（ERROR级别强制输出）
            long costTime = System.currentTimeMillis() - startTime;
            log.error("【业务操作：{}】执行失败 | 耗时：{} ms | 方法：{} | 异常类型：{} | 异常信息：{}", businessName, costTime, methodFullName, e.getClass().getSimpleName(), e.getMessage());
            // 异常继续抛出，让全局异常处理器处理
            throw e;
        }
    }

    /**
     * 根据指定日志级别输出日志
     */
    private void logByLevel(BusinessLog.LogLevel level, String message) {
        switch (level) {
            case DEBUG:
                log.debug(message);
                break;
            case WARN:
                log.warn(message);
                break;
            case ERROR:
                log.error(message);
                break;
            case INFO:
            default:
                log.info(message);
        }
    }
}