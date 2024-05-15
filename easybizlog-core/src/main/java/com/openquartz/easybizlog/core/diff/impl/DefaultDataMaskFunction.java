package com.openquartz.easybizlog.core.diff.impl;

import com.openquartz.easybizlog.core.diff.DataMaskFunction;

public class DefaultDataMaskFunction implements DataMaskFunction {

    @Override
    public String mask(String value) {
        return value;
    }
}
