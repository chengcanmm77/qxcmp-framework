package com.qxcmp.web.view.modules.table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableHeadCheckbox extends AbstractCheckboxTableCell {

    public TableHeadCheckbox(String key) {
        super(key);
    }

    @Override
    public String getFragmentName() {
        return "table-head-checkbox";
    }
}
