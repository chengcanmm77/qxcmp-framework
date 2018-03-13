package com.qxcmp.admin.form;

import com.qxcmp.web.view.annotation.form.*;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @author Aaric
 */
@Form("网站配置")
@Data
public class AdminSettingsSiteForm {

    @AvatarField(value = "网站LOGO", section = "网站配置")
    private String siteLogo;

    @AvatarField(value = "网站图标", section = "网站配置")
    private String siteFavicon;

    @TextInputField(value = "网站域名", autoFocus = true, section = "网站配置")
    private String siteDomain;

    @TextInputField(value = "网站标题", section = "SEO配置")
    private String siteTitle;

    @TextInputField(value = "网站关键字", section = "SEO配置")
    private String siteKeywords;

    @TextInputField(value = "网站描述信息", section = "SEO配置")
    private String siteDescription;

    @BooleanField(value = "开启水印", section = "水印设置")
    private Boolean imageWatermarkEnable;

    @Size(max = 20)
    @TextInputField(value = "水印名称", maxLength = 20, section = "水印设置")
    private String imageWatermarkName;

    @IgnoredSystemConfig
    @TextSelectionField(value = "水印位置", section = "水印设置")
    private String imageWatermarkPosition;

    @Min(1)
    @Max(36)
    @NumberField(value = "字体大小", min = 1, max = 36, section = "水印设置")
    private Integer imageWatermarkFontSize;

    @BooleanField(value = "用户名注册", section = "账户注册配置")
    private Boolean accountEnableUsername;

    @BooleanField(value = "邮箱注册", section = "账户注册配置")
    private Boolean accountEnableEmail;

    @BooleanField(value = "手机号注册", section = "账户注册配置")
    private Boolean accountEnablePhone;

    @BooleanField(value = "邀请码注册", section = "账户注册配置")
    private Boolean accountEnableInvite;

    @NumberField(value = "线程池大小", section = "任务调度配置", tooltip = "修改该配置需要重新服务")
    private Integer taskExecutorCorePoolSize;

    @NumberField(value = "最大线程池", section = "任务调度配置", tooltip = "修改该配置需要重新服务")
    private Integer taskExecutorMaxPoolSize;

    @NumberField(value = "队列大小", section = "任务调度配置", tooltip = "修改该配置需要重新服务")
    private Integer taskExecutorQueueCapacity;

    @NumberField(value = "超时时间", section = "系统会话配置", tooltip = "用户多少时间不操作会使会话过期，单位为秒")
    private Integer sessionTimeout;

    @NumberField(value = "最大会话数", section = "系统会话配置", tooltip = "可以同时允许一个用户登录在几个不同的会话")
    private Integer sessionMaxActiveCount;

    @BooleanField(value = "阻止登录", section = "系统会话配置", tooltip = "达到最大会话数是否阻止继续登录")
    private Boolean sessionPreventLogin;
}
