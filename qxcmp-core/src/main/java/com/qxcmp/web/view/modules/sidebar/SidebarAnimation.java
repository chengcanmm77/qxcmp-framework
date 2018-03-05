package com.qxcmp.web.view.modules.sidebar;

/**
 * 对齐方式
 *
 * @author Aaric
 */
public enum SidebarAnimation {
    NONE(""),
    OVERLAY(" overlay"),
    PUSH(" push"),
    SCALE_DOWN(" scale down"),
    UNCOVER(" uncover"),
    SLIDE_ALONG(" slide along"),
    SLIDE_OUT(" slide out");

    private String value;

    SidebarAnimation(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
