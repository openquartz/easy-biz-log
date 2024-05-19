package com.openquartz.easybizlog.starter.es.autoconfig;

import com.openquartz.easybizlog.starter.autoconfig.LogRecordAutoConfiguration;
import com.openquartz.easybizlog.starter.autoconfig.LogRecordProperties;
import com.openquartz.easybizlog.storage.api.ILogRecordService;
import com.openquartz.easybizlog.storage.es.ElasticSearchLogRecordService;
import com.openquartz.easybizlog.storage.es.mapper.LogRecordMapper;
import com.openquartz.easybizlog.storage.es.mapper.LogRecordMapperImpl;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;

/**
 * ElasticSearch Storage AutoConfiguration
 *
 * @author svnee
 **/
@Slf4j
@ConditionalOnProperty(prefix = "ebl.log.record.storage.es", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({LogRecordProperties.class, LogRecordElasticSearchProperties.class})
@AutoConfigureBefore(LogRecordAutoConfiguration.class)
@ConditionalOnClass(RestHighLevelClient.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10060)
public class LogRecordElasticSearchAutoConfiguration {

    @Bean
    @ConditionalOnClass(RestHighLevelClient.class)
    @ConditionalOnMissingBean(RestHighLevelClient.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public RestHighLevelClient restHighLevelClient(LogRecordElasticSearchProperties logRecordElasticSearchProperties) {

        String ipHosts = logRecordElasticSearchProperties.getSource().getHosts();

        Header[] headers = new BasicHeader[2];
        headers[0] = new BasicHeader("username", logRecordElasticSearchProperties.getSource().getUsername());
        headers[1] = new BasicHeader("password", logRecordElasticSearchProperties.getSource().getPassword());

        RestClientBuilder restClientBuilder = RestClient.builder(Arrays.stream(ipHosts.split(";"))
                .filter(host -> !host.trim().isEmpty())
                .map(e -> new HttpHost(e.split(":")[0], Integer.parseInt(e.split(":")[1]), "http"))
                .toArray(HttpHost[]::new))
            .setDefaultHeaders(headers)
            .setRequestConfigCallback(builder -> builder
                .setConnectTimeout(logRecordElasticSearchProperties.getSource().getConnectTimeout())
                .setSocketTimeout(logRecordElasticSearchProperties.getSource().getSocketTimeout()));
        return new RestHighLevelClient(restClientBuilder);
    }

    @Bean
    @ConditionalOnClass(RestHighLevelClient.class)
    @ConditionalOnBean(RestHighLevelClient.class)
    @ConditionalOnMissingBean(LogRecordMapper.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public LogRecordMapper logRecordMapper(RestHighLevelClient restHighLevelClient,
        LogRecordElasticSearchProperties logRecordElasticSearchProperties) {
        return new LogRecordMapperImpl(restHighLevelClient, logRecordElasticSearchProperties.getIndex());
    }

    @Bean
    @Primary
    @ConditionalOnBean(LogRecordMapper.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public ILogRecordService elasticSearchRecordService(LogRecordMapper logRecordMapper) {
        return new ElasticSearchLogRecordService(logRecordMapper);
    }

}
