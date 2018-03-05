package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

/**
 * 标题6
 *
 * @author aaric
 */
@Getter
@Setter
public class H6 extends HeaderElement {

    public H6() {
    }

    public H6(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "h6";
    }
}
