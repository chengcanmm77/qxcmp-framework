package com.qxcmp.web.view.elements.label;

import com.qxcmp.web.view.support.Attached;

/**
 * 附着标签
 * <p>
 * 该标签可附着在片段组件上
 *
 * @author Aaric
 */
public class AttachedLabel extends Label {

    private Attached attached = Attached.TOP;

    public AttachedLabel(String text) {
        super(text);
    }

    public AttachedLabel(String text, Attached attached) {
        super(text);
        this.attached = attached;
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + attached.toString();
    }
}
