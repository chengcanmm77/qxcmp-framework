package com.qxcmp.admin.form;

import com.qxcmp.core.validation.Phone;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.PhoneCaptchaField;
import com.qxcmp.web.view.annotation.form.PhoneField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form(value = "手机绑定", submitText = "确认绑定")
@Data
public class AdminProfileSecurityPhoneForm {

    @Phone
    @PhoneField(value = "手机号码", autoFocus = true)
    private String phone;

    @PhoneCaptchaField(value = "短信验证码", phoneField = "phone")
    private String captcha;
}
