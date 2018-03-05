package com.qxcmp.web.view.elements.list.item;

import com.qxcmp.web.view.elements.icon.Icon;
import lombok.Getter;
import lombok.Setter;

/**
 * 图标项目
 *
 * @author Aaric
 */
@Getter
@Setter
public class IconTextItem extends AbstractListItem {

    /**
     * 图标
     */
    private Icon icon;

    /**
     * 文本
     */
    private String text;

    public IconTextItem(Icon icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public IconTextItem(String iconName, String text) {
        this.icon = new Icon(iconName);
        this.text = text;
    }

    @Override
    public String getFragmentName() {
        return "item-icon-text";
    }
}
