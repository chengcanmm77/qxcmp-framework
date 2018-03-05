package com.qxcmp.web.view.elements.reveal;

import com.qxcmp.web.view.Component;

public class MoveUpReveal extends MoveReveal {
    public MoveUpReveal(Component visibleContent, Component hiddenContent) {
        super(visibleContent, hiddenContent);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " up";
    }
}
