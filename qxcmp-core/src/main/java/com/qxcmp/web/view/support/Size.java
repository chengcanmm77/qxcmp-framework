package com.qxcmp.web.view.support;

/**
 * 大小
 *
 * @author Aaric
 */
public enum Size {
    NONE(""),
    MINI(" mini"),
    TINY(" tiny"),
    SMALL(" small"),
    MEDIUM(" medium"),
    LARGE(" large"),
    BIG(" big"),
    HUGE(" huge"),
    MASSIVE(" massive");

    private String value;

    Size(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
