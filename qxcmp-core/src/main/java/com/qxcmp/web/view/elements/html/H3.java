package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

/**
 * 标题3
 *
 * @author aaric
 */
@Getter
@Setter
public class H3 extends HeaderElement {

    public H3() {
    }

    public H3(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "h3";
    }
}
