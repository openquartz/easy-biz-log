package com.openquartz.easybizlog.starter.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

/**
 * @author svnee
 */
@Data
@ConfigurationProperties(prefix = "ebl.log.record")
public class LogRecordProperties {

    /**
     * 是否启用LogRecord
     */
    private Boolean enabled = true;

    /**
     * 是否全部记录日志
     */
    private Boolean diffLog = false;

    /**
     * 租户
     */
    private String tenant = "default";

    /**
     * 是否加入事务中
     */
    private Boolean joinTransaction = true;

    /**
     * log record advice order
     */
    private Integer logRecordAdviceOrder = Ordered.LOWEST_PRECEDENCE;

}
