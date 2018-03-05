package com.qxcmp.web.view.elements.menu.item;

import lombok.Getter;
import lombok.Setter;

/**
 * 搜索输入框
 *
 * @author Aaric
 */
@Getter
@Setter
public class SearchInputItem extends AbstractMenuItem {

    /**
     * 搜索提交的Url
     * <p>
     * 目前有些 issue 尚未解决  只能使用该url搜索
     */
    private String url = "/search";

    /**
     * 搜索提交的查询参数
     */
    private String name = "content";

    /**
     * 提示文本
     */
    private String placeholder = "搜索...";

    public SearchInputItem() {
    }

    @Override
    public String getFragmentName() {
        return "item-search-input";
    }

    @Override
    public String getClassSuffix() {
        return "ui search " + super.getClassSuffix();
    }

    public SearchInputItem setUrl(String url) {
        this.url = url;
        return this;
    }

    public SearchInputItem setName(String name) {
        this.name = name;
        return this;
    }

    public SearchInputItem setPlaceHolder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }
}
