package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.PasswordField;
import lombok.Data;

import javax.validation.constraints.Size;

@Form(value = "设置支付密码", submitText = "确认修改")
@Data
public class AdminProfileSecurityPayPasswordForm {

    @Size(min = 6, max = 20, message = "密码长度应该在6到20之间")
    @PasswordField(value = "支付密码", maxLength = 20, required = true)
    private String password;

    @Size(min = 6, max = 20, message = "密码长度应该在6到20之间")
    @PasswordField(value = "确认密码", maxLength = 20, required = true)
    private String passwordConfirm;
}
