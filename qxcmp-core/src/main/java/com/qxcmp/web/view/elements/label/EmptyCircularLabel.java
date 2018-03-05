package com.qxcmp.web.view.elements.label;

public class EmptyCircularLabel extends CircularLabel {
    public EmptyCircularLabel() {
        super("");
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " empty";
    }
}
