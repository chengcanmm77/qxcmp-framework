package com.qxcmp.web.view.modules.dropdown;

import lombok.Getter;
import lombok.Setter;

/**
 * 选择下拉框
 * <p>
 * 用于在表单中使用
 *
 * @author Aaric
 */
@Getter
@Setter
public class Selection extends AbstractDropdown {

    /**
     * 下拉选择框 name - 用于表单提交
     */
    private String name;

    /**
     * 是否开启搜索
     */
    private boolean search;

    /**
     * 是否支持多选，可以与搜索结合使用
     */
    private boolean multiple;

    /**
     * 选择框选项菜单
     */
    private SelectionMenu menu;

    public Selection() {
    }

    public Selection(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "selection";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassContent());

        if (search) {
            stringBuilder.append(" search");
        }

        if (multiple) {
            stringBuilder.append(" multiple");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "selection dropdown";
    }

    public Selection setSearch() {
        setSearch(true);
        return this;
    }

    public Selection setMultiple() {
        setMultiple(true);
        return this;
    }

    public Selection setMenu(SelectionMenu menu) {
        this.menu = menu;
        return this;
    }
}
