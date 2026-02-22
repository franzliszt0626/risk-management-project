package gang.lu.riskmanagementproject.domain.dto.query;

import gang.lu.riskmanagementproject.property.PageProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

import static gang.lu.riskmanagementproject.message.FailedMessages.*;

/**
 * 通用分页查询参数基类。
 * <p>
 * 所有分页查询 DTO 均应继承此类，默认值由 {@link PageProperty} 统一配置。
 *
 * @author Franz Liszt
 * @since 2026-02-09
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "通用分页参数")
public class PageQueryDTO {

    /**
     * 默认页码
     */
    public static final Integer DEFAULT_PAGE_NUM = PageProperty.DEFAULT_NUM;
    /**
     * 默认每页条数
     */
    public static final Integer DEFAULT_PAGE_SIZE = PageProperty.DEFAULT_SIZE;
    /**
     * 最大每页条数
     */
    public static final Integer DEFAULT_MAX_PAGE_SIZE = PageProperty.MAX_SIZE;

    @ApiModelProperty(value = "页码（默认 1）", example = "1")
    @Min(value = 1, message = PAGE_NUMBER_INVALID)
    private Integer pageNum;

    @ApiModelProperty(value = "每页条数（1-100，默认 10）", example = "10")
    @Range(min = 1, max = 100, message = PAGE_SIZE_INVALID)
    private Integer pageSize;
}