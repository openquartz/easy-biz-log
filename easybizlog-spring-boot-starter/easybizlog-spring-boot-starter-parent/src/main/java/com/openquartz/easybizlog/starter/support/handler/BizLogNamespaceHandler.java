package com.openquartz.easybizlog.starter.support.handler;

import com.openquartz.easybizlog.starter.support.parse.BizLogBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author svnee
 */
public class BizLogNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("log-record", new BizLogBeanDefinitionParser());
    }
}
