package com.qxcmp.web.view.annotation.table;

import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.ColumnCount;
import com.qxcmp.web.view.support.Device;
import com.qxcmp.web.view.support.Size;

import java.lang.annotation.*;

/**
 * 实体表格
 * <p>
 * 该注解用于将实体对象转换为表格显示
 *
 * @author Aaric
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(EntityTables.class)
public @interface EntityTable {

    /**
     * 标题
     */
    String value() default "";

    /**
     * 数据表格名称
     * <p>
     * 用于在实体上区分多个表格
     */
    String name() default "";

    /**
     * 实体索引字段
     * <p>
     * 用于获取实体实例的字段，一般为主键
     */
    String entityIndex() default "id";

    /**
     * 表单操作的默认Url
     * <p>
     * 如果不填写将为类名转换后的Url
     */
    String action() default "";

    /**
     * 是否禁用掉过滤器
     */
    boolean disableFilter() default false;

    /**
     * 表格操作
     */
    TableAction[] tableActions() default {};

    /**
     * 批处理操作
     * <p>
     * 如果设置了批处理操作，表格将会生成多选列在第一列
     */
    BatchAction[] batchActions() default {};

    /**
     * 行操作
     */
    RowAction[] rowActions() default {};

    /**
     * 是否在单元格之间显示分隔符
     */
    boolean celled() default true;

    /**
     * 是否为基本表格
     * <p>
     * 该属性为移除表头和表尾的背景色
     */
    boolean basic() default false;

    /**
     * 是否为纯净表格
     * <p>
     * 该属性在基本表格的基础上再移除表格的外边框
     */
    boolean veryBasic() default false;

    /**
     * 是否为单行模式
     * <p>
     * 该模式下的单元格不会换行，始终保持单行
     */
    boolean singleLine() default false;

    /**
     * 是否为固定表格
     * <p>
     * 该属性将会使用 table-layout: fixed 作为数据表格布局
     * <p>
     * 如果和单行模式结合使用，显示不下的数据将以...表示
     */
    boolean fixed() default false;

    /**
     * 是否开始行高亮
     */
    boolean selectable() default true;

    /**
     * 是否显示单双行的条纹
     */
    boolean striped() default true;

    /**
     * 是否翻转颜色
     */
    boolean inverted() default false;

    /**
     * 是否为崩塌
     * <p>
     * 该属性会让单元格只占用实际宽度
     */
    boolean collapsing() default false;

    /**
     * 是否增加单元格的内边距
     */
    boolean padded() default false;

    /**
     * 是否更多的增加单元格的内边距
     */
    boolean veryPadded() default false;

    /**
     * 是否减少单元格的内边距
     */
    boolean compact() default false;

    /**
     * 是否更多的减少单元格的内边距
     */
    boolean veryCompact() default false;

    /**
     * 显示指定列的数量
     * <p>
     * 每一列都为等宽
     */
    ColumnCount columnCount() default ColumnCount.NONE;

    /**
     * 自动堆叠的设备类型
     * <p>
     * 数据表格默认不开启自动堆叠，如需要指定自动堆叠，使用此属性
     */
    Device stackDevice() default Device.NONE;

    /**
     * 颜色
     */
    Color color() default Color.NONE;

    /**
     * 大小
     */
    Size size() default Size.NONE;
}
