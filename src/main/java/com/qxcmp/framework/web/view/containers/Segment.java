package com.qxcmp.framework.web.view.containers;

import com.google.common.collect.Lists;
import com.qxcmp.framework.web.view.AbstractComponent;
import com.qxcmp.framework.web.view.support.Alignment;
import com.qxcmp.framework.web.view.support.Color;
import com.qxcmp.framework.web.view.support.Direction;
import com.qxcmp.framework.web.view.support.Floating;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Segment extends AbstractSegment {

    /**
     * 是否为基本区块
     */
    private boolean basic;

    /**
     * 是否为上升样式
     */
    private boolean raised;

    /**
     * 是否为堆叠样式
     */
    private boolean stacked;

    /**
     * 是否为高堆叠样式
     */
    private boolean tallStacked;

    /**
     * 是否为堆积样式
     */
    private boolean piled;

    /**
     * 是否为垂直区块
     */
    private boolean vertical;

    /**
     * 是否为次要区块
     */
    private boolean secondary;

    /**
     * 是否为再次要区块
     */
    private boolean tertiary;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    /**
     * 是否为加载状态
     */
    private boolean loading;

    /**
     * 是否为颜色翻转
     */
    private boolean inverted;

    /**
     * 是否增加内边距
     */
    private boolean padded;

    /**
     * 是否增加大内边距
     */
    private boolean veryPadded;

    /**
     * 是否为紧凑区块
     */
    private boolean compact;

    /**
     * 是否为圆形区块
     */
    private boolean circular;

    /**
     * 是否为附着区块
     */
    private boolean attached;

    /**
     * 附着方向，当为附着区块的时候生效，方向仅支持 TOP, BOTTOM, NONE
     */
    private Direction attachedDirection = Direction.NONE;

    /**
     * 区块颜色
     */
    private Color color = Color.NONE;

    /**
     * 是否浮动
     */
    private Floating floating = Floating.NONE;

    /**
     * 区块子元素对齐方式
     */
    private Alignment alignment = Alignment.NONE;

    /**
     * 是否清除浮动内容
     */
    private boolean clearing;

    /**
     * 是否为容器样式
     */
    private boolean container;

    /**
     * 区块内容
     */
    private List<AbstractComponent> components = Lists.newArrayList();

    @Override
    public String getClassName() {
        StringBuilder stringBuilder = new StringBuilder("ui segment");

        if (basic) {
            stringBuilder.append(" basic");
        }

        if (raised) {
            stringBuilder.append(" raised");
        }

        if (stacked) {
            stringBuilder.append(" stacked");
        }

        if (tallStacked) {
            stringBuilder.append(" tall stacked");
        }

        if (piled) {
            stringBuilder.append(" piled");
        }

        if (vertical) {
            stringBuilder.append(" vertical");
        }

        if (secondary) {
            stringBuilder.append(" secondary");
        }

        if (tertiary) {
            stringBuilder.append(" tertiary");
        }

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        if (loading) {
            stringBuilder.append(" loading");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        if (padded) {
            stringBuilder.append(" padded");
        }

        if (veryPadded) {
            stringBuilder.append(" very padded");
        }

        if (compact) {
            stringBuilder.append(" compact");
        }

        if (circular) {
            stringBuilder.append(" circular");
        }

        if (attached) {
            if (StringUtils.isNotBlank(attachedDirection.getClassName())) {
                stringBuilder.append(" ").append(attachedDirection.getClassName());
            }
            stringBuilder.append(" attached");
        }

        if (StringUtils.isNotBlank(color.getClassName())) {
            stringBuilder.append(" ").append(color.getClassName());
        }

        if (StringUtils.isNotBlank(floating.getClassName())) {
            stringBuilder.append(" ").append(floating.getClassName());
        }

        if (StringUtils.isNotBlank(alignment.toString())) {
            stringBuilder.append(" ").append(alignment.toString());
        }

        if (clearing) {
            stringBuilder.append(" clearing");
        }

        if (container) {
            stringBuilder.append(" container");
        }

        return stringBuilder.toString();
    }
}
