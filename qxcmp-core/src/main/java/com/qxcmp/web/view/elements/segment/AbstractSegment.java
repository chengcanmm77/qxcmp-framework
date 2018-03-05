package com.qxcmp.web.view.elements.segment;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Floated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 片段基类
 * <p>
 * 片段一般用来创建一组相关的内容
 *
 * @author aaric
 */
@Getter
@Setter
public class AbstractSegment extends AbstractComponent implements Segmentable {

    /**
     * 内容是否为禁用状态
     */
    private boolean disabled;

    /**
     * 是否为加载状态
     */
    private boolean loading;

    /**
     * 是否增加内边距
     */
    private boolean padded;

    /**
     * 是否增加更多内边距
     */
    private boolean veryPadded;

    /**
     * 是否为紧凑模式
     * <p>
     * 该模式的片段宽度为内容实际宽度
     */
    private boolean compact;

    /**
     * 颜色
     */
    private Color color = Color.NONE;

    /**
     * 是否为次要片段
     * <p>
     * 该属性将减少片段的显著性
     */
    private boolean secondary;

    /**
     * 是否为再次要片段
     * <p>
     * 该属性将更多的减少片段的显著性
     */
    private boolean tertiary;

    /**
     * 是否清楚浮动
     */
    private boolean clearing;

    /**
     * 浮动类型
     */
    private Floated floated = Floated.NONE;

    /**
     * 内容对齐方式
     */
    private Alignment alignment = Alignment.NONE;

    /**
     * 片段内容
     */
    private List<Component> components = Lists.newArrayList();

    public AbstractSegment addComponent(Component component) {
        components.add(component);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/segment";
    }

    @Override
    public String getFragmentName() {
        return "segment";
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

        if (loading) {
            stringBuilder.append(" loading");
        }

        if (padded) {
            stringBuilder.append(" padded");
        } else if (veryPadded) {
            stringBuilder.append(" very padded");
        }

        if (compact) {
            stringBuilder.append(" compact");
        }

        stringBuilder.append(color.toString());

        if (secondary) {
            stringBuilder.append(" secondary");
        }

        if (tertiary) {
            stringBuilder.append(" tertiary");
        }

        if (clearing) {
            stringBuilder.append(" clearing");
        }

        stringBuilder.append(floated.toString());
        stringBuilder.append(alignment.toString());

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "segment";
    }

    public AbstractSegment setDisabled() {
        this.disabled = true;
        return this;
    }

    public AbstractSegment setLoading() {
        this.loading = true;
        return this;
    }

    public AbstractSegment setPadded() {
        this.padded = true;
        return this;
    }

    public AbstractSegment setVeryPadded() {
        this.veryPadded = true;
        return this;
    }

    public AbstractSegment setCompact() {
        this.compact = true;
        return this;
    }

    public AbstractSegment setColor(Color color) {
        this.color = color;
        return this;
    }

    public AbstractSegment setSecondary() {
        this.secondary = true;
        return this;
    }

    public AbstractSegment setTertiary() {
        this.tertiary = true;
        return this;
    }

    public AbstractSegment setClearing() {
        this.clearing = true;
        return this;
    }

    public AbstractSegment setFloated(Floated floated) {
        this.floated = floated;
        return this;
    }

    public AbstractSegment setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }
}
