package com.qxcmp.web.view.modules.dropdown;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.modules.dropdown.item.SelectionItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SelectionMenu extends AbstractDropdownMenu {

    private List<SelectionItem> items = Lists.newArrayList();

    public SelectionMenu addItem(SelectionItem item) {
        items.add(item);
        return this;
    }

    public SelectionMenu addItems(List<? extends SelectionItem> items) {
        this.items.addAll(items);
        return this;
    }
}
