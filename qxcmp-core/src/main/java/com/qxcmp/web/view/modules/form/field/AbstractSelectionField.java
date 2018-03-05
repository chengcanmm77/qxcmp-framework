package com.qxcmp.web.view.modules.form.field;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.modules.dropdown.DropdownConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * 下拉框选项
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractSelectionField extends AbstractFormField {

    /**
     * 选项在 ModelAndView 中的索引
     */
    private String itemIndex;

    /**
     * 选项值索引
     * <p>
     * 用于生成选项值
     */
    private String itemValueIndex;

    /**
     * 选项文本索引
     * <p>
     * 用于生成选项文本
     */
    private String itemTextIndex;

    /**
     * JS 配置
     */
    private DropdownConfig config;

    /**
     * 是否开启选项搜索
     */
    private boolean search;

    /**
     * 是否为多选
     */
    private boolean multiple;

    /**
     * 提示文本
     */
    private String placeholder;

    /**
     * 下拉框选项
     */
    private List<Object> items = Lists.newArrayList();

    public AbstractSelectionField addItem(Object object) {
        items.add(object);
        return this;
    }

    public AbstractSelectionField addItems(Collection<?> objects) {
        items.addAll(objects);
        return this;
    }

    public String getDropdownClass() {
        final StringBuilder stringBuilder = new StringBuilder("ui component selection dropdown");

        if (search) {
            stringBuilder.append(" search");
        }

        if (multiple) {
            stringBuilder.append(" multiple");
        }

        return stringBuilder.toString();
    }
}
