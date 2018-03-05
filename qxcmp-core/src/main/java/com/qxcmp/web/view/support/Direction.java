package com.qxcmp.web.view.support;

/**
 * 方位
 *
 * @author Aaric
 */
public enum Direction {
    NONE(""),
    LEFT(" left"),
    RIGHT(" right"),
    TOP(" top"),
    BOTTOM(" bottom"),
    BELOW(" below");

    private String value;

    Direction(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
