package com.qxcmp.web.view.elements.grid;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.support.ColumnCount;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 网格行组件
 *
 * @author Aaric
 */
@Getter
@Setter
public class Row extends AbstractGridItem {

    /**
     * 行的列数
     */
    private ColumnCount columnCount = ColumnCount.NONE;

    /**
     * 是否拉伸行
     * <p>
     * 该属性将拉伸行的内容占满列的最大高度
     */
    private boolean stretched;

    /**
     * 是否居中列元素
     * <p>
     * 当列没有占满行时，将居中显示列元素
     */
    private boolean centered;

    /**
     * 列元素
     */
    private List<Col> columns = Lists.newArrayList();

    public Row addCol(Col col) {
        columns.add(col);
        return this;
    }

    @Override
    public String getFragmentName() {
        return "row";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassContent());

        if (stretched) {
            stringBuilder.append(" stretched");
        }

        if (centered) {
            stringBuilder.append(" centered");
        }

        stringBuilder.append(columnCount.toString());

        return stringBuilder.toString();
    }

    @Override
    public String getClassPrefix() {
        return "row";
    }

    public Row setColumnCount(ColumnCount columnCount) {
        this.columnCount = columnCount;
        return this;
    }

    public Row setStretched() {
        this.stretched = true;
        return this;
    }

    public Row setCentered() {
        this.centered = true;
        return this;
    }
}
