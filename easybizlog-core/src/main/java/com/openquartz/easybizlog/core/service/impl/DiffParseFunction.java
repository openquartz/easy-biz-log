package com.openquartz.easybizlog.core.service.impl;

import com.openquartz.easybizlog.common.context.LogRecordContext;
import com.openquartz.javaobjdiff.DiffUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;

/**
 * @author svnee
 */
@Slf4j
public class DiffParseFunction {
    public static final String diffFunctionName = "_DIFF";
    public static final String OLD_OBJECT = "_oldObj";

    //@Override
    public String functionName() {
        return diffFunctionName;
    }

    //@Override
    public String diff(Object source, Object target) {
        if (source == null && target == null) {
            return "";
        }
        if (source == null || target == null) {
            try {
                Class<?> clazz = source == null ? target.getClass() : source.getClass();
                source = source == null ? clazz.getDeclaredConstructor().newInstance() : source;
                target = target == null ? clazz.getDeclaredConstructor().newInstance() : target;
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        if (!Objects.equals(AopUtils.getTargetClass(source.getClass()), AopUtils.getTargetClass(target.getClass()))) {
            log.error("diff的两个对象类型不同, source.class={}, target.class={}", source.getClass(), target.getClass());
            return "";
        }

        return DiffUtils.diff(source, target);
    }

    public String diff(Object newObj) {
        Object oldObj = LogRecordContext.getMethodOrGlobal(OLD_OBJECT);
        return diff(oldObj, newObj);
    }
}
