package com.qxcmp.web.view.elements.menu.item;

public class SidebarIconItem extends IconItem {

    public SidebarIconItem() {
        super("sidebar");
    }

    @Override
    public String getClassSuffix() {
        return "sidebar link " + super.getClassSuffix();
    }
}
