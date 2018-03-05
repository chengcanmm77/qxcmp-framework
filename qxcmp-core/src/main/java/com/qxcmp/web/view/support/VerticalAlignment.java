package com.qxcmp.web.view.support;

/**
 * 垂直对齐
 *
 * @author Aaric
 */
public enum VerticalAlignment {
    NONE(""),
    TOP(" top aligned"),
    MIDDLE(" middle aligned"),
    BOTTOM(" bottom aligned");

    private String value;

    VerticalAlignment(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
