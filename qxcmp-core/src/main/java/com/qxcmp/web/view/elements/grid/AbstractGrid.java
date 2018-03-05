package com.qxcmp.web.view.elements.grid;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.ColumnCount;
import com.qxcmp.web.view.support.VerticalAlignment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 网格抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractGrid extends AbstractComponent {

    /**
     * 垂直对齐方式
     */
    protected VerticalAlignment verticalAlignment = VerticalAlignment.NONE;
    /**
     * 是否开启等宽列
     */
    private boolean equalWidth;
    /**
     * 是否增加网格内边距
     * <p>
     * 该属性会同时增加水平和垂直内边距
     */
    private boolean padded;
    /**
     * 是否增加网格垂直内边距
     */
    private boolean verticallyPadded;
    /**
     * 是否增加网格水平内边距
     */
    private boolean horizontallyPadded;
    /**
     * 是否增加槽宽度
     */
    private boolean relaxed;
    /**
     * 是否大幅增加槽宽度
     */
    private boolean veryRelaxed;
    /**
     * 是否居中列元素
     * <p>
     * 当列没有占满行时，将居中显示列元素
     */
    private boolean centered;
    /**
     * 文本对齐方式
     */
    private Alignment alignment = Alignment.NONE;
    /**
     * 是否为容器样式
     */
    private boolean container;

    /**
     * 是否为文本容器
     */
    private boolean textContainer;

    /**
     * 网格列数
     */
    private ColumnCount columnCount = ColumnCount.NONE;

    /**
     * 是否在非电脑端双倍排列列元素
     */
    private boolean doubling;

    /**
     * 是否自动堆叠
     * <p>
     * 将在移动端尺寸下自动堆叠
     */
    private boolean stackable;

    /**
     * 是否在电脑端颠倒列顺序
     */
    private boolean computerReversed;

    /**
     * 是否在平板端颠倒列顺序
     */
    private boolean tabletReversed;

    /**
     * 是否在移动端颠倒列顺序
     */
    private boolean mobileReversed;

    /**
     * 是否在电脑端颠倒行顺序
     */
    private boolean computerVerticallyReversed;

    /**
     * 是否在平板端颠倒行顺序
     */
    private boolean tabletVerticallyReversed;

    /**
     * 是否在移动端颠倒行顺序
     */
    private boolean mobileVerticallyReversed;

    /**
     * 网格内容
     * <p>
     * 网格内容必须为行或者列
     */
    private List<AbstractGridItem> items = Lists.newArrayList();

    public AbstractGrid addItem(AbstractGridItem item) {
        items.add(item);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/grid";
    }

    @Override
    public String getFragmentName() {
        return "grid";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (equalWidth) {
            stringBuilder.append(" equal width");
        }

        if (padded) {
            stringBuilder.append(" padded");
        } else if (verticallyPadded) {
            stringBuilder.append(" vertically padded");
        } else if (horizontallyPadded) {
            stringBuilder.append(" horizontally padded");
        }

        if (relaxed) {
            stringBuilder.append(" relaxed");
        } else if (veryRelaxed) {
            stringBuilder.append(" very relaxed");
        }

        if (centered) {
            stringBuilder.append(" centered");
        }

        if (container) {
            stringBuilder.append(" container");
        } else if (textContainer) {
            stringBuilder.append(" text container");
        }

        if (doubling) {
            stringBuilder.append(" doubling");
        }

        if (stackable) {
            stringBuilder.append(" stackable");
        }

        if (computerReversed) {
            stringBuilder.append(" computer reversed");
        }

        if (tabletReversed) {
            stringBuilder.append(" tablet reversed");
        }

        if (mobileReversed) {
            stringBuilder.append(" mobile reversed");
        }

        if (computerVerticallyReversed) {
            stringBuilder.append(" computer vertically reversed");
        }

        if (tabletVerticallyReversed) {
            stringBuilder.append(" tablet vertically reversed");
        }

        if (mobileVerticallyReversed) {
            stringBuilder.append(" mobile vertically reversed");
        }

        stringBuilder.append(columnCount.toString()).append(alignment.toString()).append(verticalAlignment.toString());

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "grid";
    }

    public AbstractGrid setEqualWidth() {
        this.equalWidth = true;
        return this;
    }

    public AbstractGrid setPadded() {
        this.padded = true;
        return this;
    }

    public AbstractGrid setVerticallyPadded() {
        this.verticallyPadded = true;
        return this;
    }

    public AbstractGrid setHorizontallyPadded() {
        this.horizontallyPadded = true;
        return this;
    }

    public AbstractGrid setRelaxed() {
        this.relaxed = true;
        return this;
    }

    public AbstractGrid setVeryRelaxed() {
        this.veryRelaxed = true;
        return this;
    }

    public AbstractGrid setCentered() {
        this.centered = true;
        return this;
    }

    public AbstractGrid setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public AbstractGrid setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }

    public AbstractGrid setContainer() {
        this.container = true;
        return this;
    }

    public AbstractGrid setTextContainer() {
        setTextContainer(true);
        return this;
    }

    public AbstractGrid setColumnCount(ColumnCount columnCount) {
        this.columnCount = columnCount;
        return this;
    }

    public AbstractGrid setDoubling() {
        this.doubling = true;
        return this;
    }

    public AbstractGrid setStackable() {
        this.stackable = true;
        return this;
    }

    public AbstractGrid setComputerReversed() {
        this.computerReversed = true;
        return this;
    }

    public AbstractGrid setTableReversed() {
        this.tabletReversed = true;
        return this;
    }

    public AbstractGrid setMobileReversed() {
        this.mobileReversed = true;
        return this;
    }

    public AbstractGrid setComputerVerticallyReversed() {
        this.computerVerticallyReversed = true;
        return this;
    }

    public AbstractGrid setTabletVerticallyReversed() {
        this.tabletVerticallyReversed = true;
        return this;
    }

    public AbstractGrid setMobileVerticallyReversed() {
        this.mobileVerticallyReversed = true;
        return this;
    }
}
