package com.qxcmp.web.view.modules.table;

import com.qxcmp.web.view.elements.button.AbstractButton;
import com.qxcmp.web.view.elements.input.Input;
import com.qxcmp.web.view.modules.dropdown.Selection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 实体表格过滤组件
 *
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public class EntityTableFilter extends AbstractTableComponent {

    private final Selection fieldSelection;
    private final Selection operationSelection;
    private final Input input;
    private final AbstractButton button;

    @Override
    public String getFragmentName() {
        return "table-filter";
    }
}
