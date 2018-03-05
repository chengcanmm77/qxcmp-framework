package com.qxcmp.web.view.support;

/**
 * 旋转
 *
 * @author Aaric
 */
public enum Rotated {
    NONE(""),
    CLOCKWISE(" clockwise rotated"),
    COUNTER_CLOCKWISE(" counterclockwide rotated");

    private String value;

    Rotated(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
