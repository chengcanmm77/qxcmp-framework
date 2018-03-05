package com.qxcmp.web.view.elements.segment;

/**
 * 高堆叠样式片段
 *
 * @author aaric
 * @see StackedSegment
 */
public class TallStackedSegment extends StackedSegment {
    @Override
    public String getClassContent() {
        return super.getClassContent() + " tall";
    }
}
