package gang.lu.riskmanagementproject.domain.dto.query;

import gang.lu.riskmanagementproject.config.PageConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

import static gang.lu.riskmanagementproject.common.FailedMessages.PAGE_NUMBER_INVALID;
import static gang.lu.riskmanagementproject.common.FailedMessages.PAGE_SIZE_INVALID;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 13:21
 * @description 通用分页参数DTO，统一管理分页参数的校验规则、默认值，支持业务场景自定义配置
 */
@Data
@RequiredArgsConstructor
@ApiModel(description = "通用分页参数DTO")
@Accessors(chain = true)
public class PageQueryDTO {

    /**
     * 默认全局页码
     */
    public static final Integer DEFAULT_PAGE_NUM = PageConfig.DEFAULT_NUM;
    /**
     * 默认全局页大小
     */
    public static final Integer DEFAULT_PAGE_SIZE = PageConfig.DEFAULT_SIZE;
    /**
     * 默认全局最大页大小
     */
    public static final Integer DEFAULT_MAX_PAGE_SIZE = PageConfig.MAX_SIZE;

    @ApiModelProperty(value = "页码（默认1）", example = "1")
    @Min(value = 1, message = PAGE_NUMBER_INVALID)
    private Integer pageNum;

    @ApiModelProperty(value = "每页条数", example = "10")
    @Range(min = 1, max = 100, message = PAGE_SIZE_INVALID)
    private Integer pageSize;
}

