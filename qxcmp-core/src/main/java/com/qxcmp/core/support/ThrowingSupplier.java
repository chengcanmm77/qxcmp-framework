package com.qxcmp.core.support;

/**
 * 该函数式接口为在Lambda表达式中抛出异常提供支持
 *
 * @author aaric
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    T get() throws E;
}
