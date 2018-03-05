package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

/**
 * 标题1
 *
 * @author aaric
 */
@Getter
@Setter
public class H1 extends HeaderElement {

    public H1() {
    }

    public H1(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "h1";
    }
}
