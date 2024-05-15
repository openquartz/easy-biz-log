package com.openquartz.easybizlog.core.service.impl;

import com.openquartz.easybizlog.core.service.IParseFunction;

/**
 * @author svnee
 */
public class DefaultParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return null;
    }

    @Override
    public String apply(Object value) {
        return null;
    }
}
