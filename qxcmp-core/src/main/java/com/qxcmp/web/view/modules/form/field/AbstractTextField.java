package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 文本输入类字段抽象类
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractTextField extends BaseHTMLField {

    /**
     * 最大文本长度
     */
    private int maxLength = 80;

    public AbstractTextField setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }
}
