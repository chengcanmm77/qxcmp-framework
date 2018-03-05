package com.qxcmp.web.view.elements.menu;

import com.qxcmp.web.view.elements.menu.item.IconItem;

public class IconMenu extends AbstractMenu {

    public AbstractMenu addItem(IconItem item) {
        return super.addItem(item);
    }

    @Override
    public String getClassSuffix() {
        return "icon " + super.getClassSuffix();
    }
}
