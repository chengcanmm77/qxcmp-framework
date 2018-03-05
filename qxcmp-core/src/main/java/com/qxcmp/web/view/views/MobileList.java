package com.qxcmp.web.view.views;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class MobileList extends AbstractComponent {

    private List<MobileListItem> items = Lists.newArrayList();

    public MobileList addItem(MobileListItem item) {
        items.add(item);
        return this;
    }

    public MobileList addItems(Collection<? extends MobileListItem> items) {
        this.items.addAll(items);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/views/mobile-list";
    }

    @Override
    public String getFragmentName() {
        return "container";
    }

    @Override
    public String getClassPrefix() {
        return "qxcmp";
    }

    @Override
    public String getClassSuffix() {
        return "mobile list";
    }
}
