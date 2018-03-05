package com.qxcmp.web.view.support;

/**
 * 下拉框指向
 *
 * @author Aaric
 */
public enum DropdownPointing {
    NONE(""),
    DEFAULT(" pointing"),
    LEFT(" left pointing"),
    RIGHT(" right pointing"),
    TOP_LEFT(" top left pointing"),
    TOP_RIGHT(" top right pointing"),
    BOTTOM_LEFT(" bottom left pointing"),
    BOTTOM_RIGHT(" bottom right pointing");

    private String value;

    DropdownPointing(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
