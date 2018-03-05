package com.qxcmp.web.view.modules.dropdown.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractTextDropdownItem extends AbstractDropdownItem {

    /**
     * 选项文本
     */
    private String text;

    /**
     * 选项值
     * <p>
     * 当选项支持选择框选项时需要使用该值
     */
    private String value;

    /**
     * 选项描述，靠右侧显示
     * <p>
     * 当使用该属性且下拉框为按钮形式的时候，需要保证下拉框的宽度足够
     */
    private String description;

    /**
     * 是否为禁用状态
     */
    private boolean disabled;

    public AbstractTextDropdownItem(String text) {
        this.text = text;
        this.value = text;
    }

    public AbstractTextDropdownItem(String text, String value) {
        this.text = text;
        this.value = value;
    }

    @Override
    public String getClassContent() {
        return disabled ? " disabled" : "";
    }

    public AbstractTextDropdownItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public AbstractTextDropdownItem setDisabled() {
        this.disabled = true;
        return this;
    }
}
