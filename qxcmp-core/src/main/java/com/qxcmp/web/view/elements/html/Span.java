package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

/**
 * 内联文档节
 *
 * @author aaric
 */
@Getter
@Setter
public class Span extends InlineElement {

    public Span() {
    }

    public Span(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "span-element";
    }
}
