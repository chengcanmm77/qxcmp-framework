package com.qxcmp.web.view.modules.table;

import com.qxcmp.web.view.Component;

public class TableHead extends AbstractTableCell {

    public TableHead() {
    }

    public TableHead(String content) {
        super(content);
    }

    public TableHead(Component component) {
        super(component);
    }

    @Override
    public String getFragmentName() {
        return "table-head";
    }
}
