package com.openquartz.easybizlog.starter.es.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * LogRecordElasticSearchProperties
 * @author svnee
 */
@Data
@ConfigurationProperties(prefix = "ebl.log.record.storage.es")
public class LogRecordElasticSearchProperties {

    /**
     * elastic-search存储是否启用
     */
    private boolean enabled = true;

    /**
     * 存储index
     */
    private String index = "ebl_biz_log";

    /**
     * 数据源
     */
    private Source source;

    @Data
    public static class Source {

        /**
         * hosts
         */
        private String hosts;

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;

        /**
         * 连接超时时间
         * 默认50s
         */
        private int connectTimeout = 50000;

        /**
         * 读取超时时间
         * 默认30s
         */
        private int socketTimeout = 30000;
    }

}
