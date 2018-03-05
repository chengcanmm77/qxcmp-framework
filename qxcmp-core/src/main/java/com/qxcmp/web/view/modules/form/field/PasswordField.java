package com.qxcmp.web.view.modules.form.field;

/**
 * 密码输入框
 *
 * @author Aaric
 */
public class PasswordField extends TextInputField {

    @Override
    public String getFragmentName() {
        return "field-password";
    }

    @Override
    public String getClassSuffix() {
        return "password " + super.getClassSuffix();
    }
}
