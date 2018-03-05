package com.qxcmp.web.view.modules.dropdown;

/**
 * 用于菜单中的下拉框
 *
 * @author Aaric
 */
public class MenuDropdown extends Dropdown {

    public MenuDropdown() {
    }

    public MenuDropdown(String text) {
        super(text);
    }

    @Override
    public String getClassSuffix() {
        return super.getClassSuffix() + " item";
    }
}
