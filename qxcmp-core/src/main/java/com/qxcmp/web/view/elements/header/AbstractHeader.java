package com.qxcmp.web.view.elements.header;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Attached;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Floated;
import lombok.Getter;
import lombok.Setter;

/**
 * 抬头
 * <p>
 * 抬头用来对一部分内容进行概述
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractHeader extends AbstractComponent {

    /**
     * 标题文本
     */
    private String title;

    /**
     * 可选：子标题文本
     */
    private String subTitle;

    /**
     * 可选：图标
     */
    private Icon icon;

    /**
     * 可选：图片
     */
    private Image image;

    /**
     * 对齐方式
     */
    private Alignment alignment = Alignment.NONE;

    /**
     * 是否禁用
     */
    private boolean disabled;

    /**
     * 是否有分隔符
     */
    private boolean dividing;

    /**
     * 是否为块状
     */
    private boolean block;

    /**
     * 附着类型
     */
    private Attached attached = Attached.NONE;

    /**
     * 浮动类型
     */
    private Floated floated = Floated.NONE;

    /**
     * 颜色
     */
    private Color color = Color.NONE;

    /**
     * 是否为反色
     */
    private boolean inverted;

    public AbstractHeader(String title) {
        this.title = title;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/header";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        if (dividing) {
            stringBuilder.append(" dividing");
        }

        if (block) {
            stringBuilder.append(" block");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        stringBuilder.append(alignment.toString()).append(attached.toString()).append(floated.toString()).append(color.toString());

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "header";
    }

    public AbstractHeader setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public AbstractHeader setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public AbstractHeader setImage(Image image) {
        this.image = image;
        return this;
    }

    public AbstractHeader setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public AbstractHeader setDisabled() {
        this.disabled = true;
        return this;
    }

    public AbstractHeader setDividing() {
        this.dividing = true;
        return this;
    }

    public AbstractHeader setBlock() {
        this.block = true;
        return this;
    }

    public AbstractHeader setAttached(Attached attached) {
        this.attached = attached;
        return this;
    }

    public AbstractHeader setFloated(Floated floating) {
        this.floated = floating;
        return this;
    }

    public AbstractHeader setColor(Color color) {
        this.color = color;
        return this;
    }

    public AbstractHeader setInverted() {
        this.inverted = true;
        return this;
    }
}
