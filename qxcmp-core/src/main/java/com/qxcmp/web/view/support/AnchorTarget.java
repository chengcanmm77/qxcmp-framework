package com.qxcmp.web.view.support;

/**
 * 超链接打开方式
 *
 * @author Aaric
 */
public enum AnchorTarget {
    NONE(""),
    BLANK("_blank"),
    SELF("_self"),
    PARENT("_parent"),
    TOP("_top");

    private String value;

    AnchorTarget(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
