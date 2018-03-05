package com.qxcmp.web.view.modules.dropdown.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextItem extends AbstractTextDropdownItem implements DropdownItem, SelectionItem {

    public TextItem(String text) {
        super(text);
    }

    public TextItem(String text, String value) {
        super(text, value);
    }

    @Override
    public String getFragmentName() {
        return "item-text";
    }
}
