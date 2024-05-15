package com.openquartz.easybizlog.core.diff;

/**
 * @author svnee
 */
public interface DataMaskFunction {

    /**
     * 脱敏
     * @param value 数据
     * @return 脱敏后的内容
     */
    String mask(String value);

}
