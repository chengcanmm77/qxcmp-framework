package com.qxcmp.web.view.support;

/**
 * 固定布局
 *
 * @author Aaric
 */
public enum Fixed {
    NONE(""),
    LEFT(" left fixed"),
    RIGHT(" right fixed"),
    TOP(" top fixed"),
    BOTTOM(" bottom fixed");

    private String value;

    Fixed(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
