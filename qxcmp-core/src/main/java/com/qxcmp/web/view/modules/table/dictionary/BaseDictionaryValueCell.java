package com.qxcmp.web.view.modules.table.dictionary;

import com.qxcmp.web.view.modules.table.AbstractTableCell;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义字典表格单元格基类
 * <p>
 * 用于将对象解析为其他格式的单元格
 * <p>
 * 所有子类可以将对象转换为自定义视图组件
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class BaseDictionaryValueCell<T> {

    /**
     * 单元格绑定的对象
     */
    protected T object;

    public BaseDictionaryValueCell(T object) {
        this.object = object;
    }

    /**
     * 将单元格对象渲染为视图组件
     *
     * @return 表格单元格视图组件
     */
    public abstract AbstractTableCell render();

}
