package com.qxcmp.web.view.elements.menu;

import com.qxcmp.web.view.elements.menu.item.LabeledIconItem;

public class LabeledIconMenu extends AbstractMenu {

    public AbstractMenu addItem(LabeledIconItem item) {
        return super.addItem(item);
    }

    @Override
    public String getClassSuffix() {
        return "labeled icon " + super.getClassSuffix();
    }
}
