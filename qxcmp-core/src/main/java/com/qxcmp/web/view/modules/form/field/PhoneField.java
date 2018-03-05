package com.qxcmp.web.view.modules.form.field;

/**
 * 手机号输入框
 *
 * @author Aaric
 */
public class PhoneField extends TextInputField {

    @Override
    public String getFragmentName() {
        return "field-phone";
    }

    @Override
    public String getClassSuffix() {
        return "phone " + super.getClassSuffix();
    }
}
