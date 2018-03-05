package com.qxcmp.web.view.modules.dropdown.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DividerItem extends AbstractDropdownItem implements DropdownItem {
    @Override
    public String getFragmentName() {
        return "item-divider";
    }
}
