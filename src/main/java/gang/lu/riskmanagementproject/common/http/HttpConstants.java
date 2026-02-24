package gang.lu.riskmanagementproject.common.http;

/**
 * HTTP 常量
 * <p>
 * 统一管理 CORS 配置、HTTP 路径、请求 / 响应头及缓存控制相关常量。
 *
 * <p><b>分区说明：</b>
 * <ol>
 *   <li>CORS 配置</li>
 *   <li>HTTP 路径</li>
 *   <li>HTTP 请求 / 响应头键名</li>
 *   <li>HTTP 请求 / 响应头值</li>
 *   <li>缓存控制</li>
 * </ol>
 *
 * @author Franz Liszt
 * @since 2026-02-23
 */
public interface HttpConstants {

    // ==============================1. CORS 配置================================

    String ALL              = "*";
    String ALL_INTERFACES   = "/**";
    Long   CORS_EXPIRATION_TIME = 3600L;

    // ==============================2. HTTP 路径================================

    /** Qwen / OpenAI 兼容接口路径 */
    String PATH_CHAT_COMPLETIONS = "/chat/completions";

    /** 算法服务分析接口路径 */
    String ANALYZE               = "/analyze";

    // ==============================3. HTTP 请求 / 响应头键名================================

    String HEADER_AUTHORIZATION    = "Authorization";
    String HEADER_CONTENT_TYPE     = "Content-Type";
    String HEADER_CONTENT_LENGTH   = "Content-Length";
    String CONTENT_DISPOSITION_KEY = "Content-Disposition";
    String CACHE_CONTROL           = "Cache-Control";
    String PRAGMA                  = "Pragma";
    String EXPIRES                 = "Expires";

    // ==============================4. HTTP 请求 / 响应头值================================

    String MEDIA_TYPE_JSON           = "application/json";
    String CONTENT_TYPE              = "application/pdf";
    String AUTHORIZATION_BEARER_PREFIX = "Bearer ";
    String CHARSET_UTF8              = "UTF-8";
    String ATTACHMENT_FILENAME       = "attachment; filename=\"";

    // ==============================5. 缓存控制================================

    String CACHE_CONTROL_HEADER = "no-cache, no-store, must-revalidate";
    String PRAGMA_HEADER        = "no-cache";
    String EXPIRES_HEADER       = "0";
}