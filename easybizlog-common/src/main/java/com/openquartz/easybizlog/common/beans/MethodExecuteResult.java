package com.openquartz.easybizlog.common.beans;

import lombok.Getter;

import java.lang.reflect.Method;
import lombok.Setter;

/**
 * @author svnee
 **/
@Getter
public class MethodExecuteResult {
    @Setter
    private boolean success;
    @Setter
    private Throwable throwable;
    @Setter
    private String errorMsg;

    @Setter
    private Object result;
    private final Method method;
    private final Object[] args;
    private final Class<?> targetClass;

    public MethodExecuteResult(Method method, Object[] args, Class<?> targetClass) {
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
    }

}
