package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.NumberField;
import com.qxcmp.web.view.annotation.form.TextInputField;
import lombok.Data;

/**
 * @author Aaric
 */
@Form("修改用户钱包")
@Data
public class AdminFinanceWalletForm {

    @TextInputField(value = "用户ID", required = true, autoFocus = true)
    private String userId;

    @NumberField(value = "修改金额", tooltip = "正数为增加，负数为减少，单位为分")
    private int amount;

    @TextInputField(value = "备注信息", placeholder = "备注信息将显示给用户钱包修改原因")
    private String comments;
}
