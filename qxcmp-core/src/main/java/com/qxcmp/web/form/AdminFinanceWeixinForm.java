package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.BooleanField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.TextInputField;
import com.qxcmp.web.view.annotation.form.TextSelectionField;
import lombok.Data;

@Form("微信支付配置")
@Data
public class AdminFinanceWeixinForm {

    @BooleanField("开启支付功能")
    private boolean enable;

    @TextSelectionField("支付方式")
    private String tradeType;

    @TextInputField(value = "App ID", required = true, autoFocus = true)
    private String appId;

    @TextInputField(value = "商户号", required = true)
    private String mchId;

    @TextInputField(value = "商户密钥", required = true)
    private String mchKey;

    @TextInputField(value = "支付结果通知链接", required = true)
    private String notifyUrl;

    @TextInputField("子商户公众号ID")
    private String subAppId;

    @TextInputField("子商户号")
    private String subMchId;

    @TextInputField("证书路径")
    private String keyPath;
}
