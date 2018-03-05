package com.qxcmp.web.view.modules.form;

/**
 * 提交方式
 *
 * @author Aaric
 */
public enum FormMethod {
    NONE(""),
    GET("get"),
    POST("post");

    private String value;

    FormMethod(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
