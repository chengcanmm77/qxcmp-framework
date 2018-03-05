package com.qxcmp.web.view.support;

/**
 * 网格宽度
 *
 * @author Aaric
 */
public enum Wide {
    NONE(""),
    ONE(" one wide"),
    TWO(" two wide"),
    THREE(" three wide"),
    FOUR(" four wide"),
    FIVE(" five wide"),
    SIX(" six wide"),
    SEVEN(" seven wide"),
    EIGHT(" eight wide"),
    NINE(" nine wide"),
    TEN(" ten wide"),
    ELEVEN(" eleven wide"),
    TWELVE(" twelve wide"),
    THIRTEEN(" thirteen wide"),
    FOURTEEN(" fourteen wide"),
    FIFTEEN(" fifteen wide"),
    SIXTEEN(" sixteen wide");

    private String value;

    Wide(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
