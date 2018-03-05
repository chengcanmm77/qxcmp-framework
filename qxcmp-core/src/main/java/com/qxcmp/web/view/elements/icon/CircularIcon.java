package com.qxcmp.web.view.elements.icon;

/**
 * 圆形图标
 *
 * @author Aaric
 */
public class CircularIcon extends Icon {

    public CircularIcon(String icon) {
        super(icon);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " circular";
    }
}
