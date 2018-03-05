package com.qxcmp.web.view.elements.reveal;

import com.qxcmp.web.view.Component;

public class MoveReveal extends AbstractReveal {
    public MoveReveal(Component visibleContent, Component hiddenContent) {
        super(visibleContent, hiddenContent);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " move";
    }
}
