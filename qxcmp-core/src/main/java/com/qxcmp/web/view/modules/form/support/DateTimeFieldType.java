package com.qxcmp.web.view.modules.form.support;

public enum DateTimeFieldType {
    DATETIME("datetime"),
    DATE("date"),
    TIME("time"),
    MONTH("month"),
    YEAR("year");

    private String value;

    DateTimeFieldType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
