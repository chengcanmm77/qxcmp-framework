package com.qxcmp.exception;

/**
 * 购物车服务异常
 *
 * @author aaric
 */
public class ShoppingCartServiceException extends MallServiceException {

    public ShoppingCartServiceException() {
    }

    public ShoppingCartServiceException(String message) {
        super(message);
    }

    public ShoppingCartServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShoppingCartServiceException(Throwable cause) {
        super(cause);
    }

    public ShoppingCartServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
