package com.qxcmp.web.form;

import com.qxcmp.web.view.annotation.form.BooleanField;
import com.qxcmp.web.view.annotation.form.Form;
import com.qxcmp.web.view.annotation.form.NumberField;
import com.qxcmp.web.view.modules.form.field.BooleanFieldStyle;
import lombok.Data;

import javax.validation.constraints.Max;

/**
 * 认证配置表单
 *
 * @author aaric
 */
@Form("认证配置")
@Data
public class AdminSecurityAuthenticationForm {

    @Max(10)
    @NumberField(value = "验证码阈值", section = "验证码配置", min = 1, max = 10, tooltip = "该值决定了认证失败多少次以后要求用户输入验证码以继续认证")
    private int captchaThreshold;

    @Max(10)
    @NumberField(value = "验证码长度", section = "验证码配置", min = 4, max = 10, tooltip = "图形验证码和短信验证码的长度")
    private int captchaLength;

    @BooleanField(value = "账户锁定", section = "账户锁定配置", tooltip = "用户登录失败一定次数的时候是否锁定用户账户", style = BooleanFieldStyle.TOGGLE)
    private boolean lockAccount;

    @Max(10)
    @NumberField(value = "锁定阈值", section = "账户锁定配置", min = 1, max = 10, tooltip = "登录失败多少次以后锁定账户")
    private int lockThreshold;

    @NumberField(value = "锁定时长", section = "账户锁定配置", min = 1, tooltip = "锁定时长为分钟，超过锁定时长以后将解锁用户")
    private int unlockDuration;

    @BooleanField(value = "账户过期", section = "账户过期配置", tooltip = "如果用户长时间不登录，是否使账户过期", style = BooleanFieldStyle.TOGGLE)
    private boolean expireAccount;

    @NumberField(value = "过期时间", section = "账户过期配置", min = 1, tooltip = "时间为天，超过时间不登录的用户将会使账户过期")
    private int expireAccountDuration;

    @BooleanField(value = "密码过期", section = "账户密码配置", tooltip = "如果密码长时间不修改，是否使密码过期", style = BooleanFieldStyle.TOGGLE)
    private boolean expireCredential;

    @NumberField(value = "过期时间", section = "账户密码配置", min = 1, tooltip = "时间为天，多少天以后不修改密码会使密码过期")
    private int expireCredentialDuration;

    @BooleanField(value = "唯一密码", section = "账户密码配置", tooltip = "开启以后修改的密码不能和以前重复", style = BooleanFieldStyle.TOGGLE)
    private boolean uniqueCredential;
}
