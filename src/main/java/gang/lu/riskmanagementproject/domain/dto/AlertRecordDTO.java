package gang.lu.riskmanagementproject.domain.dto;

import gang.lu.riskmanagementproject.domain.enums.AlertLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 16:09
 * @description 警报记录数据传输实体
 */
@Data
@ApiModel(description = "预警记录 - 传输对象")
public class AlertRecordDTO {
    @ApiModelProperty(value = "工人id", required = true, example = "1")
    @NotNull(message = "工人ID不能为空")
    @Positive(message = "工人ID必须为正整数")
    private Long workerId;


    @ApiModelProperty(value = "预警类型", required = true, example = "心率异常")
    @NotBlank(message = "预警类型不能为空")
    @Length(max = 50, message = "预警类型长度不能超过50个字符")
    private String alertType;

    @ApiModelProperty(value = "预警级别", required = true, example = "WARNING")
    @NotNull(message = "预警级别不能为空")
    private AlertLevel alertLevel;

    @ApiModelProperty(value = "信息", example = "工人心率超过120次/分钟")
    @Length(max = 200, message = "预警信息长度不能超过200个字符")
    private String message;

    @ApiModelProperty(value = "是否处理")
    private Boolean isHandled;

    @ApiModelProperty(value = "处理人", example = "张三")
    @Length(max = 20, message = "处理人长度不能超过20个字符")
    private String handledBy;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime handleTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;
}