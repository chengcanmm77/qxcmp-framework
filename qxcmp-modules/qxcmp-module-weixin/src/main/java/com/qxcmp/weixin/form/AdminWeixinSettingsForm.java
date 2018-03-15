package com.qxcmp.weixin.form;

import com.qxcmp.web.view.annotation.form.BooleanField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextAreaField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

@Form("公众号配置")
@Data
public class AdminWeixinSettingsForm {

    @BooleanField(value = "调试模式", section = "基本参数")
    private boolean debug;

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
    private String subscribeMessage;
}
