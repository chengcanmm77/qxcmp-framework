package com.qxcmp.web.view.elements.segment;

/**
 * 上升样式片段
 *
 * @author aaric
 * @see Segment
 */
public class RaisedSegment extends AbstractSegment {
    @Override
    public String getClassContent() {
        return super.getClassContent() + " raised";
    }
}
