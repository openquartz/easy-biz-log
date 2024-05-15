package com.openquartz.easybizlog.core.service;

public interface IFunctionService {

    String apply(String functionName, Object value);

    boolean beforeFunction(String functionName);
}
