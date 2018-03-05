package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 文本输入框
 *
 * @author Aaric
 */
@Getter
@Setter
public class TextInputField extends AbstractTextField {

    @Override
    public String getFragmentName() {
        return "field-text-input";
    }

    @Override
    public String getClassSuffix() {
        return "text input " + super.getClassSuffix();
    }
}
