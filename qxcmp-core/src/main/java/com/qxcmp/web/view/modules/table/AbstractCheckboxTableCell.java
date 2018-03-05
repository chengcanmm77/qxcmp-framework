package com.qxcmp.web.view.modules.table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractCheckboxTableCell extends AbstractTableCell {

    /**
     * 选择框对应的实体主键
     */
    private String key;

    public AbstractCheckboxTableCell(String key) {
        this.key = key;
    }

    @Override
    public String getClassSuffix() {
        return "selection column";
    }
}
