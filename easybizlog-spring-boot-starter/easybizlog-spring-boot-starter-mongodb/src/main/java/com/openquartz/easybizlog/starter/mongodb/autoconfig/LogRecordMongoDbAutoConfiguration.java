package com.openquartz.easybizlog.starter.mongodb.autoconfig;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.openquartz.easybizlog.starter.autoconfig.LogRecordAutoConfiguration;
import com.openquartz.easybizlog.starter.autoconfig.LogRecordProperties;
import com.openquartz.easybizlog.storage.api.ILogRecordService;
import com.openquartz.easybizlog.storage.mongodb.MongoDbLogRecordService;
import com.openquartz.easybizlog.storage.mongodb.mapper.LogRecordMapper;
import com.openquartz.easybizlog.storage.mongodb.mapper.LogRecordMapperImpl;
import lombok.extern.slf4j.Slf4j;
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
 * MongoDB Storage AutoConfiguration
 *
 * @author svnee
 **/
@Slf4j
@ConditionalOnProperty(prefix = "ebl.log.record.storage.mongodb", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({LogRecordProperties.class, LogRecordMongoDbProperties.class})
@AutoConfigureBefore(LogRecordAutoConfiguration.class)
@ConditionalOnClass(MongoClient.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10060)
public class LogRecordMongoDbAutoConfiguration {

    @Bean(destroyMethod = "close")
    @ConditionalOnClass(MongoClient.class)
    @ConditionalOnMissingBean(MongoClient.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public MongoClient mongoClient(LogRecordMongoDbProperties logRecordElasticSearchProperties) {

        ConnectionString connectionString = new ConnectionString(logRecordElasticSearchProperties.getUrl());
        return MongoClients.create(connectionString);
    }

    @Bean
    @ConditionalOnClass(MongoClient.class)
    @ConditionalOnBean(MongoClient.class)
    @ConditionalOnMissingBean(LogRecordMapper.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public LogRecordMapper logRecordMapper(MongoClient mongoClient,
        LogRecordMongoDbProperties logRecordMongoDbProperties) {
        return new LogRecordMapperImpl(mongoClient, logRecordMongoDbProperties.getDatabase(),
            logRecordMongoDbProperties.getCollection());
    }

    @Bean
    @Primary
    @ConditionalOnBean(LogRecordMapper.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public ILogRecordService elasticSearchRecordService(LogRecordMapper logRecordMapper) {
        return new MongoDbLogRecordService(logRecordMapper);
    }

}
