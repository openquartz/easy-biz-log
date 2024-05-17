package com.openquartz.easybizlog.starter.support.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @author svnee
 */
public class BeanFactoryLogRecordAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private final LogRecordPointcut pointcut = new LogRecordPointcut();

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        pointcut.setLogRecordOperationSource(logRecordOperationSource);
    }
}