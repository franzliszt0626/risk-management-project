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
 * 业务操作日志切面。
 * <p>
 * 优化点：
 * <ol>
 *   <li>敏感字段过滤扩展为常量列表，方便维护。</li>
 *   <li>返回值若为 {@code byte[]}（如 PDF 二进制），不打印内容，改为打印字节大小。</li>
 *   <li>返回值若为 {@link String} 且超过 {@link #MAX_RESULT_LEN} 字符，截断后打印，
 *       避免 AI 原始 JSON 撑爆日志。</li>
 *   <li>统一日志前缀格式，与 {@link ValidateLogAspect} 风格对齐。</li>
 * </ol>
 *
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/22
 */
@Slf4j
@Aspect
@Component
public class BusinessLogAspect {

    /**
     * 敏感参数关键词（出现即脱敏）
     */
    private static final String[] SENSITIVE_KEYWORDS = {"password", "token", "secret", "apiKey"};

    /**
     * 字符串类型返回值最大打印长度（超出截断）—— 避免 AI 大段 JSON 撑爆日志
     */
    private static final int MAX_RESULT_LEN = 500;

    // ─────────────────────────────────────────────────────────────

    @Pointcut("@annotation(gang.lu.riskmanagementproject.annotation.BusinessLog)")
    public void businessLogPointcut() {
    }

    @Around("businessLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // ── 1. 元数据 ──────────────────────────────────────────────
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BusinessLog annotation = method.getAnnotation(BusinessLog.class);

        String bizName = annotation.value();
        String fullName = joinPoint.getTarget().getClass().getName() + "." + method.getName();
        BusinessLog.LogLevel level = annotation.logLevel();

        // ── 2. 请求上下文 ──────────────────────────────────────────
        StringBuilder ctx = new StringBuilder();
        ctx.append(String.format("【%s】方法: %s", bizName, fullName));

        if (annotation.recordRequestContext()) {
            try {
                ServletRequestAttributes attrs =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (Objects.nonNull(attrs)) {
                    HttpServletRequest req = attrs.getRequest();
                    ctx.append(String.format(" | IP: %s | %s %s | UA: %s",
                            req.getRemoteAddr(),
                            req.getMethod(),
                            req.getRequestURI(),
                            req.getHeader("User-Agent") != null ? req.getHeader("User-Agent") : "未知"));
                } else {
                    ctx.append(" | 非Web环境");
                }
            } catch (Exception ex) {
                ctx.append(" | 请求上下文异常: ").append(ex.getMessage());
            }
        }

        // ── 3. 开始日志 ────────────────────────────────────────────
        long startMs = System.currentTimeMillis();
        logByLevel(level, ctx + " | 开始执行");

        // ── 4. 入参日志 ────────────────────────────────────────────
        if (annotation.recordParams()) {
            String params = Arrays.stream(joinPoint.getArgs())
                    .map(arg -> {
                        if (arg == null) {
                            return "null";
                        }
                        String argStr = arg.toString();
                        for (String keyword : SENSITIVE_KEYWORDS) {
                            if (argStr.toLowerCase().contains(keyword.toLowerCase())) {
                                return "******";
                            }
                        }
                        return JSONUtil.toJsonStr(arg);
                    })
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("无参数");
            logByLevel(level, String.format("【%s】入参: %s", bizName, params));
        }

        // ── 5. 执行目标方法 ────────────────────────────────────────
        try {
            Object result = joinPoint.proceed();
            long costMs = System.currentTimeMillis() - startMs;

            logByLevel(level, String.format("【%s】执行成功 | 耗时: %d ms | 方法: %s",
                    bizName, costMs, fullName));

            // ── 6. 返回值日志（智能处理）─────────────────────────
            if (annotation.recordResult() && result != null) {
                logByLevel(level, String.format("【%s】返回值: %s", bizName, formatResult(result)));
            }

            return result;

        } catch (Exception e) {
            long costMs = System.currentTimeMillis() - startMs;
            log.error("【{}】执行失败 | 耗时: {} ms | 方法: {} | 异常: {} - {}",
                    bizName, costMs, fullName, e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }
    

    /**
     * 智能格式化返回值：
     * <ul>
     *   <li>{@code byte[]} → 打印字节大小（PDF/文件场景）</li>
     *   <li>超长字符串 → 截断并标注原始长度</li>
     *   <li>其他 → JSON 序列化</li>
     * </ul>
     */
    private String formatResult(Object result) {
        if (result instanceof byte[]) {
            int sizeKb = ((byte[]) result).length / 1024;
            return String.format("[byte[] 大小: %d KB (%d bytes)]",
                    sizeKb, ((byte[]) result).length);
        }
        String json = result instanceof String
                ? (String) result
                : JSONUtil.toJsonStr(result);
        if (json.length() > MAX_RESULT_LEN) {
            return json.substring(0, MAX_RESULT_LEN)
                    + String.format("... [已截断，原始长度: %d 字符]", json.length());
        }
        return json;
    }

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
            default:
                log.info(message);
                break;
        }
    }
}