package com.openquartz.easybizlog.starter.autoconfig;

import com.openquartz.easybizlog.core.service.impl.DefaultFunctionServiceImpl;
import com.openquartz.easybizlog.core.service.impl.DefaultOperatorGetServiceImpl;
import com.openquartz.easybizlog.core.service.impl.DefaultParseFunction;
import com.openquartz.easybizlog.core.service.impl.DiffParseFunction;
import com.openquartz.easybizlog.core.service.impl.ParseFunctionFactory;
import com.openquartz.easybizlog.core.service.IFunctionService;
import com.openquartz.easybizlog.core.service.ILogRecordPerformanceMonitor;
import com.openquartz.easybizlog.core.service.IOperatorGetService;
import com.openquartz.easybizlog.core.service.IParseFunction;
import com.openquartz.easybizlog.core.service.impl.DefaultLogRecordPerformanceMonitor;
import com.openquartz.easybizlog.starter.annotation.EnableLogRecord;
import com.openquartz.easybizlog.starter.support.DefaultLogRecordServiceImpl;
import com.openquartz.easybizlog.starter.support.diff.DefaultDiffItemsToLogContentService;
import com.openquartz.easybizlog.core.diff.IDiffItemsToLogContentService;
import com.openquartz.easybizlog.starter.support.aop.BeanFactoryLogRecordAdvisor;
import com.openquartz.easybizlog.starter.support.aop.LogRecordInterceptor;
import com.openquartz.easybizlog.starter.support.aop.LogRecordOperationSource;
import com.openquartz.easybizlog.storage.api.ILogRecordService;
import com.openquartz.easybizlog.storage.jdbc.JdbcLogRecordService;
import com.openquartz.easybizlog.storage.jdbc.mapper.LogRecordMapper;
import com.openquartz.easybizlog.storage.jdbc.mapper.LogRecordMapperImpl;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * @author svnee
 */
@Configuration
@EnableConfigurationProperties({LogRecordProperties.class})
@Slf4j
public class LogRecordProxyAutoConfiguration implements ImportAware {

    private AnnotationAttributes enableLogRecord;


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordOperationSource logRecordOperationSource() {
        return new LogRecordOperationSource();
    }

    @Bean
    @ConditionalOnMissingBean(IFunctionService.class)
    public IFunctionService functionService(ParseFunctionFactory parseFunctionFactory) {
        return new DefaultFunctionServiceImpl(parseFunctionFactory);
    }

    @Bean
    public ParseFunctionFactory parseFunctionFactory(@Autowired List<IParseFunction> parseFunctions) {
        return new ParseFunctionFactory(parseFunctions);
    }

    @Bean
    @ConditionalOnMissingBean(IParseFunction.class)
    public DefaultParseFunction parseFunction() {
        return new DefaultParseFunction();
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryLogRecordAdvisor logRecordAdvisor(LogRecordProperties logRecordProperties) {
        BeanFactoryLogRecordAdvisor advisor =
                new BeanFactoryLogRecordAdvisor();
        advisor.setLogRecordOperationSource(logRecordOperationSource());
        advisor.setAdvice(logRecordInterceptor(logRecordProperties.getDiffLog()));
        advisor.setOrder(enableLogRecord.getNumber("order"));
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean(ILogRecordPerformanceMonitor.class)
    public ILogRecordPerformanceMonitor logRecordPerformanceMonitor() {
        return new DefaultLogRecordPerformanceMonitor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordInterceptor logRecordInterceptor(Boolean diffLog) {
        LogRecordInterceptor interceptor = new LogRecordInterceptor();
        interceptor.setLogRecordOperationSource(logRecordOperationSource());
        interceptor.setTenant(enableLogRecord.getString("tenant"));
        interceptor.setJoinTransaction(enableLogRecord.getBoolean("joinTransaction"));
        interceptor.setDiffLog(diffLog);
        interceptor.setLogRecordPerformanceMonitor(logRecordPerformanceMonitor());
        return interceptor;
    }

    @Bean
    public DiffParseFunction diffParseFunction(IDiffItemsToLogContentService diffItemsToLogContentService,
                                               LogRecordProperties logRecordProperties) {
        DiffParseFunction diffParseFunction = new DiffParseFunction();
        diffParseFunction.setDiffItemsToLogContentService(diffItemsToLogContentService);
        // issue#111
        diffParseFunction.addUseEqualsClass(LocalDateTime.class);
        if (!StringUtils.isEmpty(logRecordProperties.getUseEqualsMethod())) {
            diffParseFunction.addUseEqualsClass(Arrays.asList(logRecordProperties.getUseEqualsMethod().split(",")));
        }
        return diffParseFunction;
    }

    @Bean
    @ConditionalOnMissingBean(IDiffItemsToLogContentService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public IDiffItemsToLogContentService diffItemsToLogContentService(LogRecordProperties logRecordProperties) {
        return new DefaultDiffItemsToLogContentService(logRecordProperties);
    }

    @Bean
    @ConditionalOnMissingBean(IOperatorGetService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public IOperatorGetService operatorGetService() {
        return new DefaultOperatorGetServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(ILogRecordService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public ILogRecordService defaultRecordService() {
        return new DefaultLogRecordServiceImpl();
    }

    @Bean
    @ConditionalOnClass(JdbcTemplate.class)
    @ConditionalOnBean(JdbcTemplate.class)
    @ConditionalOnMissingBean(LogRecordMapper.class)
    public LogRecordMapper logRecordMapper(JdbcTemplate jdbcTemplate){
        return new LogRecordMapperImpl(jdbcTemplate);
    }

    @Bean
    @ConditionalOnBean(LogRecordMapper.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public ILogRecordService jdbcRecordService(LogRecordMapper logRecordMapper) {
        return new JdbcLogRecordService(logRecordMapper);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableLogRecord = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableLogRecord.class.getName(), false));
        if (this.enableLogRecord == null) {
            log.info("EnableLogRecord is not present on importing class");
        }
    }
}
