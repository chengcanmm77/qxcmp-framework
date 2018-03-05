package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 数字输入框
 *
 * @author Aaric
 */
@Getter
@Setter
public class NumberField extends BaseHTMLField {

    /**
     * 最小值
     */
    private float min;

    /**
     * 最大值
     */
    private float max;

    /**
     * 步长
     * <p>
     * 每次点击增加或减少按钮的时候改变的值
     */
    private float step;

    @Override
    public String getFragmentName() {
        return "field-number";
    }

    @Override
    public String getClassSuffix() {
        return "number " + super.getClassSuffix();
    }
}
