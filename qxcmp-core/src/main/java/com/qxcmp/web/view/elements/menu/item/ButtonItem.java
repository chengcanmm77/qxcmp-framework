package com.qxcmp.web.view.elements.menu.item;

import com.qxcmp.web.view.elements.button.AbstractButton;
import com.qxcmp.web.view.elements.button.Button;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ButtonItem extends AbstractMenuItem {

    private AbstractButton button;

    public ButtonItem(String text) {
        this.button = new Button(text);
    }

    public ButtonItem(AbstractButton button) {
        this.button = button;
    }

    @Override
    public String getFragmentName() {
        return "item-button";
    }
}
