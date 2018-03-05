package com.qxcmp.web.form;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.annotation.form.BooleanField;
import com.qxcmp.web.view.annotation.form.DynamicField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.NumberField;
import lombok.Data;

import java.util.List;

@Form("兑换码配置")
@Data
public class AdminRedeemSettingsForm {

    @BooleanField(value = "是否开启", tooltip = "关闭以后所有兑换码的使用将无效")
    private boolean enable;

    @NumberField(value = "兑换码有效时间", tooltip = "单位：秒")
    private Integer expireDuration;

    @DynamicField(value = "兑换码业务", itemHeaders = "业务名称")
    private List<String> type = Lists.newArrayList();
}
