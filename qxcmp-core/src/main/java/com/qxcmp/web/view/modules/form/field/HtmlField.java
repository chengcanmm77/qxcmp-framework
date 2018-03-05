package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * HTML 字段编辑器
 *
 * @author Aaric
 */
@Getter
@Setter
public class HtmlField extends BaseHTMLField {

    private int rows;

    @Override
    public String getFragmentName() {
        return "field-html";
    }
}
