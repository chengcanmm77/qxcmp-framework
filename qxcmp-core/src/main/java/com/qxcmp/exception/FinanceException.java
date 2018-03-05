package com.qxcmp.exception;

/**
 * 平台财务相关异常抽象类
 *
 * @author aaric
 */
public abstract class FinanceException extends BaseQXCMPException {

    /**
     * 异常错误代码
     */
    private String code;

    /**
     * Constructs a new exception with the specified detail message.  The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                method.
     */
    public FinanceException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
