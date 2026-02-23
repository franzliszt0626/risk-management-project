package gang.lu.riskmanagementproject.common.http;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/23 14:06
 * @description Http常量
 */
public interface HttpConstants {

    /**
     * 所有源
     */
    String ALL = "*";
    String ALL_INTERFACES = "/**";
    Long CORS_EXPIRATION_TIME = 3600L;

    /**
     * HTTP路径
     */
    String PATH_CHAT_COMPLETIONS = "/chat/completions";

    /**
     * 算法服务endpoint
     */
    String ANALYZE = "/analyze";

    /**
     * HTTP请求/响应头
     */
    String MEDIA_TYPE_JSON = "application/json";
    String HEADER_AUTHORIZATION = "Authorization";
    String HEADER_CONTENT_TYPE = "Content-Type";
    String HEADER_CONTENT_LENGTH = "Content-Length";
    String AUTHORIZATION_BEARER_PREFIX = "Bearer ";
    String CONTENT_TYPE = "application/pdf";
    String CONTENT_DISPOSITION_KEY = "Content-Disposition";
    String CHARSET_UTF8 = "UTF-8";
    String CACHE_CONTROL_HEADER = "no-cache, no-store, must-revalidate";
    String PRAGMA_HEADER = "no-cache";
    String EXPIRES_HEADER = "0";
    String CACHE_CONTROL = "Cache-Control";
    String PRAGMA = "Pragma";
    String EXPIRES = "Expires";
    String ATTACHMENT_FILENAME = "attachment; filename=\"";
}
