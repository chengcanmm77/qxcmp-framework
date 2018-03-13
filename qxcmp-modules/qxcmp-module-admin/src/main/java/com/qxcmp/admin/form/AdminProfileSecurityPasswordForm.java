package com.qxcmp.admin.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.PasswordField;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author Aaric
 */
@Form(value = "修改登录密码", submitText = "确认修改")
@Data
public class AdminProfileSecurityPasswordForm {

    @Size(min = 6, max = 20, message = "密码长度应该在6到20之间")
    @PasswordField(value = "原始密码", maxLength = 20, required = true, autoFocus = true)
    private String oldPassword;

    @Size(min = 6, max = 20, message = "密码长度应该在6到20之间")
    @PasswordField(value = "新密码", maxLength = 20, required = true)
    private String newPassword;

    @Size(min = 6, max = 20, message = "密码长度应该在6到20之间")
    @PasswordField(value = "确认密码", maxLength = 20, required = true)
    private String passwordConfirm;
}
