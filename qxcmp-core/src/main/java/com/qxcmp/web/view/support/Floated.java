package com.qxcmp.web.view.support;

/**
 * 元素浮动类型
 *
 * @author Aaric
 */
public enum Floated {
    NONE(""),
    LEFT(" left floated"),
    RIGHT(" right floated");

    private String value;

    Floated(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
