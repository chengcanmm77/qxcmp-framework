package com.qxcmp.web.view.elements.step;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Attached;
import com.qxcmp.web.view.support.ItemCount;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * 步骤组件
 *
 * @author Aaric
 */
@Getter
@Setter
public class Steps extends AbstractComponent {

    /**
     * 是否显示顺序
     * <p>
     * 在该属性下步骤不支持使用图标
     * <p>
     * 图标由步骤的状态确定
     */
    private boolean ordered;

    /**
     * 是否垂直显示步骤
     */
    private boolean vertical;

    /**
     * 是否自动堆叠
     */
    private boolean stackable;

    /**
     * 移除自动堆叠
     */
    private boolean unstackable;

    /**
     * 是否占满宽度
     */
    private boolean fluid;

    /**
     * 附着方式
     */
    private Attached attached = Attached.NONE;

    /**
     * 显示指定步骤个数
     * <p>
     * 步骤将等宽度显示
     */
    private ItemCount itemCount = ItemCount.NONE;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    /**
     * 步骤
     */
    private List<Step> steps = Lists.newArrayList();

    public Steps addStep(Step step) {
        steps.add(step);
        return this;
    }

    public Steps addSteps(Collection<? extends Step> steps) {
        this.steps.addAll(steps);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/step";
    }

    @Override
    public String getFragmentName() {
        return "steps";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (ordered) {
            stringBuilder.append(" ordered");
        }

        if (vertical) {
            stringBuilder.append(" vertical");
        }

        if (stackable) {
            stringBuilder.append(" stackable");
        }

        if (unstackable) {
            stringBuilder.append(" unstackable");
        }

        if (fluid) {
            stringBuilder.append(" fluid");
        }

        return stringBuilder.append(attached).append(itemCount).append(size).toString();
    }

    @Override
    public String getClassSuffix() {
        return "steps";
    }

    public Steps setOrdered() {
        setOrdered(true);
        return this;
    }

    public Steps setVertical() {
        setVertical(true);
        return this;
    }

    public Steps setStackable() {
        setStackable(true);
        return this;
    }

    public Steps setUnstackable() {
        setUnstackable(true);
        return this;
    }

    public Steps setFluid() {
        setFluid(true);
        return this;
    }

    public Steps setAttached(Attached attached) {
        this.attached = attached;
        return this;
    }

    public Steps setItemCount(ItemCount itemCount) {
        this.itemCount = itemCount;
        return this;
    }

    public Steps setSize(Size size) {
        this.size = size;
        return this;
    }
}
