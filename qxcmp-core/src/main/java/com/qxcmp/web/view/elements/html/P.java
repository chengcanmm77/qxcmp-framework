package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

/**
 * 段落
 *
 * @author aaric
 */
@Getter
@Setter
public class P extends BlockElement {

    public P() {
    }

    public P(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "paragraph";
    }
}
