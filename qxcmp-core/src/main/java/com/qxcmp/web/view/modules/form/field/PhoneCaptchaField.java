package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneCaptchaField extends AbstractCaptchaField {

    /**
     * 指定获取电话号码的字段
     * <p>
     * 如果未设定该值，将自动生成手机号输入框
     */
    private String phoneField;

    /**
     * 重新发送短信的时间间隔(秒)
     */
    private int interval = 120;

    @Override
    public String getFragmentName() {
        return "field-captcha-phone";
    }

    @Override
    public String getClassSuffix() {
        return "phone " + super.getClassSuffix();
    }
}
