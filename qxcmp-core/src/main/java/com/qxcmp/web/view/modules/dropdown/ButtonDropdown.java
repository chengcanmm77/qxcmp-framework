package com.qxcmp.web.view.modules.dropdown;

import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 按钮下拉框
 *
 * @author Aaric
 */
@Getter
@Setter
public class ButtonDropdown extends Dropdown {

    /**
     * 图标
     */
    private Icon icon;

    /**
     * 是否为基本按钮
     */
    private boolean basic;

    /**
     * 是否开启搜索
     */
    private boolean search;

    /**
     * 颜色
     */
    private Color color = Color.NONE;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    public ButtonDropdown(String iconName) {
        this.icon = new Icon(iconName);
    }

    public ButtonDropdown(Icon icon) {
        this.icon = icon;
    }

    public ButtonDropdown(Icon icon, String text) {
        super(text);
        this.icon = icon;
    }

    public ButtonDropdown(String iconName, String text) {
        super(text);
        this.icon = new Icon(iconName);
    }

    @Override
    public String getFragmentName() {
        return "button";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassContent());

        if (basic) {
            stringBuilder.append(" basic");
        }

        if (search) {
            stringBuilder.append(" search");
        }

        if (Objects.isNull(getText())) {
            stringBuilder.append(" icon");
        } else {
            stringBuilder.append(" labeled icon");
        }

        return stringBuilder.append(color).append(size).toString();
    }

    @Override
    public String getClassSuffix() {
        return super.getClassSuffix() + " button";
    }

    public ButtonDropdown setBasic() {
        setBasic(true);
        return this;
    }

    public ButtonDropdown setSearch() {
        setSearch(true);
        return this;
    }

    public ButtonDropdown setColor(Color color) {
        this.color = color;
        return this;
    }

    public ButtonDropdown setSize(Size size) {
        this.size = size;
        return this;
    }
}
