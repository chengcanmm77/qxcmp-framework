package com.qxcmp.web.view.elements.menu.item;

import com.qxcmp.web.view.elements.html.Anchor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeaderItem extends AbstractMenuItem {

    private String text;

    private Anchor anchor;

    public HeaderItem(String text) {
        this.text = text;
    }

    public HeaderItem(String text, Anchor anchor) {
        this.text = text;
        this.anchor = anchor;
    }

    @Override
    public String getFragmentName() {
        return "item-header";
    }

    @Override
    public String getClassSuffix() {
        return "header " + super.getClassSuffix();
    }

    public HeaderItem setAnchor(Anchor anchor) {
        this.anchor = anchor;
        return this;
    }
}
