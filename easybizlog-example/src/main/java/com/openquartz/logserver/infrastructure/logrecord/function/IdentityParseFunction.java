package com.openquartz.logserver.infrastructure.logrecord.function;


import com.openquartz.easybizlog.core.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @author svnee
 */
@Component
public class IdentityParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "IDENTITY";
    }

    @Override
    public String apply(Object value) {
        return value.toString();
    }
}
