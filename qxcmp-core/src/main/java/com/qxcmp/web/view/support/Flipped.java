package com.qxcmp.web.view.support;

/**
 * 翻转
 *
 * @author Aaric
 */
public enum Flipped {
    NONE(""),
    HORIZONTAL(" horizontally flipped"),
    VERTICAL(" vertically flipped");

    private String value;

    Flipped(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
