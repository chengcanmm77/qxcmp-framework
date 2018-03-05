package com.qxcmp.web.view.elements.icon;

/**
 * 方形图标
 *
 * @author Aaric
 */
public class BorderedIcon extends Icon {

    public BorderedIcon(String icon) {
        super(icon);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " bordered";
    }
}
