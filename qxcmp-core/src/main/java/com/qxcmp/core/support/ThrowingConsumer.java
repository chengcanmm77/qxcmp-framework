package com.qxcmp.core.support;

/**
 * 该函数式接口为在Lambda表达式中抛出异常提供支持
 *
 * @author aaric
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

    /**
     * 接受一个消费者
     *
     * @param t 消费者类型
     *
     * @throws E 异常类型
     */
    void accept(T t) throws E;
}
