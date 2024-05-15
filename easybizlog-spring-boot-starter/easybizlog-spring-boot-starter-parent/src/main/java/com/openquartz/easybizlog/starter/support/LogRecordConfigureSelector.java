package com.openquartz.easybizlog.starter.support;

import com.openquartz.easybizlog.starter.annotation.EnableLogRecord;
import com.openquartz.easybizlog.starter.autoconfig.LogRecordProxyAutoConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.lang.Nullable;

/**
 * DATE 6:57 PM
 *
 * @author svnee
 */
public class LogRecordConfigureSelector extends AdviceModeImportSelector<EnableLogRecord> {

    @Override
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{AutoProxyRegistrar.class.getName(), LogRecordProxyAutoConfiguration.class.getName()};
            case ASPECTJ:
                return new String[] {LogRecordProxyAutoConfiguration.class.getName()};
            default:
                return null;
        }
    }
}