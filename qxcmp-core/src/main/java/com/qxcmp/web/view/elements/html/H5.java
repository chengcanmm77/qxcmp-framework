package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

/**
 * 标题5
 *
 * @author aaric
 */
@Getter
@Setter
public class H5 extends HeaderElement {

    public H5() {
    }

    public H5(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "h5";
    }
}
