package com.qxcmp.weixin.controller;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.google.gson.GsonBuilder;
import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.audit.ActionException;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.weixin.WeixinModuleSystemConfig;
import com.qxcmp.weixin.WeixinService;
import com.qxcmp.weixin.event.AdminWeixinSettingsEvent;
import com.qxcmp.weixin.form.AdminWeixinMenuForm;
import com.qxcmp.weixin.form.AdminWeixinSettingsForm;
import com.qxcmp.weixin.page.AdminWeixinMenuPage;
import com.qxcmp.weixin.page.AdminWeixinOverviewPage;
import com.qxcmp.weixin.page.AdminWeixinSettingsPage;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;

import static com.qxcmp.weixin.WeixinModule.ADMIN_WEIXIN_URL;
import static me.chanjar.weixin.common.api.WxConsts.OAuth2Scope.SNSAPI_USERINFO;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_WEIXIN_URL)
@RequiredArgsConstructor
public class AdminWeixinPageController extends QxcmpAdminController {

    private final WxPayConfig wxPayConfig;
    private final WxMpService wxMpService;
    private final WxMpConfigStorage wxMpConfigStorage;
    private final WeixinService weixinService;

    @GetMapping("")
    public ModelAndView weixinPage() {
        return page(AdminWeixinOverviewPage.class, weixinService.getSyncService());
    }

    @GetMapping("/sync")
    public ModelAndView weixinUserSync() {
        return execute("同步微信用户", context -> {
            try {
                weixinService.getSyncService().syncUsers(currentUser().orElseThrow(RuntimeException::new));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ADMIN_WEIXIN_URL));
    }

    @GetMapping("/settings")
    public ModelAndView settingsGet(final AdminWeixinSettingsForm form, BindingResult bindingResult) {
        return systemConfigPage(AdminWeixinSettingsPage.class, form, bindingResult, WeixinModuleSystemConfig.class);
    }

    @PostMapping("/settings")
    public ModelAndView settingsPost(@Valid final AdminWeixinSettingsForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return settingsGet(form, bindingResult);
        }
        ModelAndView modelAndView = updateSystemConfig(WeixinModuleSystemConfig.class, form);
        WxMpInMemoryConfigStorage configStorage = (WxMpInMemoryConfigStorage) wxMpConfigStorage;
        configStorage.setAppId(form.getAppId());
        configStorage.setSecret(form.getAppSecret());
        configStorage.setToken(form.getToken());
        configStorage.setAesKey(form.getAesKey());
        wxPayConfig.setAppId(form.getAppId());
        wxPayConfig.setMchId(form.getPaymentMchId());
        wxPayConfig.setMchKey(form.getPaymentMchKey());
        wxPayConfig.setSubAppId(form.getPaymentSubAppId());
        wxPayConfig.setSubMchId(form.getPaymentSubMchId());
        wxPayConfig.setNotifyUrl(form.getPaymentNotifyUrl());
        wxPayConfig.setKeyPath(form.getPaymentKeyPath());

        if (StringUtils.isNotBlank(form.getOauth2CallbackUrl())) {
            try {
                String oauth2Url = wxMpService.oauth2buildAuthorizationUrl(form.getOauth2CallbackUrl(), SNSAPI_USERINFO, null);
                systemConfigService.update(WeixinModuleSystemConfig.AUTHORIZATION_URL, oauth2Url);
            } catch (Exception ignored) {

            }
        }

        return modelAndView;
    }

    @GetMapping("/menu")
    public ModelAndView menuGet(final AdminWeixinMenuForm form) {
        try {
            WxMpMenu wxMpMenu = wxMpService.getMenuService().menuGet();
            if (Objects.nonNull(wxMpMenu)) {
                form.setContent(new GsonBuilder().setPrettyPrinting().create().toJson(wxMpService.getMenuService().menuGet().getMenu()));
            } else {
                form.setContent("当前公众号还未设置自定义菜单");
            }
            return page(AdminWeixinMenuPage.class, form, null);
        } catch (Exception e) {
            return overviewPage(viewHelper.nextWarningOverview("无法获取公众号菜单").addComponent(new P(e.getMessage())));
        }
    }

    @PostMapping("/menu")
    public ModelAndView menuPost(@Valid final AdminWeixinMenuForm form) {
        return execute("修改公众号菜单", context -> {
            try {
                wxMpService.getMenuService().menuCreate(form.getContent());
                applicationContext.publishEvent(new AdminWeixinSettingsEvent(currentUser().orElseThrow(RuntimeException::new)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ""));
    }
}
