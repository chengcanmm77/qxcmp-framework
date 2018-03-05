package com.qxcmp.web.view.elements.menu.item;

import com.qxcmp.web.view.elements.input.AbstractInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputItem extends AbstractMenuItem {

    private AbstractInput input;

    public InputItem(AbstractInput input) {
        this.input = input;
    }

    @Override
    public String getFragmentName() {
        return "item-input";
    }
}
