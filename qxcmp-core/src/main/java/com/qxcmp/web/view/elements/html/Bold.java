package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bold extends InlineElement {
    public Bold() {
    }

    public Bold(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "bold-element";
    }
}
