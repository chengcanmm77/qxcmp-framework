package com.qxcmp.web.view.modules.form;

/**
 * 表单编码方式
 *
 * @author Aaric
 */
public enum FormEnctype {
    NONE(""),
    APPLICATION("application/x-www-form-urlencoded"),
    MULTIPART("multipart/form-data"),
    TEXT("text/plain");

    private String value;

    FormEnctype(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
