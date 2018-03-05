package com.qxcmp.web.view.support;

/**
 * 颜色
 *
 * @author Aaric
 */
public enum Color {
    NONE(""),
    RED(" red"),
    ORANGE(" orange"),
    YELLOW(" yellow"),
    OLIVE(" olive"),
    GREEN(" green"),
    TEAL(" teal"),
    BLUE(" blue"),
    VIOLET(" violet"),
    PURPLE(" purple"),
    PINK(" pink"),
    BROWN(" brown"),
    GREY(" grey"),
    BLACK(" black");

    private String value;

    Color(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
