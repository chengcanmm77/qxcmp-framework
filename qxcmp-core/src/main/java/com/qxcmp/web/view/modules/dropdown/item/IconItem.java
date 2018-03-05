package com.qxcmp.web.view.modules.dropdown.item;

import com.qxcmp.web.view.elements.icon.Icon;
import lombok.Getter;
import lombok.Setter;

/**
 * 带图标的选项
 *
 * @author Aaric
 */
@Getter
@Setter
public class IconItem extends AbstractTextDropdownItem implements DropdownItem, SelectionItem {

    /**
     * 选项图标
     */
    private Icon icon;

    public IconItem(String text, String iconName) {
        this(text, new Icon(iconName));
    }

    public IconItem(String text, Icon icon) {
        super(text);
        this.icon = icon;
    }

    public IconItem(String text, String value, String iconName) {
        super(text, value);
        this.icon = new Icon(iconName);
    }

    public IconItem(String text, String value, Icon icon) {
        super(text, value);
        this.icon = icon;
    }

    @Override
    public String getFragmentName() {
        return "item-icon";
    }
}
