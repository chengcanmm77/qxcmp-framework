package com.qxcmp.account.username;

import com.qxcmp.core.validation.Username;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.ImageCaptchaField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 密保问题密码找回平台统一表单
 * <p>
 * 用户在使用密保找回密码的时候需要填写的信息
 * <p>
 * 找回流程如下： <ol> <li>用户需要提前设置好密保问题</li> <li>用户输入要找回的用户名</li> <li>跳转到密保问题表单</li> <li>用户完成密保问题</li>
 * <li>如果用户回答对两个以上的问题则生成密码重置链接并跳转到账户重置表单</li> </ol>
 *
 * @author aaric
 * @see AccountUsernameResetQuestionForm
 */
@Form(submitText = "回答密保问题")
@Data
public class AccountUsernameResetForm {

    /**
     * 用户名
     * <p>
     * 要找回密码的用户名
     */
    @Size(min = 6, max = 20, message = "{Size.username}")
    @Username
    @TextInputField(value = "用户名", placeholder = "请输入要找回密码的账户用户名", autoFocus = true)
    private String username;

    /**
     * 会话验证码
     * <p>
     * 用户输入的会话验证码，需要和验证码图片中的信息一致
     */
    @ImageCaptchaField(value = "验证码", placeholder = "请输入图片中的文字信息")
    private String captcha;
}
