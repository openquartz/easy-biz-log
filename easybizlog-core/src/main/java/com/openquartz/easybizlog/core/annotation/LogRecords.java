package com.openquartz.easybizlog.core.annotation;

import java.lang.annotation.*;

/**
 * @author svnee
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecords {
    LogRecord[] value();
}
