package gang.lu.riskmanagementproject.domain.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 13:21
 * @description 通用分页参数DTO，统一管理分页参数的校验规则、默认值，支持业务场景自定义配置
 */
@Data
@NoArgsConstructor
@ApiModel(description = "通用分页参数DTO")
@Accessors(chain = true)
public class PageQueryDTO {
    /**
     * 默认全局页码
     */
    public static final Integer DEFAULT_PAGE_NUM = 1;
    /**
     * 默认全局页大小
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    /**
     * 默认全局最大页大小
     */
    public static final Integer DEFAULT_MAX_PAGE_SIZE = 100;

    @ApiModelProperty(value = "页码（默认1）", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页条数", example = "10")
    @Range(min = 1, max = Integer.MAX_VALUE, message = "每页条数需在1-{max}之间")
    private Integer pageSize;

    /**
     * 可以自定义分页业务规则，但是目前还是使用deFault
     */
    private Integer customDefaultPageNum;
    private Integer customDefaultPageSize;
    private Integer customMaxPageSize;

    /**
     * 构建业务自定义分页参数
     *
     * @param customDefaultPageNum  业务默认页码
     * @param customDefaultPageSize 业务默认页大小
     * @param customMaxPageSize     业务最大页大小
     * @return 分页参数DTO
     */
    public static PageQueryDTO buildCustom(Integer customDefaultPageNum,
                                           Integer customDefaultPageSize,
                                           Integer customMaxPageSize) {
        PageQueryDTO dto = new PageQueryDTO();
        dto.setCustomDefaultPageNum(customDefaultPageNum);
        dto.setCustomDefaultPageSize(customDefaultPageSize);
        dto.setCustomMaxPageSize(customMaxPageSize);
        return dto;
    }

    /**
     * 工作区域分页规则
     */
    public static PageQueryDTO workAreaPage() {
        return buildCustom(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE, DEFAULT_MAX_PAGE_SIZE);
    }

    /**
     * 工人分页规则
     */
    public static PageQueryDTO workerPage() {
        return buildCustom(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE, DEFAULT_MAX_PAGE_SIZE);
    }

    /**
     * 风险指标分页规则
     */
    public static PageQueryDTO riskIndicatorPage() {
        return buildCustom(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE, DEFAULT_MAX_PAGE_SIZE);
    }

    /**
     * 预警记录分页规则
     */
    public static PageQueryDTO alertRecordPage() {
        return buildCustom(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE, DEFAULT_MAX_PAGE_SIZE);
    }
}

