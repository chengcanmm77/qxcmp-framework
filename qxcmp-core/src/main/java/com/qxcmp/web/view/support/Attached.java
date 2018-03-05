package com.qxcmp.web.view.support;

/**
 * 附着方式
 *
 * @author Aaric
 */
public enum Attached {
    NONE(""),
    ATTACHED(" attached"),
    TOP(" top attached"),
    BOTTOM(" bottom attached"),
    LEFT(" left attached"),
    RIGHT(" right attached"),
    TOP_LEFT(" top left attached"),
    TOP_RIGHT(" top right attached"),
    BOTTOM_LEFT(" bottom left attached"),
    BOTTOM_RIGHT(" bottom right attached");

    private String value;

    Attached(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
