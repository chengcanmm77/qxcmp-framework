package com.qxcmp.web.view.elements.label;

public class CircularLabel extends Label {
    public CircularLabel(String text) {
        super(text);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " circular";
    }
}
