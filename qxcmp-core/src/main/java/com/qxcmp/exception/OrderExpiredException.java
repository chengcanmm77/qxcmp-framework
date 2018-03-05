package com.qxcmp.exception;

/**
 * 该异常表明订单已经过期
 * <p>
 * 如果处理订单的时间已经超过了订单有效期，则放弃处理该订单，并将订单状态标记为已过期
 *
 * @author aaric
 */
public class OrderExpiredException extends FinanceException {

    private static final String CODE = "ORDER_EXPIRED";

    public OrderExpiredException(String message) {
        super(message, CODE);
    }
}
