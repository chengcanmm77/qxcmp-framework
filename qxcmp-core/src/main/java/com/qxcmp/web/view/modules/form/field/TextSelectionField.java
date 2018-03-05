package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 文本下拉框
 *
 * @author Aaric
 */
@Getter
@Setter
public class TextSelectionField extends AbstractSelectionField {

    @Override
    public String getFragmentName() {
        return "field-selection-text";
    }
}
