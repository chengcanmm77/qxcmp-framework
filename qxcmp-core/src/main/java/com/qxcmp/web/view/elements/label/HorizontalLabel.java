package com.qxcmp.web.view.elements.label;

public class HorizontalLabel extends Label {
    public HorizontalLabel(String text) {
        super(text);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " horizontal";
    }
}
