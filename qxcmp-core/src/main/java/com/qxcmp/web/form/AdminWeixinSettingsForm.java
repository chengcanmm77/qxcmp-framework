package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.BooleanField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextAreaField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

@Form("公众号配置")
@Data
public class AdminWeixinSettingsForm {

    @BooleanField("调试模式")
    private boolean debug;

    @TextInputField(value = "App ID", required = true, autoFocus = true)
    private String appId;

    @TextInputField(value = "App secret", required = true)
    private String secret;

    @TextInputField(value = "Token", required = true)
    private String token;

    @TextInputField("AES Key")
    private String aesKey;

    @TextInputField("授权回调链接")
    private String oauth2Url;

    @TextAreaField(value = "关注欢迎语", maxLength = 300, rows = 10)
    private String subscribeMessage;
}
