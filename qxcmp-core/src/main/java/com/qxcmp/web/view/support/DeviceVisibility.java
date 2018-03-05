package com.qxcmp.web.view.support;

/**
 * 设备可见性
 *
 * @author Aaric
 */
public enum DeviceVisibility {
    NONE(""),
    LARGE_ONLY("large screen only"),
    COMPUTER_ONLY("computer only"),
    TABLET_ONLY("tablet only"),
    MOBILE_ONLY("mobile only");

    private String className;

    DeviceVisibility(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
