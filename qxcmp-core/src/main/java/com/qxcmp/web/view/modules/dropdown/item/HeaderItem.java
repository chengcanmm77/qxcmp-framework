package com.qxcmp.web.view.modules.dropdown.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeaderItem extends AbstractDropdownItem implements DropdownItem {

    /**
     * 文本
     */
    private String text;

    /**
     * 图标
     */
    private String icon;

    public HeaderItem(String text) {
        this.text = text;
    }

    public HeaderItem(String text, String icon) {
        this.text = text;
        this.icon = icon;
    }

    @Override
    public String getFragmentName() {
        return "item-header";
    }

    @Override
    public String getClassPrefix() {
        return "";
    }

    @Override
    public String getClassContent() {
        return "";
    }

    @Override
    public String getClassSuffix() {
        return "header";
    }
}
