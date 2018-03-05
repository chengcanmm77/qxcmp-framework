package com.qxcmp.web.view.modules.table;

public class TableDataCheckbox extends AbstractCheckboxTableCell {

    public TableDataCheckbox(String key) {
        super(key);
    }

    @Override
    public String getFragmentName() {
        return "table-data-checkbox";
    }
}
