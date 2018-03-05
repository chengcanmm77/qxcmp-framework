package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Italic extends InlineElement {
    public Italic() {
    }

    public Italic(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "italic-element";
    }
}
