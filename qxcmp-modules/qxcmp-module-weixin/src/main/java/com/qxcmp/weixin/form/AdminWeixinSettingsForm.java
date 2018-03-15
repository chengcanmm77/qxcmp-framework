package com.qxcmp.weixin.form;

import com.qxcmp.web.view.annotation.form.BooleanField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextAreaField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form("微信平台参数")
@Data
public class AdminWeixinSettingsForm {

    @BooleanField(value = "调试模式", section = "基本参数")
    private Boolean debugMode;

    @TextInputField(value = "App ID", required = true, autoFocus = true, section = "基本参数")
    private String appId;

    @TextInputField(value = "App secret", required = true, section = "基本参数")
    private String appSecret;

    @TextInputField(value = "Token", required = true, section = "基本参数")
    private String token;

    @TextInputField(value = "AES Key", section = "基本参数")
    private String aesKey;

    @TextInputField(value = "授权回调链接", section = "基本参数")
    private String oauth2CallbackUrl;

    @TextAreaField(value = "关注欢迎语", maxLength = 300, rows = 10, section = "基本参数")
    private String subscribeWelcomeMessage;

    @BooleanField(value = "开启微信支付", section = "微信支付")
    private Boolean paymentEnable;

    @TextInputField(value = "商户号", section = "微信支付", required = true)
    private String paymentMchId;

    @TextInputField(value = "商户密钥", section = "微信支付", required = true)
    private String paymentMchKey;

    @TextInputField(value = "支付结果通知链接", section = "微信支付", required = true)
    private String paymentNotifyUrl;

    @TextInputField(value = "子商户公众号ID", section = "微信支付")
    private String paymentSubAppId;

    @TextInputField(value = "子商户号", section = "微信支付")
    private String paymentSubMchId;

    @TextInputField(value = "证书路径", section = "微信支付")
    private String paymentKeyPath;

}
