package com.qxcmp.web.view.elements.rail;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 扶手组件抽象类
 * <p>
 * 扶手组件用于在主要内容的两边显示额外内容
 * <p>
 * 一般加上固定位置，用于显示广告或者其他信息
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractRail extends AbstractComponent {

    /**
     * 扶手内容
     */
    private Component component;

    /**
     * 是否显示在容器内部
     */
    private boolean internal;

    /**
     * 是否在容器中间显示分隔符
     */
    private boolean dividing;

    /**
     * 是否移除和容器之间的间距
     */
    private boolean attached;

    /**
     * 是否减少和容器之间的距离
     */
    private boolean close;

    /**
     * 是否更多的减少和容器之间的距离
     */
    private boolean veryClose;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    public AbstractRail(Component component) {
        this.component = component;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/rail";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (internal) {
            stringBuilder.append(" internal");
        }

        if (dividing) {
            stringBuilder.append(" dividing");
        }

        if (attached) {
            stringBuilder.append(" attached");
        }

        if (close) {
            stringBuilder.append(" close");
        } else if (veryClose) {
            stringBuilder.append(" very close");
        }

        return stringBuilder.append(size).toString();
    }

    @Override
    public String getClassSuffix() {
        return "rail";
    }

    public AbstractRail setInternal() {
        setInternal(true);
        return this;
    }

    public AbstractRail setDividing() {
        setDividing(true);
        return this;
    }

    public AbstractRail setAttached() {
        setAttached(true);
        return this;
    }

    public AbstractRail setClose() {
        setClose(true);
        return this;
    }

    public AbstractRail setVeryClose() {
        setVeryClose(true);
        return this;
    }

    public AbstractRail setSize(Size size) {
        this.size = size;
        return this;
    }
}
