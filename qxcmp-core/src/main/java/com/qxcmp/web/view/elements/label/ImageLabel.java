package com.qxcmp.web.view.elements.label;

/**
 * 图片标签
 *
 * @author Aaric
 */
public class ImageLabel extends Label {
    public ImageLabel(String text, String image) {
        super(text);
        setImage(image);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " image";
    }
}
