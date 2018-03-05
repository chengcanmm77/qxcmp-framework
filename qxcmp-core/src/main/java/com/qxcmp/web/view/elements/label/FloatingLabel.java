package com.qxcmp.web.view.elements.label;

public class FloatingLabel extends Label {
    public FloatingLabel(String text) {
        super(text);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " floating";
    }
}
