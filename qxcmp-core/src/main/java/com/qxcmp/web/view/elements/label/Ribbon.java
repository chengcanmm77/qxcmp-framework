package com.qxcmp.web.view.elements.label;

import lombok.Getter;
import lombok.Setter;

/**
 * 丝带标签
 *
 * @author Aaric
 */
@Getter
@Setter
public class Ribbon extends Label {

    private boolean right;

    public Ribbon(String text) {
        super(text);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + (right ? " right ribbon" : " ribbon");
    }

    public Ribbon setRight() {
        this.right = true;
        return this;
    }
}
