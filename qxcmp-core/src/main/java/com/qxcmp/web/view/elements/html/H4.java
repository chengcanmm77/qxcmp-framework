package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

/**
 * 标题4
 *
 * @author aaric
 */
@Getter
@Setter
public class H4 extends HeaderElement {

    public H4() {

    }

    public H4(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "h4";
    }
}
