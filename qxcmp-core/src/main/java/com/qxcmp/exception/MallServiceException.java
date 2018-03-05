package com.qxcmp.exception;

/**
 * 商城服务异常基类
 *
 * @author aaric
 */
public abstract class MallServiceException extends BaseQXCMPException {

    public MallServiceException() {
    }

    public MallServiceException(String message) {
        super(message);
    }

    public MallServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MallServiceException(Throwable cause) {
        super(cause);
    }

    public MallServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
