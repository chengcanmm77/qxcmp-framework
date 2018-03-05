package com.qxcmp.exception;

/**
 * 该异常表明订单状态错误
 * <p>
 * 可能情况有
 * <p>
 * <ol> <li>订单金额为负数</li> <li>订单状态不一致</li> <li>订单用户不存在</li> <li>订单号不存在</li> </ol>
 *
 * @author aaric
 */
public class OrderStatusException extends FinanceException {

    private static final String CODE = "ILLEGAL_ORDER_STATUS";

    public OrderStatusException(String message) {
        super(message, CODE);
    }
}
