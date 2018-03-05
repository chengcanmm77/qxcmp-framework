package com.qxcmp.web.view.support;

/**
 * 对齐方式
 *
 * @author Aaric
 */
public enum Alignment {
    NONE(""),
    LEFT(" left aligned"),
    RIGHT(" right aligned"),
    CENTER(" center aligned"),
    JUSTIFY(" justified");

    private String value;

    Alignment(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
