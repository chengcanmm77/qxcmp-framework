package com.qxcmp.admin.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form(value = "邮箱绑定", submitText = "确认绑定")
@Data
public class AdminProfileSecurityEmailBindForm {

    @TextInputField(value = "验证码", autoFocus = true, placeholder = "请输入你收到的邮箱绑定验证码", required = true)
    private String captcha;
}
