package com.qxcmp.web.view.elements.step;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

/**
 * 步骤组件
 *
 * @author Aaric
 */
@Getter
@Setter
public class Step extends AbstractComponent {

    /**
     * 超链接
     */
    private String url;

    /**
     * 超链接打开方式
     */
    private String urlTarget;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 图标
     */
    private Icon icon;

    /**
     * 是否为激活状态
     */
    private boolean active;

    /**
     * 是否为完成状态
     */
    private boolean completed;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    /**
     * 是否渲染为链接
     * <p>
     * 该属性无论是否设置Url都将会渲染为链接样式
     */
    private boolean link;

    public Step(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Step(String iconName, String title, String description) {
        this.icon = new Icon(iconName);
        this.title = title;
        this.description = description;
    }

    public Step(Icon icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/step";
    }

    @Override
    public String getFragmentName() {
        return "step";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (active) {
            stringBuilder.append(" active");
        }

        if (completed) {
            stringBuilder.append(" completed");
        }

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        if (link) {
            stringBuilder.append(" link");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "step";
    }

    public Step setUrl(String url) {
        this.url = url;
        return this;
    }

    public Step setUrlTarget(AnchorTarget target) {
        this.urlTarget = target.toString();
        return this;
    }

    public Step setActive() {
        setActive(true);
        return this;
    }

    public Step setCompleted() {
        setCompleted(true);
        return this;
    }

    public Step setDisabled() {
        setDisabled(true);
        return this;
    }

    public Step setLink() {
        setLink(true);
        return this;
    }
}
