package com.qxcmp.web.view.modules.table;

import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.ColumnCount;
import com.qxcmp.web.view.support.Device;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据表格抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractTable extends AbstractTableComponent {

    /**
     * 显示指定列的数量
     * <p>
     * 每一列都为等宽
     */
    protected ColumnCount columnCount = ColumnCount.NONE;
    /**
     * 表头
     */
    private AbstractTableHeader header;
    /**
     * 表格内容
     */
    private AbstractTableBody body;
    /**
     * 表尾
     */
    private AbstractTableFooter footer;
    /**
     * 是否禁用掉过滤器
     */
    private boolean disableFilter;
    /**
     * 是否在单元格之间显示分隔符
     */
    private boolean celled;
    /**
     * 是否为基本表格
     * <p>
     * 该属性为移除表头和表尾的背景色
     */
    private boolean basic;
    /**
     * 是否为纯净表格
     * <p>
     * 该属性在基本表格的基础上再移除表格的外边框
     */
    private boolean veryBasic;
    /**
     * 是否为单行模式
     * <p>
     * 该模式下的单元格不会换行，始终保持单行
     */
    private boolean singleLine;
    /**
     * 是否为固定表格
     * <p>
     * 该属性将会使用 table-layout: fixed 作为数据表格布局
     * <p>
     * 如果和单行模式结合使用，显示不下的数据将以...表示
     */
    private boolean fixed;
    /**
     * 自动堆叠的设备类型
     * <p>
     * 数据表格默认不开启自动堆叠，如需要指定自动堆叠，使用此属性
     */
    private Device stackDevice = Device.NONE;
    /**
     * 是否开始行高亮
     */
    private boolean selectable;
    /**
     * 是否显示单双行的条纹
     */
    private boolean striped;
    /**
     * 是否翻转颜色
     */
    private boolean inverted;
    /**
     * 是否为崩塌
     * <p>
     * 该属性会让单元格只占用实际宽度
     */
    private boolean collapsing;
    /**
     * 是否增加单元格的内边距
     */
    private boolean padded;
    /**
     * 是否更多的增加单元格的内边距
     */
    private boolean veryPadded;
    /**
     * 是否减少单元格的内边距
     */
    private boolean compact;
    /**
     * 是否更多的减少单元格的内边距
     */
    private boolean veryCompact;
    /**
     * 颜色
     */
    private Color color = Color.NONE;

    /**
     * 大小
     */
    private Size size = Size.NONE;

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (celled) {
            stringBuilder.append(" celled");
        }

        if (basic) {
            stringBuilder.append(" basic");
        } else if (veryBasic) {
            stringBuilder.append(" very basic");
        }

        if (singleLine) {
            stringBuilder.append(" single line");
        }

        if (fixed) {
            stringBuilder.append(" fixed");
        }

        if (StringUtils.isNotBlank(stackDevice.toString())) {
            stringBuilder.append(" ").append(stackDevice).append(" stackable");
        } else {
            stringBuilder.append(" unstackable");
        }

        if (selectable) {
            stringBuilder.append(" selectable");
        }

        if (striped) {
            stringBuilder.append(" striped");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        if (collapsing) {
            stringBuilder.append(" collapsing");
        }

        if (padded) {
            stringBuilder.append(" padded");
        } else if (veryPadded) {
            stringBuilder.append(" very padded");
        }

        if (compact) {
            stringBuilder.append(" compact");
        } else if (veryCompact) {
            stringBuilder.append(" very compact");
        }

        return stringBuilder.append(columnCount).append(color).append(size).toString();
    }

    @Override
    public String getClassSuffix() {
        return "table";
    }

    public AbstractTable setHeader(AbstractTableHeader header) {
        this.header = header;
        return this;
    }

    public AbstractTable setBody(AbstractTableBody body) {
        this.body = body;
        return this;
    }

    public AbstractTable setFooter(AbstractTableFooter footer) {
        this.footer = footer;
        return this;
    }

    public AbstractTable setCelled() {
        setCelled(true);
        return this;
    }

    public AbstractTable setBasic() {
        setBasic(true);
        return this;
    }

    public AbstractTable setVeryBasic() {
        setVeryBasic(true);
        return this;
    }

    public AbstractTable setSingleLine() {
        setSingleLine(true);
        return this;
    }

    public AbstractTable setFixed() {
        setFixed(true);
        return this;
    }

    public AbstractTable setStackDevice(Device device) {
        this.stackDevice = device;
        return this;
    }

    public AbstractTable setSelectable() {
        setSelectable(true);
        return this;
    }

    public AbstractTable setStriped() {
        setStriped(true);
        return this;
    }

    public AbstractTable setInverted() {
        setInverted(true);
        return this;
    }

    public AbstractTable setCollapsing() {
        setCollapsing(true);
        return this;
    }

    public AbstractTable setPadded() {
        setPadded(true);
        return this;
    }

    public AbstractTable setVeryPadded() {
        setVeryPadded(true);
        return this;
    }

    public AbstractTable setCompact() {
        setCompact(true);
        return this;
    }

    public AbstractTable setVeryCompact() {
        setVeryCompact(true);
        return this;
    }

    public AbstractTable setColumnCount(ColumnCount columnCount) {
        this.columnCount = columnCount;
        return this;
    }

    public AbstractTable setColor(Color color) {
        this.color = color;
        return this;
    }

    public AbstractTable setSize(Size size) {
        this.size = size;
        return this;
    }
}
