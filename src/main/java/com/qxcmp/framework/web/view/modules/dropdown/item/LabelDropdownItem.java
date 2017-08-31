package com.qxcmp.framework.web.view.modules.dropdown.item;

import com.qxcmp.framework.web.view.elements.label.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LabelDropdownItem extends AbstractSelectionItem {

    /**
     * 选项标签内容，默认为空标签
     */
    private Label label;

    public LabelDropdownItem() {
        Label label = new Label("");
        label.setEmptyCircular(true);
        this.label = label;
    }

    public LabelDropdownItem(String text) {
        this.label = new Label(text);
    }

    public LabelDropdownItem(Label label) {
        this.label = label;
    }

    @Override
    public String getFragmentName() {
        return "item-label";
    }

    @Override
    public String getClassName() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassName());

        stringBuilder.append(" item");

        return stringBuilder.toString();
    }
}
