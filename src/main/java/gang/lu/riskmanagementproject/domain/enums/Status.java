package gang.lu.riskmanagementproject.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import gang.lu.riskmanagementproject.common.FailureMessages;
import lombok.Getter;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:15
 * @description 工人状态类，前端需要提供下拉菜单，不可让用户自己输入
 */
@Getter
public enum Status {

    /*
     * 工人当前状态枚举
     * 对应数据库 t_worker.status 字段：
     * ENUM('正常', '异常', '离线')
     */
    NORMAL("正常"),
    ABNORMAL("异常"),
    OFFLINE("离线");

    @EnumValue
    private final String value;

    Status(String value) {
        this.value = value;
    }


    /**
     * 工具方法：字符串转 Status 枚举
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Status fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(FailureMessages.WORKER_STATUS_EMPTY);
        }
        String trimValue = value.trim();
        for (Status status : values()) {
            if (status.value.equals(trimValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException(
                String.format(FailureMessages.WORKER_STATUS_INVALID, trimValue)
        );
    }
}