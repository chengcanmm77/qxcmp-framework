package com.qxcmp.admin.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form("短信服务配置")
@Data
public class AdminSmsSettingsForm {

    @TextInputField(value = "AccessKey")
    private String messageSmsAccessKey;

    @TextInputField(value = "AccessSecret")
    private String messageSmsAccessSecret;

    @TextInputField("默认短信签名")
    private String messageSmsSignName;
}
