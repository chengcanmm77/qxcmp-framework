package com.qxcmp.exception;

/**
 * 该异常表明在转账时出现的问题
 *
 * @author aaric
 */
public class TransferException extends FinanceException {

    private static final String CODE = "ILLEGAL_ORDER_STATUS";

    public TransferException(String message) {
        super(message, CODE);
    }
}
