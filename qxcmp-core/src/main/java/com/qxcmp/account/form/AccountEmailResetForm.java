package com.qxcmp.account.form;

import com.qxcmp.account.AccountCode;
import com.qxcmp.web.view.annotation.form.EmailField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.ImageCaptchaField;
import com.qxcmp.web.view.modules.form.FormMethod;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

/**
 * 邮箱账户密码找回平台统一表单
 * <p>
 * 用户在使用邮箱找回密码的时候需要填写的信息
 * <p>
 * 找回流程如下： <ol> <li>填写用户邮箱</li> <li>检查邮箱格式以及是否存在</li> <li>生成重置密码链接{@link AccountCode}，并发送到该邮箱</li>
 * <li>用户点击该重置密码链接，生成账户密码重置表单{@link AccountResetForm}</li> <li>提交密码重置表单以修改账户密码</li> </ol>
 *
 * @author aaric
 */
@Form(submitText = "发送重置链接", method = FormMethod.POST)
@Data
public class AccountEmailResetForm {

    /**
     * 用户账户所绑定的邮箱
     * <p>
     * 需要改邮箱已经存在，并且符合邮箱格式
     */
    @Email
    @EmailField(value = "邮箱", placeholder = "输入您账户所绑定的邮箱", autoFocus = true)
    private String email;

    @ImageCaptchaField(value = "验证码", placeholder = "请输入图片中的文字信息")
    private String captcha;
}
