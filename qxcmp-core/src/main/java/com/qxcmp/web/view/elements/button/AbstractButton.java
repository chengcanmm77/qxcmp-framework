package com.qxcmp.web.view.elements.button;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.html.Anchor;
import com.qxcmp.web.view.support.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 按钮基本类
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractButton extends AbstractComponent {

    /**
     * 按键索引
     */
    private int tabIndex;

    /**
     * 按钮是否为超链接
     */
    private Anchor anchor;

    /**
     * 是否为激活状态
     */
    private boolean active;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    /**
     * 是否为加载状态
     */
    private boolean loading;

    /**
     * 按钮类型
     */
    private ButtonType type = ButtonType.NONE;

    /**
     * 按钮颜色
     */
    private Color color = Color.NONE;

    /**
     * 按钮大小
     */
    private Size size = Size.NONE;

    /**
     * 浮动类型
     */
    private Floated floated = Floated.NONE;

    /**
     * 是否为紧凑按钮
     */
    private boolean compact;

    /**
     * 是否占满父元素宽度
     */
    private boolean fluid;

    /**
     * 是否为圆形按钮
     */
    private boolean circular;

    /**
     * 是否翻转颜色
     */
    private boolean inverted;

    /**
     * 是否为主要按钮
     */
    private boolean primary;

    /**
     * 是否为次要按钮
     */
    private boolean secondary;

    /**
     * 是否为基本按钮
     */
    private boolean basic;

    /**
     * 附着方式
     */
    private Attached attached = Attached.NONE;

    public AbstractButton() {
    }

    public AbstractButton(String url) {
        this.anchor = new Anchor("", url);
    }

    public AbstractButton(String url, AnchorTarget anchorTarget) {
        this.anchor = new Anchor("", url, anchorTarget.toString());
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/button";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassSuffix() {
        return "button";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (active) {
            stringBuilder.append(" active");
        }

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        if (loading) {
            stringBuilder.append(" loading");
        }

        if (compact) {
            stringBuilder.append(" compact");
        }

        if (fluid) {
            stringBuilder.append(" fluid");
        }

        if (circular) {
            stringBuilder.append(" circular");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        if (primary) {
            stringBuilder.append(" primary");
        }

        if (secondary) {
            stringBuilder.append(" secondary");
        }

        if (basic) {
            stringBuilder.append(" basic");
        }

        stringBuilder.append(color.toString()).append(size.toString()).append(floated.toString()).append(attached.toString());

        return stringBuilder.toString();
    }

    public AbstractButton setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
        return this;
    }

    public AbstractButton setActive() {
        this.active = true;
        return this;
    }

    public AbstractButton setDisabled() {
        this.disabled = true;
        return this;
    }

    public AbstractButton setLoading() {
        this.loading = true;
        return this;
    }

    public AbstractButton setType(ButtonType type) {
        this.type = type;
        return this;
    }

    public AbstractButton setColor(Color color) {
        this.color = color;
        return this;
    }

    public AbstractButton setSize(Size size) {
        this.size = size;
        return this;
    }

    public AbstractButton setFloated(Floated floated) {
        this.floated = floated;
        return this;
    }

    public AbstractButton setCompact() {
        this.compact = true;
        return this;
    }

    public AbstractButton setFluid() {
        this.fluid = true;
        return this;
    }

    public AbstractButton setCircular() {
        this.circular = true;
        return this;
    }

    public AbstractButton setInverted() {
        this.inverted = true;
        return this;
    }

    public AbstractButton setPrimary() {
        this.primary = true;
        return this;
    }

    public AbstractButton setSecondary() {
        this.secondary = true;
        return this;
    }

    public AbstractButton setBasic() {
        this.basic = true;
        return this;
    }

    public AbstractButton setAttached(Attached attached) {
        this.attached = attached;
        return this;
    }
}
