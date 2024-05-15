package com.openquartz.easybizlog.core.diff;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataMaskFunctionFactory
 * @author svnee
 */
public class DataMaskFunctionFactory {

    private final Map<String, DataMaskFunction> maskType2Function = new ConcurrentHashMap<>();

    private static final DataMaskFunctionFactory INSTANCE = new DataMaskFunctionFactory();

    public static DataMaskFunctionFactory getInstance() {
        return INSTANCE;
    }

    /**
     * register If not exist
     * @param maskType mask type
     * @param maskFunction mask function
     */
    public synchronized void registerIfAbsent(String maskType, DataMaskFunction maskFunction) {
        maskType2Function.putIfAbsent(maskType, maskFunction);
    }

    public DataMaskFunction getMaskFunction(String maskType) {
        return maskType2Function.get(maskType);
    }
}
