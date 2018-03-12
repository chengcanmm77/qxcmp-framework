package com.qxcmp.redeem.form;

import com.qxcmp.web.view.annotation.form.BooleanField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.NumberField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form("兑换码配置")
@Data
public class AdminRedeemSettingsForm {

    @BooleanField(value = "是否开启", tooltip = "关闭以后所有兑换码的使用将无效")
    private boolean enable;

    @NumberField(value = "有效时间", tooltip = "生成的兑换码默认有效时间单位：秒")
    private Integer defaultExpireDuration;
}
