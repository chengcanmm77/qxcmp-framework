package com.qxcmp.exception;

/**
 * 该异常表明用户余额不足
 * <p>
 * 当用户进行支付操作的时候，如果账户余额不足，则抛出该异常
 *
 * @author aaric
 */
public class NoBalanceException extends FinanceException {

    private static final String CODE = "NO_BALANCE";

    public NoBalanceException(String message) {
        super(message, CODE);
    }
}
