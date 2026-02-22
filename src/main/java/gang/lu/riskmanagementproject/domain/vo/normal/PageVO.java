package gang.lu.riskmanagementproject.domain.vo.normal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 通用分页返回视图对象。
 * <p>
 * 泛型 {@code T} 为具体业务 VO 类型，字段与 MyBatis-Plus {@code IPage} 对齐，
 * 方便直接从 {@code IPage} 转换。
 *
 * @param <T> 数据列表元素类型
 * @author Franz Liszt
 * @since 2026-02-12
 */
@Data
@ApiModel(description = "通用分页返回结果")
public class PageVO<T> {

    @ApiModelProperty(value = "当前页码", example = "1")
    private Long current;

    @ApiModelProperty(value = "每页条数", example = "10")
    private Long size;

    @ApiModelProperty(value = "总记录数", example = "100")
    private Long total;

    @ApiModelProperty(value = "总页数", example = "10")
    private Long pages;

    @ApiModelProperty(value = "当前页数据列表")
    private List<T> records;
}
