package com.qxcmp.web.view.elements.menu.item;

import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.elements.icon.Icon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IconItem extends AbstractMenuItem {

    private Icon icon;

    private Anchor anchor;

    public IconItem(String iconName) {
        this.icon = new Icon(iconName);
    }

    public IconItem(Icon icon) {
        this.icon = icon;
    }

    public IconItem(String iconName, Anchor anchor) {
        this.icon = new Icon(iconName);
        this.anchor = anchor;
    }

    public IconItem(Icon icon, Anchor anchor) {
        this.icon = icon;
        this.anchor = anchor;
    }

    @Override
    public String getFragmentName() {
        return "item-icon";
    }

    public IconItem setAnchor(Anchor anchor) {
        this.anchor = anchor;
        return this;
    }
}
