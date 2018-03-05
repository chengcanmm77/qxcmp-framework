package com.qxcmp.web.view.support;

/**
 * 按钮类型
 *
 * @author Aaric
 */
public enum ButtonType {
    NONE(""),
    BUTTON("button"),
    SUBMIT("submit"),
    RESET("reset");

    private String value;

    ButtonType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
