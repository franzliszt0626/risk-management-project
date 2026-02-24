package gang.lu.riskmanagementproject.common.global;

/**
 * 全局符号常量
 * <p>
 * 统一管理系统中各类特殊字符串标记、拼接前缀及框架相关标识。
 *
 * <p><b>分区说明：</b>
 * <ol>
 *   <li>AI 响应 Markdown 清理标记</li>
 *   <li>SQL 拼接前缀</li>
 *   <li>框架标识</li>
 * </ol>
 *
 * @author Franz Liszt
 * @since 2026-02-23
 */
public interface GlobalSimbolConstants {

    // ==============================1. AI 响应 Markdown 清理标记================================

    String AI_TEXT_JSON_PATTERN = "```json";
    String AI_TEXT_TICK_PATTERN = "```";

    // ==============================2. SQL 拼接前缀================================

    String LIMIT = "LIMIT ";

    // ==============================3. 框架标识================================

    String SPRING = "spring";
}