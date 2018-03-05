package com.qxcmp.web.view.support;

/**
 * 设备类型
 *
 * @author Aaric
 */
public enum Device {
    NONE(""),
    COMPUTER(" computer"),
    TABLET(" tablet"),
    MOBILE(" mobile");

    private String value;

    Device(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
