package com.qxcmp.web.view.elements.menu;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.elements.menu.item.MenuItem;
import com.qxcmp.web.view.support.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public abstract class AbstractMenu extends AbstractComponent {

    /**
     * 是否为紧凑样式
     */
    private boolean compact;

    /**
     * 是否为次要菜单
     * <p>
     * 该属性将调整菜单外观以不再强调菜单的内容
     */
    private boolean secondary;

    /**
     * 是否为带指针菜单
     * <p>
     * 指针菜单可以指向相邻的元素以显示和相邻元素的关系
     */
    private boolean pointing;

    /**
     * 是否为 Tab 的菜单
     */
    private boolean tabular;

    /**
     * 是否为纯文本菜单
     */
    private boolean text;

    /**
     * 是否自动堆叠
     */
    private boolean stackable;

    /**
     * 是否翻转颜色
     */
    private boolean inverted;

    /**
     * 是否不显示边框
     */
    private boolean borderless;

    /**
     * 显示指定菜单项数量
     * <p>
     * 若使用该属性，所有菜单项将平分菜单宽度
     */
    private ItemCount itemCount = ItemCount.NONE;

    /**
     * 附着方式
     */
    private Attached attached = Attached.NONE;

    /**
     * 颜色
     */
    private Color color = Color.NONE;

    /**
     * 菜单大小
     */
    private Size size = Size.NONE;

    /**
     * 是否固定菜单
     */
    private Fixed fixed = Fixed.NONE;

    /**
     * 右侧菜单 - 可选
     */
    private RightMenu rightMenu;

    private List<MenuItem> items = Lists.newArrayList();

    public AbstractMenu addItem(MenuItem item) {
        items.add(item);
        return this;
    }

    public AbstractMenu addItems(Collection<? extends MenuItem> items) {
        this.items.addAll(items);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/menu";
    }

    @Override
    public String getFragmentName() {
        return "menu";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (compact) {
            stringBuilder.append(" compact");
        }

        if (secondary) {
            stringBuilder.append(" secondary");
        }

        if (pointing) {
            stringBuilder.append(" pointing");
        }

        if (text) {
            stringBuilder.append(" text");
        }

        if (tabular) {
            stringBuilder.append(" tabular");
        }

        if (stackable) {
            stringBuilder.append(" stackable");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        if (borderless) {
            stringBuilder.append(" borderless");
        }

        return stringBuilder.append(itemCount).append(color).append(size).append(attached).append(fixed).toString();
    }

    @Override
    public String getClassSuffix() {
        return "menu";
    }

    public AbstractMenu setRightMenu(RightMenu rightMen) {
        this.rightMenu = rightMen;
        return this;
    }

    public AbstractMenu setCompact() {
        setCompact(true);
        return this;
    }

    public AbstractMenu setSecondary() {
        setSecondary(true);
        return this;
    }

    public AbstractMenu setPointing() {
        setPointing(true);
        return this;
    }

    public AbstractMenu setText() {
        setText(true);
        return this;
    }

    public AbstractMenu setTabular() {
        setTabular(true);
        return this;
    }

    public AbstractMenu setStackable() {
        setStackable(true);
        return this;
    }

    public AbstractMenu setInverted() {
        setInverted(true);
        return this;
    }

    public AbstractMenu setBorderless() {
        setBorderless(true);
        return this;
    }

    public AbstractMenu setAttached(Attached attached) {
        this.attached = attached;
        return this;
    }

    public AbstractMenu setColor(Color color) {
        this.color = color;
        return this;
    }

    public AbstractMenu setSize(Size size) {
        this.size = size;
        return this;
    }

    public AbstractMenu setItemCount(ItemCount itemCount) {
        this.itemCount = itemCount;
        return this;
    }

    public AbstractMenu setFixed(Fixed fixed) {
        this.fixed = fixed;
        return this;
    }
}
