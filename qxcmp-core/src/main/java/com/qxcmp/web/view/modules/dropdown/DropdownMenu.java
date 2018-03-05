package com.qxcmp.web.view.modules.dropdown;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.modules.dropdown.item.DropdownItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DropdownMenu extends AbstractDropdownMenu {

    private List<DropdownItem> items = Lists.newArrayList();

    public DropdownMenu addItem(DropdownItem item) {
        items.add(item);
        return this;
    }

    public DropdownMenu addItems(List<? extends DropdownItem> items) {
        this.items.addAll(items);
        return this;
    }
}
