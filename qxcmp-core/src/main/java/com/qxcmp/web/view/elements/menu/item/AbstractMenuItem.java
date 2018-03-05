package com.qxcmp.web.view.elements.menu.item;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractMenuItem extends AbstractComponent implements MenuItem {

    /**
     * 菜单项颜色
     */
    private Color color = Color.NONE;

    /**
     * 是否为链接样式
     */
    private boolean link;

    /**
     * 是否为激活状态
     */
    private boolean active;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/menu";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (link) {
            stringBuilder.append(" link");
        }

        if (active) {
            stringBuilder.append(" active");
        }

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        return stringBuilder.append(color).toString();
    }

    @Override
    public String getClassSuffix() {
        return "item";
    }

    public AbstractMenuItem setLink() {
        setLink(true);
        return this;
    }

    public AbstractMenuItem setActive() {
        setActive(true);
        return this;
    }

    public AbstractMenuItem setDisabled() {
        setDisabled(true);
        return this;
    }

    public AbstractMenuItem setColor(Color color) {
        this.color = color;
        return this;
    }
}
