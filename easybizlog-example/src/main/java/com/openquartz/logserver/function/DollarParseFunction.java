package com.openquartz.logserver.function;

import com.openquartz.easybizlog.core.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @author svnee
 */
@Component
public class DollarParseFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "DOLLAR";
    }

    @Override
    public String apply(Object value) {
        return "10$,/666";
    }
}
