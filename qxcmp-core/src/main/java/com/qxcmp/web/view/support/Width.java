package com.qxcmp.web.view.support;

/**
 * 宽度
 *
 * @author Aaric
 */
public enum Width {
    NONE(""),
    VERY_THIN(" very thin"),
    THIN(" thin"),
    WIDE(" wide"),
    VERY_WIDE(" very wide");

    private String value;

    Width(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
