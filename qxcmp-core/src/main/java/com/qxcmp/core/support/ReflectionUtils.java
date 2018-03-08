package com.qxcmp.core.support;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具
 *
 * @author Aaric
 */
public class ReflectionUtils {

    /**
     * 获取一个类以及所有父类的所有字段
     *
     * @param type 类型
     *
     * @return 所有字段集合
     */
    public static List<Field> getAllFields(Class<?> type) {
        return getAllFields(Lists.newArrayList(), type);
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }
}
