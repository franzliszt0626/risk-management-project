package gang.lu.riskmanagementproject.domain.vo.normal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/2/12 13:37
 * @description 通用分页VO
 */
@Data
@ApiModel(description = "通用分页返回VO")
public class PageVO<T> {
    @ApiModelProperty(value = "页码", example = "1")
    private Long current;

    @ApiModelProperty(value = "每页条数", example = "10")
    private Long size;

    @ApiModelProperty(value = "总记录数", example = "100")
    private Long total;

    @ApiModelProperty(value = "总页数", example = "10")
    private Long pages;

    @ApiModelProperty(value = "数据列表")
    private List<T> records;
}
