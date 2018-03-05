package com.qxcmp.account.phone;

import com.qxcmp.account.AccountCode;
import com.qxcmp.core.validation.Phone;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.PhoneCaptchaField;
import com.qxcmp.web.view.annotation.form.PhoneField;
import lombok.Data;

/**
 * 手机账户密码找回平台统一表单
 * <p>
 * 用户在使用手机号找回密码的时候需要填写的信息
 * <p>
 * 找回流程如下： <ol> <li>用户填写手机号，并点击发送短信验证码按钮</li> <li>验证手机号是否已经注册</li> <li>发送短信验证码到指定手机号</li> <li>用户输入短信验证码</li>
 * <li>如果验证码正确则生成一个密码重置链接并跳转到该链接{@link AccountCode}</li> </ol>
 *
 * @author aaric
 */
@Form(submitText = "立即重置")
@Data
public class AccountPhoneResetForm {

    @Phone
    @PhoneField(value = "手机号码", placeholder = "请输入您账户绑定的手机号码")
    private String phone;

    @PhoneCaptchaField(value = "短信验证码", placeholder = "请输入你收到的短信验证码", phoneField = "phone")
    private String captcha;
}
