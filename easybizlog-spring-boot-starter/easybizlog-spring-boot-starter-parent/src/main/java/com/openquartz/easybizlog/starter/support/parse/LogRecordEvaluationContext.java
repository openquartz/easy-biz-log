package com.openquartz.easybizlog.starter.support.parse;

import com.openquartz.easybizlog.common.context.LogRecordContext;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.lang.NonNull;

/**
 * @author svnee
 */
public class LogRecordEvaluationContext extends MethodBasedEvaluationContext {

    public LogRecordEvaluationContext(Object rootObject, Method method, Object[] arguments,
                                      ParameterNameDiscoverer parameterNameDiscoverer, Object ret, String errorMsg) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
        Map<String, Object> variables = LogRecordContext.getVariables();
        Map<String, Object> globalVariable = LogRecordContext.getGlobalVariableMap();
        if (variables != null) {
            setVariables(variables);
        }
        if (globalVariable != null && !globalVariable.isEmpty()) {
            for (Map.Entry<String, Object> entry : globalVariable.entrySet()) {
                if (lookupVariable(entry.getKey()) == null) {
                    setVariable(entry.getKey(), entry.getValue());
                }
            }
        }
        setVariable("_ret", ret);
        setVariable("_errorMsg", errorMsg);
    }
}