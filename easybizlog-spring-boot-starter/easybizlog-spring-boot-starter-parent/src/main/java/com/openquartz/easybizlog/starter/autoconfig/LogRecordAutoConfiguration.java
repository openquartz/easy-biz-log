package com.openquartz.easybizlog.starter.autoconfig;

import com.openquartz.easybizlog.core.service.IFunctionService;
import com.openquartz.easybizlog.core.service.ILogRecordPerformanceMonitor;
import com.openquartz.easybizlog.core.service.IOperatorGetService;
import com.openquartz.easybizlog.core.service.IParseFunction;
import com.openquartz.easybizlog.core.service.impl.DefaultFunctionServiceImpl;
import com.openquartz.easybizlog.core.service.impl.DefaultLogRecordPerformanceMonitor;
import com.openquartz.easybizlog.core.service.impl.DefaultOperatorGetServiceImpl;
import com.openquartz.easybizlog.core.service.impl.DefaultParseFunction;
import com.openquartz.easybizlog.core.service.impl.DiffParseFunction;
import com.openquartz.easybizlog.core.service.impl.ParseFunctionFactory;
import com.openquartz.easybizlog.starter.support.DefaultLogRecordServiceImpl;
import com.openquartz.easybizlog.starter.support.aop.BeanFactoryLogRecordAdvisor;
import com.openquartz.easybizlog.starter.support.aop.LogRecordInterceptor;
import com.openquartz.easybizlog.starter.support.aop.LogRecordOperationSource;
import com.openquartz.easybizlog.storage.api.ILogRecordService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.util.StringUtils;

/**
 * @author svnee
 */
@Slf4j
@ConditionalOnProperty(prefix = "ebl.log.record", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({LogRecordProperties.class})
public class LogRecordAutoConfiguration {

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
    public BeanFactoryLogRecordAdvisor logRecordAdvisor(LogRecordProperties logRecordProperties,
        LogRecordInterceptor logRecordInterceptor,
        LogRecordOperationSource logRecordOperationSource) {
        BeanFactoryLogRecordAdvisor advisor =
            new BeanFactoryLogRecordAdvisor();
        advisor.setLogRecordOperationSource(logRecordOperationSource);
        advisor.setAdvice(logRecordInterceptor);
        advisor.setOrder(logRecordProperties.getLogRecordAdviceOrder());
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean(ILogRecordPerformanceMonitor.class)
    public ILogRecordPerformanceMonitor logRecordPerformanceMonitor() {
        return new DefaultLogRecordPerformanceMonitor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordInterceptor logRecordInterceptor(LogRecordProperties logRecordProperties) {
        LogRecordInterceptor interceptor = new LogRecordInterceptor();
        interceptor.setLogRecordOperationSource(logRecordOperationSource());
        interceptor.setTenant(logRecordProperties.getTenant());
        interceptor.setJoinTransaction(logRecordProperties.getJoinTransaction());
        interceptor.setDiffLog(logRecordProperties.getDiffLog());
        interceptor.setLogRecordPerformanceMonitor(logRecordPerformanceMonitor());
        return interceptor;
    }

    @Bean
    public DiffParseFunction diffParseFunction(LogRecordProperties logRecordProperties) {
        DiffParseFunction diffParseFunction = new DiffParseFunction();
        diffParseFunction.addUseEqualsClass(LocalDateTime.class);
        if (!StringUtils.isEmpty(logRecordProperties.getUseEqualsMethod())) {
            diffParseFunction.addUseEqualsClass(Arrays.asList(logRecordProperties.getUseEqualsMethod().split(",")));
        }
        return diffParseFunction;
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

}
