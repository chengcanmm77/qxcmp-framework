package com.qxcmp.web.view.elements.list;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.list.item.AbstractListItem;
import com.qxcmp.web.view.support.Floated;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * 列表组件抽象类
 * <p>
 * 列表用来分组相关的内容
 *
 * @author Aaric
 */
@Getter
@Setter
public class List extends AbstractComponent {

    /**
     * 是否显示附加项
     */
    private boolean bulleted;

    /**
     * 是否显示顺序选项
     */
    private boolean ordered;

    /**
     * 是否为导航链接列表
     */
    private boolean link;

    /**
     * 是否为水平列表
     */
    private boolean horizontal;

    /**
     * 是否翻转颜色
     */
    private boolean inverted;

    /**
     * 是否为选择列表
     * <p>
     * 该属性会在鼠标悬浮的是否高亮选项
     */
    private boolean selection;

    /**
     * 是否显示动画
     * <p>
     * 该属性会在鼠标悬浮的是否出现动画
     */
    private boolean animated;

    /**
     * 是否增加项目之间的外边距
     */
    private boolean relaxed;

    /**
     * 是否更多的增加项目之间的外边距
     */
    private boolean veryRelaxed;

    /**
     * 是否在项目之间显示分隔符
     */
    private boolean divided;

    /**
     * 是否在项目之间和两端（水平列表：上下）显示分隔符
     */
    private boolean celled;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    /**
     * 浮动类型
     */
    private Floated floated = Floated.NONE;

    private java.util.List<AbstractListItem> items = Lists.newArrayList();

    public List addItem(AbstractListItem item) {
        items.add(item);
        return this;
    }

    public List addItems(Collection<? extends AbstractListItem> items) {
        this.items.addAll(items);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/list";
    }

    @Override
    public String getFragmentName() {
        return "list";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (bulleted) {
            stringBuilder.append(" bulleted");
        }

        if (ordered) {
            stringBuilder.append(" ordered");
        }

        if (link) {
            stringBuilder.append(" link");
        }

        if (horizontal) {
            stringBuilder.append(" horizontal");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        if (selection) {
            stringBuilder.append(" selection");
        }

        if (animated) {
            stringBuilder.append(" animated");
        }

        if (relaxed) {
            stringBuilder.append(" relaxed");
        } else if (veryRelaxed) {
            stringBuilder.append(" very relaxed");
        }

        if (divided) {
            stringBuilder.append(" divided");
        }

        if (celled) {
            stringBuilder.append(" celled");
        }

        return stringBuilder.append(size).append(floated).toString();
    }

    @Override
    public String getClassSuffix() {
        return "list";
    }

    public List setBulleted() {
        setBulleted(true);
        return this;
    }

    public List setOrdered() {
        setOrdered(true);
        return this;
    }

    public List setLink() {
        setLink(true);
        return this;
    }

    public List setInverted() {
        setInverted(true);
        return this;
    }

    public List setHorizontal() {
        setHorizontal(true);
        return this;
    }

    public List setSelection() {
        setSelection(true);
        return this;
    }

    public List setAnimated() {
        setAnimated(true);
        return this;
    }

    public List setRelaxed() {
        setRelaxed(true);
        return this;
    }

    public List setVeryRelaxed() {
        setVeryRelaxed(true);
        return this;
    }

    public List setDivided() {
        setDivided(true);
        return this;
    }

    public List setCelled() {
        setCelled(true);
        return this;
    }

    public List setSize(Size size) {
        this.size = size;
        return this;
    }

    public List setFloated(Floated floated) {
        this.floated = floated;
        return this;
    }
}
