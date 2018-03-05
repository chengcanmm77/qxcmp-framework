package com.qxcmp.web.view.support;

/**
 * 角落
 *
 * @author Aaric
 */
public enum Corner {
    NONE(""),
    LEFT(" left corner"),
    RIGHT(" right corner"),
    TOP_LEFT(" top left corner"),
    TOP_RIGHT(" top right corner"),
    BOTTOM_LEFT(" bottom left corner"),
    BOTTOM_RIGHT(" bottom right corner");

    private String value;

    Corner(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
