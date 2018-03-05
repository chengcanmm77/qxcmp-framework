package com.qxcmp.web.view.modules.table;

import com.qxcmp.web.view.Component;

public class TableData extends AbstractTableCell {

    public TableData() {
    }

    public TableData(String content) {
        super(content);
    }

    public TableData(Component component) {
        super(component);
    }

    @Override
    public String getFragmentName() {
        return "table-data";
    }
}
