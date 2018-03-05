package com.qxcmp.web.view.elements.label;

/**
 * Tag标签
 *
 * @author Aaric
 */
public class Tag extends Label {
    public Tag(String text) {
        super(text);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " tag";
    }
}
