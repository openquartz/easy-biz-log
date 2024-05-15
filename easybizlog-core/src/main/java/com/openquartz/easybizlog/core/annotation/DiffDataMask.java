package com.openquartz.easybizlog.core.annotation;

import com.openquartz.easybizlog.core.diff.DataMaskType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据脱敏
 * @author svnee
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiffDataMask {

    /**
     * 脱敏类型
     */
    String maskType() default DataMaskType.DEFAULT;
}
