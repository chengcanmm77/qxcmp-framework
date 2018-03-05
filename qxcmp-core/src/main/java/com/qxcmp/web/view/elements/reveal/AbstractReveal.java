package com.qxcmp.web.view.elements.reveal;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.Component;
import lombok.Getter;
import lombok.Setter;

/**
 * 展览框
 * <p>
 * 该元素包含可见和隐藏两个部分内容
 * <p>
 * 该元素为激活状态是显示隐藏内容
 * <p>
 * 应确保可见和隐藏部分具有相同比例
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractReveal extends AbstractComponent {

    /**
     * 可见部分，默认显示
     */
    private Component visibleContent;

    /**
     * 隐藏部分，为激活状态时显示
     */
    private Component hiddenContent;

    /**
     * 是否为激活状态
     */
    private boolean active;

    /**
     * 是否立即显示隐藏内容
     */
    private boolean instant;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    public AbstractReveal(Component visibleContent, Component hiddenContent) {
        this.visibleContent = visibleContent;
        this.hiddenContent = hiddenContent;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/reveal";
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

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        if (instant) {
            stringBuilder.append(" instant");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "reveal";
    }

    public AbstractReveal setActive() {
        setActive(true);
        return this;
    }

    public AbstractReveal setDisabled() {
        setDisabled(true);
        return this;
    }

    public AbstractReveal setInstant() {
        setInstant(true);
        return this;
    }
}
