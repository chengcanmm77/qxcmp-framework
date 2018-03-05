package com.qxcmp.web.view.elements.menu.item;

import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class LogoutItem extends AbstractMenuItem {

    private final String text;

    public LogoutItem(String text) {
        this.text = text;
    }

    @Override
    public String getFragmentName() {
        return "item-logout";
    }
}
