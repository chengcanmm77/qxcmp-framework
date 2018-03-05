package com.qxcmp.web.view.elements.label;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.support.AnchorTarget;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 标签抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractLabel extends AbstractComponent {

    /**
     * 标签文本
     */
    private String text;

    /**
     * 标签补充文本
     * <p>
     * 补充文本仅只用于标准标签
     */
    private String details;

    /**
     * 标签图标
     */
    private Icon icon;

    /**
     * 标签图片
     */
    private String image;

    /**
     * 标签超链接
     */
    private String url;

    /**
     * 超链接打开方式
     */
    private String urlTarget;

    /**
     * 颜色
     */
    private Color color = Color.NONE;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    public AbstractLabel(String text) {
        this.text = text;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/label";
    }

    @Override
    public String getFragmentName() {
        return "label";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(color.toString()).append(size.toString());

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "label";
    }

    public AbstractLabel setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public AbstractLabel setImage(String image) {
        this.image = image;
        return this;
    }

    public AbstractLabel setDetails(String details) {
        this.details = details;
        return this;
    }

    public AbstractLabel setUrl(String url) {
        this.url = url;
        return this;
    }

    public AbstractLabel setUrl(String url, AnchorTarget target) {
        this.url = url;
        this.urlTarget = target.toString();
        return this;
    }

    public AbstractLabel setColor(Color color) {
        this.color = color;
        return this;
    }

    public AbstractLabel setSize(Size size) {
        this.size = size;
        return this;
    }
}
