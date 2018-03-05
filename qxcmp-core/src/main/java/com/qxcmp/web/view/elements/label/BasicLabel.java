package com.qxcmp.web.view.elements.label;

public class BasicLabel extends Label {
    public BasicLabel(String text) {
        super(text);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " basic";
    }
}
