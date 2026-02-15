package gang.lu.riskmanagementproject.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static gang.lu.riskmanagementproject.common.BusinessConstants.CREATE_TIME;
import static gang.lu.riskmanagementproject.common.BusinessConstants.UPDATE_TIME;

/**
 * @author Franz Liszt
 * @version 1.0
 * @date 2026/1/31 15:31
 * @description 元数据自动填充控制器
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充...");
        // 只在字段值为 null 时填充
        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("更新时由数据库自动填充 updateTime");
    }
}