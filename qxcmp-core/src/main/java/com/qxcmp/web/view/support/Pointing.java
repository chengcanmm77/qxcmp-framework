package com.qxcmp.web.view.support;

/**
 * 旋转
 *
 * @author Aaric
 */
public enum Pointing {
    TOP_POINTING(" pointing"),
    BOTTOM_POINTING(" pointing below"),
    LEFT_POINTING(" left pointing"),
    RIGHT_POINTING(" right pointing");

    private String value;

    Pointing(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
