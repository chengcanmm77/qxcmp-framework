package com.qxcmp.web.view.modules.dropdown.item;

import com.qxcmp.web.view.elements.label.AbstractLabel;
import com.qxcmp.web.view.elements.label.Label;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelItem extends AbstractTextDropdownItem implements DropdownItem, SelectionItem {

    /**
     * 选项标签内容，默认为空标签
     */
    private AbstractLabel label;

    public LabelItem(String text, String labelText) {
        this(text, new Label(labelText));
    }

    public LabelItem(String text, AbstractLabel label) {
        super(text);
        this.label = label;
    }

    public LabelItem(String text, String value, String labelText) {
        super(text, value);
        this.label = new Label(labelText);
    }

    public LabelItem(String text, String value, AbstractLabel label) {
        super(text, value);
        this.label = label;
    }

    @Override
    public String getFragmentName() {
        return "item-label";
    }

}
