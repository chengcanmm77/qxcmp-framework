package com.qxcmp.message;

import org.springframework.stereotype.Component;

@Component
public class TestSmsTemplate implements SmsTemplateExtension {

    @Override
    public String getName() {
        return "验证码短信";
    }

    @Override
    public String getSignName() {
        return "挚友旅游";
    }

    @Override
    public String getTemplateCode() {
        return "SMS_78730104";
    }

    @Override
    public String getContent() {
        return "尊敬的用户：您的验证码是：${captcha}，请在5分钟之内输入。工作人员不会索取，请勿泄露。";
    }

    @Override
    public boolean isCaptcha() {
        return true;
    }
}
