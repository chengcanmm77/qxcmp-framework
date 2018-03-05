package com.qxcmp.web.view.support;

/**
 * 项目数量
 *
 * @author Aaric
 */
public enum ItemCount {
    NONE(""),
    ONE(" one item"),
    TWO(" two item"),
    THREE(" three item"),
    FOUR(" four item"),
    FIVE(" five item"),
    SIX(" six item"),
    SEVEN(" seven item"),
    EIGHT(" eight item"),
    NINE(" nine item"),
    TEN(" ten item"),
    ELEVEN(" eleven item"),
    TWELVE(" twelve item"),
    THIRTEEN(" thirteen item"),
    FOURTEEN(" fourteen item"),
    FIFTEEN(" fifteen item"),
    SIXTEEN(" sixteen item");

    private String value;

    ItemCount(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
