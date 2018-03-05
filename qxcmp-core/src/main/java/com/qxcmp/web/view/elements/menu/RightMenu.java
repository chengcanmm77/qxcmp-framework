package com.qxcmp.web.view.elements.menu;

/**
 * 右侧菜单
 *
 * @author Aaric
 */
public class RightMenu extends AbstractMenu {

    @Override
    public String getClassPrefix() {
        return "";
    }

    @Override
    public String getClassSuffix() {
        return "right " + super.getClassSuffix();
    }
}
