package com.qxcmp.account.form;

import com.qxcmp.core.validation.Username;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.ImageCaptchaField;
import com.qxcmp.web.view.annotation.form.PasswordField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 用户名账户注册平台统一表单
 * <p>
 * 用户在使用用户名账户注册的时候需要填写的信息
 *
 * @author aaric
 */
@Form(submitText = "立即注册")
@Data
public class AccountUsernameLogonForm {

    /**
     * 用户名
     * <p>
     * 全局唯一，注册的时候需要检查是否已经存在
     */
    @Size(min = 6, max = 20, message = "{Size.username}")
    @Username
    @TextInputField(value = "用户名", tooltip = "用户名只能由字母、数字、下划线组成，且长度在6到20个字符之间", maxLength = 20, placeholder = "用户名只能由字母、数字、下划线组成", autoFocus = true)
    private String username;

    /**
     * 登录密码
     * <p>
     * 用户登录时候需要输入的密码
     */
    @Size(min = 6, max = 20, message = "{Size.password}")
    @PasswordField(value = "设置密码", placeholder = "请使用至少两种以上的字符组合", maxLength = 20)
    private String password;

    /**
     * 确认密码
     * <p>
     * 保证用户两次输入的密码一致
     */
    @PasswordField(value = "确认密码", placeholder = "请再次输入您的登录密码", maxLength = 20)
    private String passwordConfirm;

    /**
     * 会话验证码
     * <p>
     * 用户输入的会话验证码，需要和验证码图片中的信息一致
     */
    @ImageCaptchaField(value = "验证码", placeholder = "请输入图片中的文字信息")
    private String captcha;
}
