package com.qxcmp.web.view.elements.menu.item;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class SubMenuItem extends AbstractMenuItem {

    private String text;

    private List<MenuItem> items = Lists.newArrayList();

    public SubMenuItem(String text) {
        this.text = text;
    }

    public SubMenuItem(String text, List<MenuItem> items) {
        this.text = text;
        this.items = items;
    }

    public SubMenuItem addItem(MenuItem item) {
        items.add(item);
        return this;
    }

    public SubMenuItem addItems(Collection<? extends MenuItem> items) {
        this.items.addAll(items);
        return this;
    }

    @Override
    public String getFragmentName() {
        return "item-sub-menu";
    }
}
