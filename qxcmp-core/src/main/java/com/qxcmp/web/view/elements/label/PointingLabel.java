package com.qxcmp.web.view.elements.label;

import com.qxcmp.web.view.support.Pointing;

/**
 * 带指针的标签
 *
 * @author Aaric
 */
public class PointingLabel extends Label {

    private Pointing pointing = Pointing.TOP_POINTING;

    public PointingLabel(String text) {
        super(text);
    }

    public PointingLabel(String text, Pointing pointing) {
        super(text);
        this.pointing = pointing;
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + pointing.toString();
    }
}
