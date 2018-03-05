package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 布尔值选择框
 *
 * @author Aaric
 */
@Getter
@Setter
public class BooleanField extends AbstractFormField {

    /**
     * 样式
     */
    private BooleanFieldStyle style = BooleanFieldStyle.STANDARD;

    @Override
    public String getFragmentName() {
        return "field-boolean";
    }

    public String getCheckboxClass() {
        final StringBuilder stringBuilder = new StringBuilder("ui component checkbox");

        switch (style) {
            case STANDARD:
                break;
            case SLIDER:
                stringBuilder.append(" slider");
                break;
            case TOGGLE:
                stringBuilder.append(" toggle");
                break;
        }

        return stringBuilder.toString();
    }

}
