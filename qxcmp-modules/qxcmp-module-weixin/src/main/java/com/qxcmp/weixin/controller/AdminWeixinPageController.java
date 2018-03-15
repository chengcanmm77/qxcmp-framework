package com.qxcmp.weixin.controller;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.google.common.collect.ImmutableList;
import com.google.gson.GsonBuilder;
import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.audit.ActionException;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.elements.button.Button;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.html.HtmlText;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.message.InfoMessage;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.support.Wide;
import com.qxcmp.web.view.views.Overview;
import com.qxcmp.weixin.WeixinModuleSystemConfig;
import com.qxcmp.weixin.WeixinMpMaterialService;
import com.qxcmp.weixin.WeixinMpMaterialType;
import com.qxcmp.weixin.WeixinService;
import com.qxcmp.weixin.event.AdminWeixinSettingsEvent;
import com.qxcmp.weixin.form.AdminFinanceWeixinForm;
import com.qxcmp.weixin.form.AdminWeixinMenuForm;
import com.qxcmp.weixin.form.AdminWeixinSettingsForm;
import com.qxcmp.weixin.page.AdminWeixinOverviewPage;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.weixin.WeixinModule.ADMIN_WEIXIN_URL;
import static me.chanjar.weixin.common.api.WxConsts.OAuth2Scope.SNSAPI_USERINFO;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_WEIXIN_URL)
@RequiredArgsConstructor
public class AdminWeixinPageController extends QxcmpAdminController {

    private static final List<String> SUPPORT_WEIXIN_PAYMENT = ImmutableList.of("NATIVE", "JSAPI");

    private final WxPayConfig wxPayConfig;
    private final WxMpService wxMpService;
    private final WxMpConfigStorage wxMpConfigStorage;
    private final WeixinMpMaterialService weixinMpMaterialService;
    private final WeixinService weixinService;

    @GetMapping("")
    public ModelAndView weixinPage() {
        return page(AdminWeixinOverviewPage.class);
    }

    @PostMapping("/sync")
    public ResponseEntity<RestfulResponse> weixinUserSync() {
        weixinService.getSyncService().syncUsers(currentUser().orElseThrow(RuntimeException::new));
        return ResponseEntity.ok(RestfulResponse.builder().status(HttpStatus.OK.value()).build());
    }

    @GetMapping("/material")
    public ModelAndView materialPage(Pageable pageable) {

        Grid grid = new Grid();
        Col col = new Col(Wide.SIXTEEN);

        if (weixinService.getSyncService().isWeixinMaterialSync()) {
            col.addComponent(new InfoMessage(String.format("微信素材正在同步中... 当前进度为 %d/%d，请稍后刷新查看", weixinService.getSyncService().getCurrentMaterialSync(), weixinService.getSyncService().getTotalMaterialSync())).setCloseable());
        }

        col.addComponent(convertToTable(new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("type"), new Sort.Order(Sort.Direction.DESC, "updateTime"))), weixinMpMaterialService));

        return page().addComponent(grid.addItem(new Row().addCol(col)))
                .setBreadcrumb("控制台", "", "微信公众平台", "weixin", "素材管理")
                .build();
    }

    @PostMapping("/material/sync")
    public ResponseEntity<RestfulResponse> userWeixinSyncPage() {
        weixinService.getSyncService().syncMaterials(currentUser().orElseThrow(RuntimeException::new));
        return ResponseEntity.ok(RestfulResponse.builder().status(HttpStatus.OK.value()).build());
    }

    @GetMapping("/material/{id}/preview")
    public ModelAndView materialPreviewPage(@PathVariable String id) {
        return weixinMpMaterialService.findOne(id)
                .filter(weixinMpMaterial -> weixinMpMaterial.getType().equals(WeixinMpMaterialType.NEWS))
                .map(weixinMpMaterial -> page()
                        .addComponent(new Segment().addComponent(new Button("转换为文章", String.format(QXCMP_ADMIN_URL + "/weixin/material/%s/convert", id)).setPrimary())
                                .addComponent(new Overview(weixinMpMaterial.getTitle(), weixinMpMaterial.getAuthor()).addComponent(new HtmlText(weixinMpMaterial.getContent()))))
                        .setBreadcrumb("控制台", "", "微信公众平台", "weixin", "素材管理", "weixin/material", "图文查看"))
                .orElse(page(viewHelper.nextWarningOverview("素材不存在或者不为图文素材", "目前仅支持图文素材的查看"))).build();
    }

    @GetMapping("/settings")
    public ModelAndView weixinMpPage(final AdminWeixinSettingsForm form) {

        form.setDebug(systemConfigService.getBoolean(WeixinModuleSystemConfig.DEBUG_MODE).orElse(false));
        form.setAppId(systemConfigService.getString(WeixinModuleSystemConfig.APP_ID).orElse(""));
        form.setToken(systemConfigService.getString(WeixinModuleSystemConfig.TOKEN).orElse(""));
        form.setAesKey(systemConfigService.getString(WeixinModuleSystemConfig.AES_KEY).orElse(""));
        form.setSubscribeMessage(systemConfigService.getString(WeixinModuleSystemConfig.SUBSCRIBE_WELCOME_MESSAGE).orElse(""));

        return page()
                .addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "微信公众平台", "weixin", "公众号配置")
                .build();
    }

    @PostMapping("/settings")
    public ModelAndView weixinMpPage(@Valid final AdminWeixinSettingsForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page()
                    .addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "微信公众平台", "weixin", "公众号配置")
                    .build();
        }

        return submitForm(form, context -> {
            try {
                systemConfigService.update(WeixinModuleSystemConfig.DEBUG_MODE, String.valueOf(form.isDebug()));
                systemConfigService.update(WeixinModuleSystemConfig.APP_ID, form.getAppId());
                systemConfigService.update(WeixinModuleSystemConfig.APP_SECRET, form.getAppSecret());
                systemConfigService.update(WeixinModuleSystemConfig.TOKEN, form.getToken());
                systemConfigService.update(WeixinModuleSystemConfig.AES_KEY, form.getAesKey());
                systemConfigService.update(WeixinModuleSystemConfig.OAUTH2_CALLBACK_URL, form.getOauth2CallbackUrl());
                systemConfigService.update(WeixinModuleSystemConfig.SUBSCRIBE_WELCOME_MESSAGE, form.getSubscribeMessage());

                WxMpInMemoryConfigStorage configStorage = (WxMpInMemoryConfigStorage) wxMpConfigStorage;
                configStorage.setAppId(form.getAppId());
                configStorage.setToken(form.getToken());
                configStorage.setAesKey(form.getAesKey());

                if (StringUtils.isNotBlank(form.getOauth2CallbackUrl())) {
                    try {
                        String oauth2Url = wxMpService.oauth2buildAuthorizationUrl(form.getOauth2CallbackUrl(), SNSAPI_USERINFO, null);
                        context.put("oauth2Url", oauth2Url);
                        systemConfigService.update(WeixinModuleSystemConfig.AUTHORIZATION_URL, oauth2Url);
                    } catch (Exception e) {
                        throw new ActionException("Can't build Oauth2 Url", e);
                    }
                }
                applicationContext.publishEvent(new AdminWeixinSettingsEvent(currentUser().orElseThrow(RuntimeException::new)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addComponent(convertToTable(map -> map.put("网页授权链接", stringObjectMap.get("oauth2Url")))));
    }

    @GetMapping("/menu")
    public ModelAndView weixinMenuPage(final AdminWeixinMenuForm form) {
        try {
            WxMpMenu wxMpMenu = wxMpService.getMenuService().menuGet();
            if (Objects.nonNull(wxMpMenu)) {
                form.setContent(new GsonBuilder().setPrettyPrinting().create().toJson(wxMpService.getMenuService().menuGet().getMenu()));
            } else {
                form.setContent("当前公众号还未设置自定义菜单");
            }
            return page()
                    .addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "微信公众平台", "weixin", "公众号菜单")
                    .build();
        } catch (Exception e) {
            return page(viewHelper.nextWarningOverview("无法获取公众号菜单", "")
                    .addComponent(new P(e.getMessage()))
                    .addLink("返回", QXCMP_ADMIN_URL + "/weixin")).build();
        }
    }

    @PostMapping("/menu")
    public ModelAndView weixinMenuPage(@Valid final AdminWeixinMenuForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page()
                    .addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "微信公众平台", "weixin", "公众号菜单")
                    .build();
        }

        return submitForm(form, context -> {
            try {
                wxMpService.getMenuService().menuCreate(form.getContent());
                applicationContext.publishEvent(new AdminWeixinSettingsEvent(currentUser().orElseThrow(RuntimeException::new)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @GetMapping("/weixin")
    public ModelAndView weixinPayPage(final AdminFinanceWeixinForm form) {

        form.setEnable(systemConfigService.getBoolean(WeixinModuleSystemConfig.PAYMENT_ENABLE).orElse(WeixinModuleSystemConfig.PAYMENT_ENABLE_DEFAULT));
        form.setTradeType(systemConfigService.getString(QxcmpSystemConfig.WECHAT_PAYMENT_DEFAULT_TRADE_TYPE).orElse(QxcmpSystemConfig.WECHAT_PAYMENT_DEFAULT_TRADE_TYPE_DEFAULT));
        form.setAppId(systemConfigService.getString(WeixinModuleSystemConfig.APP_ID).orElse(""));
        form.setMchId(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_MCH_ID).orElse(""));
        form.setMchKey(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_MCH_KEY).orElse(""));
        form.setNotifyUrl(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_NOTIFY_URL).orElse(""));
        form.setSubAppId(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_SUB_APP_ID).orElse(""));
        form.setSubMchId(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_SUB_MCH_ID).orElse(""));
        form.setKeyPath(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_KEY_PATH).orElse(""));

        return page()
                .addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "财务管理", "finance", "微信支付配置")
                .addObject("selection_items_tradeType", SUPPORT_WEIXIN_PAYMENT)
                .build();
    }

    @PostMapping("/weixin")
    public ModelAndView weixinPayPage(@Valid final AdminFinanceWeixinForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return page()
                    .addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "财务管理", "finance", "微信支付配置")
                    .addObject("selection_items_tradeType", SUPPORT_WEIXIN_PAYMENT)
                    .build();
        }

        return submitForm(form, context -> {
            systemConfigService.update(WeixinModuleSystemConfig.PAYMENT_ENABLE, String.valueOf(form.isEnable()));
            systemConfigService.update(QxcmpSystemConfig.WECHAT_PAYMENT_DEFAULT_TRADE_TYPE, form.getTradeType());
            systemConfigService.update(WeixinModuleSystemConfig.APP_ID, form.getAppId());
            systemConfigService.update(WeixinModuleSystemConfig.PAYMENT_MCH_ID, form.getMchId());
            systemConfigService.update(WeixinModuleSystemConfig.PAYMENT_MCH_KEY, form.getMchKey());
            systemConfigService.update(WeixinModuleSystemConfig.PAYMENT_SUB_APP_ID, form.getSubAppId());
            systemConfigService.update(WeixinModuleSystemConfig.PAYMENT_SUB_MCH_ID, form.getSubMchId());
            systemConfigService.update(WeixinModuleSystemConfig.PAYMENT_NOTIFY_URL, form.getNotifyUrl());
            systemConfigService.update(WeixinModuleSystemConfig.PAYMENT_KEY_PATH, form.getKeyPath());
            systemConfigService.update(QxcmpSystemConfig.FINANCE_PAYMENT_SUPPORT_WEIXIN, systemConfigService.getBoolean(WeixinModuleSystemConfig.PAYMENT_ENABLE).orElse(WeixinModuleSystemConfig.PAYMENT_ENABLE_DEFAULT).toString());

            wxPayConfig.setAppId(form.getAppId());
            wxPayConfig.setMchId(form.getMchId());
            wxPayConfig.setMchKey(form.getMchKey());
            wxPayConfig.setSubAppId(form.getSubAppId());
            wxPayConfig.setSubMchId(form.getSubMchId());
            wxPayConfig.setNotifyUrl(form.getNotifyUrl());
            wxPayConfig.setKeyPath(form.getKeyPath());
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_ADMIN_URL + "/finance/weixin"));
    }
}
