package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 基本 HTML 字段抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class BaseHTMLField extends AbstractFormField {

    /**
     * 是否禁用浏览器自动完成
     */
    private boolean disableAutoComplete;

    /**
     * 是否在页面加载完成时自动获得焦点
     */
    private boolean autoFocus;

    /**
     * 是否为只读
     */
    private boolean readOnly;

    /**
     * 提示文本
     */
    private String placeholder;

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder(super.getClassContent());

        if (isReadOnly()) {
            stringBuilder.append(" readonly");
        }

        return stringBuilder.toString();
    }

    public BaseHTMLField setDisableAutoComplete() {
        setDisableAutoComplete(true);
        return this;
    }

    public BaseHTMLField setAutoFocus() {
        setAutoFocus(true);
        return this;
    }

    public BaseHTMLField setReadOnly() {
        setReadOnly(true);
        return this;
    }

    public BaseHTMLField setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }
}
