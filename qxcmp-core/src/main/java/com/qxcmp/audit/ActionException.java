package com.qxcmp.audit;

/**
 * 操作异常
 * <p>
 * 抛出该异常会使操作标记为失败
 *
 * @author aaric
 */
public class ActionException extends Exception {

    /**
     * 创建一个操作异常
     *
     * @param message 异常消息
     */
    public ActionException(String message) {
        super(message);
    }

    /**
     * 创建一个操作异常
     *
     * @param message 异常消息
     * @param cause   失败原因
     */
    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }

}
