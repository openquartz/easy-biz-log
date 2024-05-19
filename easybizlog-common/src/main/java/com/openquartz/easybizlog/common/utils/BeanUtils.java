package com.openquartz.easybizlog.common.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BeanUtils {

    private BeanUtils() {
    }

    public static Map<String, Object> objectToMap(Object bean) {

        if (Objects.isNull(bean)) {
            return new HashMap<>();
        }

        Field[] fields = bean.getClass().getFields();
        if (Objects.isNull(fields) || fields.length == 0) {
            return new HashMap<>();
        }

        try {
            Map<String, Object> map = new HashMap<>();
            for (Field field : fields) {
                // 设置访问权限（如果字段是私有的）
                field.setAccessible(true);
                map.put(field.getName(), field.get(bean));
            }
            return map;
        } catch (Exception ex) {
            return ExceptionUtils.rethrow(ex);
        }
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {

        if (Objects.isNull(map) || map.isEmpty()) {
            return null;
        }

        try {
            T bean = clazz.newInstance();
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(bean, map.get(field.getName()));
            }
            return bean;
        } catch (Exception ex) {
            return ExceptionUtils.rethrow(ex);
        }
    }
}