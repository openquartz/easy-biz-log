package com.openquartz.easybizlog.starter.mongodb.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * LogRecordElasticSearchProperties
 * @author svnee
 */
@Data
@ConfigurationProperties(prefix = "ebl.log.record.storage.mongodb")
public class LogRecordMongoDbProperties {

    /**
     * mongodb存储是否启用
     */
    private boolean enabled = true;

    /**
     * 连接 mongodb url
     * 例如：mongodb://username:password@localhost:27017
     */
    private String url;

    /**
     * biz-log database
     */
    private String database = "default";

    /**
     * biz-log collection
     */
    private String collection = "ebl_biz_log";

}
