package com.qxcmp.account;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.ImageCaptchaField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

/**
 * 账户激活表单
 *
 * @author aaric
 */
@Form(submitText = "发送激活邮件")
@Data
public class AccountActivateForm {

    @TextInputField(value = "用户名", maxLength = 20, placeholder = "你要激活账户的用户名")
    private String username;

    @ImageCaptchaField("验证码")
    private String captcha;
}
