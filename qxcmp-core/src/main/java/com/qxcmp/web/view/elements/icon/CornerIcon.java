package com.qxcmp.web.view.elements.icon;

import com.qxcmp.web.view.support.Corner;
import lombok.Getter;
import lombok.Setter;

/**
 * 角落图标
 *
 * @author Aaric
 */
@Getter
@Setter
public class CornerIcon extends Icon {

    /**
     * 图标坐在角落
     */
    private Corner corner = Corner.BOTTOM_RIGHT;

    public CornerIcon(String icon) {
        super(icon);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + corner.toString();
    }

    public CornerIcon setCorner(Corner corner) {
        this.corner = corner;
        return this;
    }
}
