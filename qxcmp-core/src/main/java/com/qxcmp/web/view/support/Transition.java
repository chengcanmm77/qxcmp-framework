package com.qxcmp.web.view.support;

/**
 * 动画效果
 *
 * @author Aaric
 */
public enum Transition {
    NONE(""),
    SCALE("scale"),
    FADE("fade"),
    FADE_IN("fade in"),
    FADE_UP("fade up"),
    FADE_DOWN("fade down"),
    FADE_LEFT("fade left"),
    FADE_RIGHT("fade right"),
    HORIZONTAL_FLIP("horizontal flip"),
    VERTICAL_FLIP("vertical flip"),
    FLY_LEFT("fly left"),
    FLY_RIGHT("fly right"),
    FLY_UP("fly up"),
    FLY_DOWN("fly down"),
    SWING_LEFT("swing left"),
    SWING_RIGHT("swing right"),
    SWING_UP("swing up"),
    SWING_DOWN("swing down"),
    BROWSE("browse"),
    BROWSE_RIGHT("browse right"),
    SLIDE_DOWN("slide down"),
    SLIDE_UP("slide up"),
    SLIDE_LEFT("slide left"),
    SLIDE_RIGHT("slide right"),
    JIGGLE("jiggle"),
    FLASH("flash"),
    SHAKE("shake"),
    PULSE("pulse"),
    TADA("tada"),
    BOUNCE("bounce");

    private String value;

    Transition(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
