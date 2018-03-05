package com.qxcmp.web.view.modules.table;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.VerticalAlignment;
import com.qxcmp.web.view.support.Wide;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * 数据表格单元格抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractTableCell extends AbstractTableComponent {

    /**
     * 单元格内容
     */
    private List<Component> components = Lists.newArrayList();

    /**
     * 单元格文本
     * <p>
     * 当单元格未设置内容时，将使用单元格文本作为内容填充
     */
    private String content;

    /**
     * 行跨度
     */
    private int rowSpan;

    /**
     * 列跨度
     */
    private int colSpan;

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
     * 是否为崩塌
     * <p>
     * 该属性会让单元格只占用实际宽度
     */
    private boolean collapsing;

    /**
     * 对齐方式
     */
    private Alignment alignment = Alignment.NONE;

    /**
     * 垂直对齐方式
     */
    private VerticalAlignment verticalAlignment = VerticalAlignment.NONE;

    /**
     * 是否为可选择
     * <p>
     * 在鼠标悬浮的时候高亮显示
     * <p>
     * 如果使用 a 超链接作为单元格内容，则会让整个单元格都可点击
     */
    private boolean selectable;

    /**
     * 单元格宽度
     */
    private Wide wide = Wide.NONE;

    public AbstractTableCell() {
    }

    public AbstractTableCell(String content) {
        this.content = content;
    }

    public AbstractTableCell(Component component) {
        this.components.add(component);
    }

    public AbstractTableCell addComponent(Component component) {
        this.components.add(component);
        return this;
    }

    public AbstractTableCell addComponents(Collection<? extends Component> components) {
        this.components.addAll(components);
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

        if (collapsing) {
            stringBuilder.append(" collapsing");
        }

        if (selectable) {
            stringBuilder.append(" selectable");
        }

        return stringBuilder.append(alignment).append(verticalAlignment).append(wide).toString();
    }

    public AbstractTableCell setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
        return this;
    }

    public AbstractTableCell setColSpan(int colSpan) {
        this.colSpan = colSpan;
        return this;
    }

    public AbstractTableCell setPositive() {
        setPositive(true);
        return this;
    }

    public AbstractTableCell setNegative() {
        setNegative(true);
        return this;
    }

    public AbstractTableCell setError() {
        setError(true);
        return this;
    }

    public AbstractTableCell setWarning() {
        setWarning(true);
        return this;
    }

    public AbstractTableCell setActive() {
        setActive(true);
        return this;
    }

    public AbstractTableCell setDisabled() {
        setDisabled(true);
        return this;
    }

    public AbstractTableCell setCollapsing() {
        setCollapsing(true);
        return this;
    }

    public AbstractTableCell setSelectable() {
        setSelectable(true);
        return this;
    }

    public AbstractTableCell setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public AbstractTableCell setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }
}
