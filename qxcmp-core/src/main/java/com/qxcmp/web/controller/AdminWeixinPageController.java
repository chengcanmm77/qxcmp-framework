package com.qxcmp.web.controller;

import com.google.gson.GsonBuilder;
import com.qxcmp.audit.ActionException;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.core.event.AdminWeixinSettingsEvent;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.AdminWeixinMenuForm;
import com.qxcmp.web.form.AdminWeixinSettingsForm;
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
import com.qxcmp.weixin.WeixinMpMaterialService;
import com.qxcmp.weixin.WeixinMpMaterialType;
import com.qxcmp.weixin.WeixinService;
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
import java.util.Objects;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;
import static com.qxcmp.core.QxcmpSystemConfig.*;
import static me.chanjar.weixin.common.api.WxConsts.OAuth2Scope.SNSAPI_USERINFO;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_ADMIN_URL + "/weixin")
@RequiredArgsConstructor
public class AdminWeixinPageController extends QxcmpController {

    private final WxMpService wxMpService;
    private final WxMpConfigStorage wxMpConfigStorage;
    private final WeixinMpMaterialService weixinMpMaterialService;
    private final WeixinService weixinService;

    @GetMapping("")
    public ModelAndView weixinPage() {
        return page().addComponent(new Overview("微信公众平台")
                .addComponent(convertToTable(stringObjectMap -> {
                    stringObjectMap.put("App ID", systemConfigService.getString(WECHAT_APP_ID).orElse(""));
                    stringObjectMap.put("App Secret", systemConfigService.getString(WECHAT_SECRET).orElse(""));
                    stringObjectMap.put("Token", systemConfigService.getString(WECHAT_TOKEN).orElse(""));
                    stringObjectMap.put("AES Key", systemConfigService.getString(WECHAT_AES_KEY).orElse(""));
                    stringObjectMap.put("授权回调链接", systemConfigService.getString(WECHAT_OAUTH2_CALLBACK_URL).orElse(""));
                    stringObjectMap.put("网页授权链接", systemConfigService.getString(WECHAT_OAUTH2_AUTHORIZATION_URL).orElse(""));
                    stringObjectMap.put("调试模式", systemConfigService.getString(WECHAT_DEBUG).orElse(""));
                    stringObjectMap.put("欢迎语", systemConfigService.getString(WECHAT_SUBSCRIBE_WELCOME_MESSAGE).orElse(""));
                })))
                .setBreadcrumb("控制台", "", "微信公众平台")
                .setVerticalNavigation(NAVIGATION_ADMIN_WEIXIN, "")
                .build();
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
                .setVerticalNavigation(NAVIGATION_ADMIN_WEIXIN, NAVIGATION_ADMIN_WEIXIN_MATERIAL)
                .build();
    }

    @PostMapping("/material/sync")
    public ResponseEntity<RestfulResponse> userWeixinSyncPage() {
        weixinService.getSyncService().syncMaterials(currentUser().orElseThrow(RuntimeException::new));
        return ResponseEntity.ok(new RestfulResponse(HttpStatus.OK.value()));
    }

    @GetMapping("/material/{id}/preview")
    public ModelAndView materialPreviewPage(@PathVariable String id) {
        return weixinMpMaterialService.findOne(id)
                .filter(weixinMpMaterial -> weixinMpMaterial.getType().equals(WeixinMpMaterialType.NEWS))
                .map(weixinMpMaterial -> page()
                        .addComponent(new Segment().addComponent(new Button("转换为文章", String.format(QXCMP_ADMIN_URL + "/weixin/material/%s/convert", id)).setPrimary())
                                .addComponent(new Overview(weixinMpMaterial.getTitle(), weixinMpMaterial.getAuthor()).addComponent(new HtmlText(weixinMpMaterial.getContent()))))
                        .setBreadcrumb("控制台", "", "微信公众平台", "weixin", "素材管理", "weixin/material", "图文查看")
                        .setVerticalNavigation(NAVIGATION_ADMIN_WEIXIN, NAVIGATION_ADMIN_WEIXIN_MATERIAL))
                .orElse(page(viewHelper.nextWarningOverview("素材不存在或者不为图文素材", "目前仅支持图文素材的查看"))).build();
    }

    @GetMapping("/settings")
    public ModelAndView weixinMpPage(final AdminWeixinSettingsForm form) {

        form.setDebug(systemConfigService.getBoolean(QxcmpSystemConfig.WECHAT_DEBUG).orElse(false));
        form.setAppId(systemConfigService.getString(QxcmpSystemConfig.WECHAT_APP_ID).orElse(""));
        form.setSecret(systemConfigService.getString(QxcmpSystemConfig.WECHAT_SECRET).orElse(""));
        form.setToken(systemConfigService.getString(QxcmpSystemConfig.WECHAT_TOKEN).orElse(""));
        form.setAesKey(systemConfigService.getString(QxcmpSystemConfig.WECHAT_AES_KEY).orElse(""));
        form.setOauth2Url(systemConfigService.getString(QxcmpSystemConfig.WECHAT_OAUTH2_CALLBACK_URL).orElse(""));
        form.setSubscribeMessage(systemConfigService.getString(QxcmpSystemConfig.WECHAT_SUBSCRIBE_WELCOME_MESSAGE).orElse(""));

        return page()
                .addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "微信公众平台", "weixin", "公众号配置")
                .setVerticalNavigation(NAVIGATION_ADMIN_WEIXIN, NAVIGATION_ADMIN_WEIXIN_SETTINGS)
                .build();
    }

    @PostMapping("/settings")
    public ModelAndView weixinMpPage(@Valid final AdminWeixinSettingsForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page()
                    .addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "微信公众平台", "weixin", "公众号配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_WEIXIN, NAVIGATION_ADMIN_WEIXIN_SETTINGS)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                systemConfigService.update(QxcmpSystemConfig.WECHAT_DEBUG, String.valueOf(form.isDebug()));
                systemConfigService.update(QxcmpSystemConfig.WECHAT_APP_ID, form.getAppId());
                systemConfigService.update(QxcmpSystemConfig.WECHAT_SECRET, form.getSecret());
                systemConfigService.update(QxcmpSystemConfig.WECHAT_TOKEN, form.getToken());
                systemConfigService.update(QxcmpSystemConfig.WECHAT_AES_KEY, form.getAesKey());
                systemConfigService.update(QxcmpSystemConfig.WECHAT_OAUTH2_CALLBACK_URL, form.getOauth2Url());
                systemConfigService.update(QxcmpSystemConfig.WECHAT_SUBSCRIBE_WELCOME_MESSAGE, form.getSubscribeMessage());

                WxMpInMemoryConfigStorage configStorage = (WxMpInMemoryConfigStorage) wxMpConfigStorage;
                configStorage.setAppId(form.getAppId());
                configStorage.setSecret(form.getSecret());
                configStorage.setToken(form.getToken());
                configStorage.setAesKey(form.getAesKey());

                if (StringUtils.isNotBlank(form.getOauth2Url())) {
                    try {
                        String oauth2Url = wxMpService.oauth2buildAuthorizationUrl(form.getOauth2Url(), SNSAPI_USERINFO, null);
                        context.put("oauth2Url", oauth2Url);
                        systemConfigService.update(QxcmpSystemConfig.WECHAT_OAUTH2_AUTHORIZATION_URL, oauth2Url);
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
                    .setVerticalNavigation(NAVIGATION_ADMIN_WEIXIN, NAVIGATION_ADMIN_WEIXIN_MENU)
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
                    .setVerticalNavigation(NAVIGATION_ADMIN_WEIXIN, NAVIGATION_ADMIN_WEIXIN_MENU)
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
}
