package com.qxcmp.web.view.elements.reveal;

import com.qxcmp.web.view.Component;

public class FadeReveal extends AbstractReveal {
    public FadeReveal(Component visibleContent, Component hiddenContent) {
        super(visibleContent, hiddenContent);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " fade";
    }
}
