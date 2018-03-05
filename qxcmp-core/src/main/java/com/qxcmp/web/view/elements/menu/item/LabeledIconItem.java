package com.qxcmp.web.view.elements.menu.item;

import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.elements.icon.Icon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabeledIconItem extends AbstractMenuItem {

    private String label;

    private Icon icon;

    private Anchor anchor;

    public LabeledIconItem(String label, Icon icon) {
        this.label = label;
        this.icon = icon;
    }

    public LabeledIconItem(String label, String iconName) {
        this.label = label;
        this.icon = new Icon(iconName);
    }

    public LabeledIconItem(String label, String iconName, Anchor anchor) {
        this.label = label;
        this.icon = new Icon(iconName);
        this.anchor = anchor;
    }

    public LabeledIconItem(String label, Icon icon, Anchor anchor) {
        this.label = label;
        this.icon = icon;
        this.anchor = anchor;
    }

    @Override
    public String getFragmentName() {
        return "item-labeled-icon";
    }


    public LabeledIconItem setAnchor(Anchor anchor) {
        this.anchor = anchor;
        return this;
    }
}
