package com.openquartz.easybizlog.core.annotation;

import java.lang.annotation.*;

/**
 * all unannotated fields.
 *
 * @author svnee
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiffLogAllFields {
}