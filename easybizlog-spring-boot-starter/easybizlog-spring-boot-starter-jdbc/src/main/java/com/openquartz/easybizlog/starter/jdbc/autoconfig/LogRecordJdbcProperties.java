package com.openquartz.easybizlog.starter.jdbc.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * LogRecordJdbcProperties
 * @author svnee
 */
@Data
@ConfigurationProperties(prefix = "ebl.log.record.storage.jdbc")
public class LogRecordJdbcProperties {

    /**
     * jdbc存储是否启用
     */
    private boolean enabled = true;
}
