package com.qxcmp.web.view.elements.segment;

/**
 * 堆叠样式片段
 *
 * @author aaric
 * @see Segment
 */
public class StackedSegment extends AbstractSegment {
    @Override
    public String getClassContent() {
        return super.getClassContent() + " stacked";
    }
}
