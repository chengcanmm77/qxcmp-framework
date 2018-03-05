package com.qxcmp.web.view.elements.button;

import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

/**
 * 带标签的图标按钮
 *
 * @author Aaric
 */
@Getter
@Setter
public class LabeledIconButton extends AbstractButton {

    /**
     * 按钮图标
     */
    private Icon icon;

    /**
     * 按钮文本
     */
    private String text;

    /**
     * 图标是否在右侧显示
     */
    private boolean rightIcon;

    public LabeledIconButton(String iconName, String text) {
        this.icon = new Icon(iconName);
        this.text = text;
    }

    public LabeledIconButton(String text, Icon icon) {
        this.icon = icon;
        this.text = text;
    }

    public LabeledIconButton(String text, Icon icon, String url) {
        super(url);
        this.icon = icon;
        this.text = text;
    }

    public LabeledIconButton(String text, Icon icon, String url, AnchorTarget anchorTarget) {
        super(url, anchorTarget);
        this.icon = icon;
        this.text = text;
    }

    @Override
    public String getFragmentName() {
        return "label-icon";
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + (rightIcon ? " right labeled icon" : " labeled icon");
    }

    public LabeledIconButton setRightIcon() {
        this.rightIcon = true;
        return this;
    }
}
