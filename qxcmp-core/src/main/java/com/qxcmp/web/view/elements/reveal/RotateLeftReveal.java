package com.qxcmp.web.view.elements.reveal;

import com.qxcmp.web.view.Component;

public class RotateLeftReveal extends RotateReveal {
    public RotateLeftReveal(Component visibleContent, Component hiddenContent) {
        super(visibleContent, hiddenContent);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " left";
    }
}
