package com.qxcmp.web.view.elements.button;

import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

/**
 * 图标按钮
 *
 * @author Aaric
 */
@Getter
@Setter
public class IconButton extends AbstractButton {

    private Icon icon;

    public IconButton(String iconName) {
        this.icon = new Icon(iconName);
    }

    public IconButton(Icon icon) {
        this.icon = icon;
    }

    public IconButton(Icon icon, String url) {
        super(url);
        this.icon = icon;
    }

    public IconButton(Icon icon, String url, AnchorTarget anchorTarget) {
        super(url, anchorTarget);
        this.icon = icon;
    }

    @Override
    public String getFragmentName() {
        return "icon";
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " icon";
    }
}
