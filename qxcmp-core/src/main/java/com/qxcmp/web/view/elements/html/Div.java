package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

/**
 * 块级文档节
 *
 * @author aaric
 */
@Getter
@Setter
public class Div extends BlockElement {

    public Div() {
    }

    public Div(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "division";
    }
}
