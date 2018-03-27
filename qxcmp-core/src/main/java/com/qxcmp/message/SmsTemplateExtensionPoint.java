package com.qxcmp.message;

import com.qxcmp.core.extension.AbstractExtensionPoint;
import org.springframework.stereotype.Component;

/**
 * @author Aaric
 */
@Component
public class SmsTemplateExtensionPoint extends AbstractExtensionPoint<SmsTemplateExtension> {

    /**
     * 获取验证码短信模板
     *
     * @return 验证码短信模板
     */
    public SmsTemplateExtension getCaptchaTemplate() {
        return getExtensions().stream().filter(SmsTemplateExtension::isCaptcha).findAny().orElseThrow(() -> new RuntimeException("No captcha template found."));
    }
}
