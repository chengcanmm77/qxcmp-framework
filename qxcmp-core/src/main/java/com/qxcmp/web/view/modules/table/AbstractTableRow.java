package com.qxcmp.web.view.modules.table;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.VerticalAlignment;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * 数据表格行组件抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractTableRow extends AbstractTableComponent {

    /**
     * 是否为正确状态
     */
    private boolean positive;

    /**
     * 是否为不正确状态
     */
    private boolean negative;

    /**
     * 是否为错误状态
     */
    private boolean error;

    /**
     * 是否为警告状态
     */
    private boolean warning;

    /**
     * 是否为激活状态
     */
    private boolean active;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    /**
     * 对齐方式
     */
    private Alignment alignment = Alignment.NONE;

    /**
     * 垂直对齐方式
     */
    private VerticalAlignment verticalAlignment = VerticalAlignment.NONE;

    /**
     * 行单元格
     */
    private List<AbstractTableCell> cells = Lists.newArrayList();

    public AbstractTableRow addCell(AbstractTableCell cell) {
        cells.add(cell);
        return this;
    }

    public AbstractTableRow addCells(Collection<? extends AbstractTableCell> cells) {
        this.cells.addAll(cells);
        return this;
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (positive) {
            stringBuilder.append(" positive");
        }

        if (negative) {
            stringBuilder.append(" negative");
        }

        if (error) {
            stringBuilder.append(" error");
        }

        if (warning) {
            stringBuilder.append(" warning");
        }

        if (active) {
            stringBuilder.append(" active");
        }

        if (disabled) {
            stringBuilder.append(" disabled");
        }

        return stringBuilder.append(alignment).append(verticalAlignment).toString();
    }

    public AbstractTableRow setPositive() {
        setPositive(true);
        return this;
    }

    public AbstractTableRow setNegative() {
        setNegative(true);
        return this;
    }

    public AbstractTableRow setError() {
        setError(true);
        return this;
    }

    public AbstractTableRow setWarning() {
        setWarning(true);
        return this;
    }

    public AbstractTableRow setActive() {
        setActive(true);
        return this;
    }

    public AbstractTableRow setDisabled() {
        setDisabled(true);
        return this;
    }

    public AbstractTableRow setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public AbstractTableRow setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }
}
