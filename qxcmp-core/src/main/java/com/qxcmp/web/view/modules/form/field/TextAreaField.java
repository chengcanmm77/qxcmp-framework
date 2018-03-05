package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextAreaField extends AbstractTextField {

    /**
     * 默认行数
     */
    private int rows;

    @Override
    public String getFragmentName() {
        return "field-text-area";
    }

    @Override
    public String getClassSuffix() {
        return "textarea " + super.getClassSuffix();
    }

    public TextAreaField setRows(int rows) {
        this.rows = rows;
        return this;
    }
}
