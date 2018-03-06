package com.qxcmp.account.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.HiddenField;
import com.qxcmp.web.view.annotation.form.PasswordField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form(submitIcon = "sign in")
@Data
public class LoginForm {

    @HiddenField
    private String callback;

    @TextInputField(value = "用户名", tooltip = "用户名/邮箱/手机", maxLength = 30)
    private String username;

    @PasswordField("登录密码")
    private String password;
}
