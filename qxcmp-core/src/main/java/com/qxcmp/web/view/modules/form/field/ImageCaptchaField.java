package com.qxcmp.web.view.modules.form.field;

/**
 * 图片验证码输入框
 *
 * @author Aaric
 */
public class ImageCaptchaField extends AbstractCaptchaField {

    @Override
    public String getFragmentName() {
        return "field-captcha-image";
    }

    @Override
    public String getClassSuffix() {
        return "image " + super.getClassSuffix();
    }
}
