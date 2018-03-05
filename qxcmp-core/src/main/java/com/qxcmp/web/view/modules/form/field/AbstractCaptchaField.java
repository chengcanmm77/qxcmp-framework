package com.qxcmp.web.view.modules.form.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 验证码字段抽象类
 * <p>
 * 验证码字段一般只出现一次，且推荐使用 captcha 作为字段名称
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractCaptchaField extends BaseHTMLField {

    /**
     * 获取验证码的地址
     */
    private String captchaUrl;

    @Override
    public String getClassSuffix() {
        return "captcha " + super.getClassSuffix();
    }
}
