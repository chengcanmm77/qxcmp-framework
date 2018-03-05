package com.qxcmp.web.view.modules.table;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * 数据表格分区抽象类
 * <p>
 * head, body, footer
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractTableSection extends AbstractTableComponent {

    /**
     * 是否占满宽度
     * <p>
     * 在表格开始 Definition 属性的时候同时启用该属性将显示第一列
     * <p>
     * 适用于 Header 和 Footer
     */
    private boolean fullWidth;

    /**
     * 分区行元素
     */
    private List<AbstractTableRow> rows = Lists.newArrayList();

    public AbstractTableSection addRow(AbstractTableRow row) {
        rows.add(row);
        return this;
    }

    public AbstractTableSection addRows(Collection<? extends AbstractTableRow> rows) {
        this.rows.addAll(rows);
        return this;
    }

    @Override
    public String getClassContent() {
        return fullWidth ? "full-width" : "";
    }
}
