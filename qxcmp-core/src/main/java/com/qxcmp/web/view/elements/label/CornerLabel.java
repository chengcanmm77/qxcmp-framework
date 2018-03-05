package com.qxcmp.web.view.elements.label;

import com.qxcmp.web.view.support.Corner;

/**
 * 角落标签
 *
 * @author Aaric
 */
public class CornerLabel extends Label {

    private Corner corner = Corner.LEFT;

    public CornerLabel(Corner corner) {
        super("");
        this.corner = corner;
    }

    public CornerLabel(String text, Corner corner) {
        super(text);
        this.corner = corner;
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + corner.toString();
    }
}
