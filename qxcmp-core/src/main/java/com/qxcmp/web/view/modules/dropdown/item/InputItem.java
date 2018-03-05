package com.qxcmp.web.view.modules.dropdown.item;

import lombok.Getter;
import lombok.Setter;

/**
 * 搜索输入框
 */
@Getter
@Setter
public class InputItem extends AbstractDropdownItem implements DropdownItem {

    /**
     * 占位符
     */
    private String placeholder;

    /**
     * 搜索图标是否在左边
     */
    private boolean leftIcon;

    public InputItem(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String getFragmentName() {
        return "item-input";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return leftIcon ? "left" : "";
    }

    @Override
    public String getClassSuffix() {
        return "icon search input";
    }

    public InputItem setLeftIcon() {
        setLeftIcon(true);
        return this;
    }
}
