package com.qxcmp.web.view.elements.segment;

/**
 * 堆积样式片段
 *
 * @author aaric
 * @see Segment
 */
public class PiledSegment extends AbstractSegment {
    @Override
    public String getClassContent() {
        return super.getClassContent() + " piled";
    }
}
