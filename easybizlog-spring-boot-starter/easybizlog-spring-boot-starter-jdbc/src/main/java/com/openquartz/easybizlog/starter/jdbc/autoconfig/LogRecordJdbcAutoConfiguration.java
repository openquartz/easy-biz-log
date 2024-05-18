package com.openquartz.easybizlog.starter.jdbc.autoconfig;

import com.openquartz.easybizlog.starter.autoconfig.LogRecordAutoConfiguration;
import com.openquartz.easybizlog.starter.autoconfig.LogRecordProperties;
import com.openquartz.easybizlog.storage.api.ILogRecordService;
import com.openquartz.easybizlog.storage.jdbc.JdbcLogRecordService;
import com.openquartz.easybizlog.storage.jdbc.mapper.LogRecordMapper;
import com.openquartz.easybizlog.storage.jdbc.mapper.LogRecordMapperImpl;
import javax.sql.DataSource;
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
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Jdbc Storage AutoConfiguration
 *
 * @author svnee
 **/
@Slf4j
@ConditionalOnProperty(prefix = "ebl.log.record.storage.jdbc", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({LogRecordProperties.class, LogRecordJdbcProperties.class})
@AutoConfigureBefore(LogRecordAutoConfiguration.class)
@ConditionalOnClass(JdbcTemplate.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10060)
public class LogRecordJdbcAutoConfiguration {

    @Bean
    @ConditionalOnClass({DataSource.class, JdbcTemplate.class})
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnMissingBean(JdbcTemplate.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnClass(JdbcTemplate.class)
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnMissingBean(LogRecordMapper.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public LogRecordMapper logRecordMapper(JdbcTemplate jdbcTemplate) {
        return new LogRecordMapperImpl(jdbcTemplate);
    }

    @Bean
    @Primary
    @ConditionalOnBean(LogRecordMapper.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public ILogRecordService jdbcRecordService(LogRecordMapper logRecordMapper) {
        return new JdbcLogRecordService(logRecordMapper);
    }

}
