package com.openquartz.easybizlog.core.annotation;

import java.lang.annotation.*;

/**
 * Ignore convert field
 *
 * @author svnee
 **/
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DIffLogIgnore {
}
