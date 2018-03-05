package com.qxcmp.web.view.elements.input;

import com.qxcmp.web.view.elements.icon.Icon;
import lombok.Getter;
import lombok.Setter;

/**
 * 带图标的输入框
 *
 * @author Aaric
 */
@Getter
@Setter
public class IconInput extends AbstractInput {

    /**
     * 图标
     */
    private Icon icon;

    /**
     * 图标是否靠左显示
     */
    private boolean leftIcon;

    /**
     * 是否为加载状态
     */
    private boolean loading;

    public IconInput(Icon icon) {
        this.icon = icon;
    }

    public IconInput(Icon icon, String placeholder) {
        super(placeholder);
        this.icon = icon;
    }

    public IconInput(Icon icon, String placeholder, String name) {
        super(placeholder, name);
        this.icon = icon;
    }


    public IconInput(String iconName) {
        this.icon = new Icon(iconName);
    }

    public IconInput(String iconName, String placeholder) {
        super(placeholder);
        this.icon = new Icon(iconName);
    }

    public IconInput(String iconName, String placeholder, String name) {
        super(placeholder, name);
        this.icon = new Icon(iconName);
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassContent());

        if (loading) {
            stringBuilder.append(" loading");
        }

        if (leftIcon) {
            stringBuilder.append(" left icon");
        } else {
            stringBuilder.append(" icon");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getFragmentName() {
        return "icon";
    }

    public AbstractInput setLeftIcon() {
        setLeftIcon(true);
        return this;
    }

    public AbstractInput setLoading() {
        setLoading(true);
        return this;
    }
}
