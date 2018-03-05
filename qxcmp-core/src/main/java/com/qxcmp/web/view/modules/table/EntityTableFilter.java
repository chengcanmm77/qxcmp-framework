package com.qxcmp.web.view.modules.table;

import com.qxcmp.web.view.elements.button.AbstractButton;
import com.qxcmp.web.view.elements.input.Input;
import com.qxcmp.web.view.modules.dropdown.Selection;
import lombok.Getter;

/**
 * 实体表格过滤组件
 *
 * @author Aaric
 */
@Getter
public class EntityTableFilter extends AbstractTableComponent {

    private Selection selection;
    private Input input;
    private AbstractButton button;

    public EntityTableFilter(Selection selection, Input input, AbstractButton button) {
        this.selection = selection;
        this.input = input;
        this.button = button;
    }

    @Override
    public String getFragmentName() {
        return "table-filter";
    }
}
