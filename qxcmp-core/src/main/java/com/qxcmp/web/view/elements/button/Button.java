package com.qxcmp.web.view.elements.button;

import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

/**
 * 一般按钮
 *
 * @author Aaric
 */
@Getter
@Setter
public class Button extends AbstractButton {

    /**
     * 按钮文本
     */
    private String text;

    public Button(String text) {
        this.text = text;
    }

    public Button(String text, String url) {
        super(url);
        this.text = text;
    }

    public Button(String text, String url, AnchorTarget anchorTarget) {
        super(url, anchorTarget);
        this.text = text;
    }
}
