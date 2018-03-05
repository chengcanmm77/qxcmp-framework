package com.qxcmp.web.view.elements.rail;

import com.qxcmp.web.view.Component;

public class LeftRail extends AbstractRail {
    public LeftRail(Component component) {
        super(component);
    }

    @Override
    public String getClassSuffix() {
        return "left rail";
    }
}
