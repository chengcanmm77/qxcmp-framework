package com.qxcmp.weixin;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.qxcmp.config.SystemConfigService;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * @author Aaric
 */
@Configuration
@RequiredArgsConstructor
public class WeixinModule {

    public static final String ADMIN_WEIXIN_URL = ADMIN_URL + "/weixin";
    public static final String WEIXIN_FRAGMENT = "qxcmp/weixin/weixin-mp";

    private final SystemConfigService systemConfigService;
    private final WeixinMpMessageHandler defaultMessageHandler;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(systemConfigService.getString(WeixinModuleSystemConfig.APP_ID).orElse(""));
        configStorage.setSecret(systemConfigService.getString(WeixinModuleSystemConfig.APP_SECRET).orElse(""));
        configStorage.setToken(systemConfigService.getString(WeixinModuleSystemConfig.TOKEN).orElse(""));
        configStorage.setAesKey(systemConfigService.getString(WeixinModuleSystemConfig.AES_KEY).orElse(""));
        return configStorage;
    }

    @Bean
    public WxMpMessageRouter wxMpMessageRouter() {
        WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxMpService());
        wxMpMessageRouter.rule().async(false).handler(defaultMessageHandler).end();
        return wxMpMessageRouter;
    }

    @Bean
    public WxPayService wxPayService() {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig());
        return wxPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(systemConfigService.getString(WeixinModuleSystemConfig.APP_ID).orElse(""));
        wxPayConfig.setMchId(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_MCH_ID).orElse(""));
        wxPayConfig.setMchKey(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_MCH_KEY).orElse(""));
        wxPayConfig.setSubAppId(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_SUB_APP_ID).orElse(""));
        wxPayConfig.setSubMchId(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_SUB_MCH_ID).orElse(""));
        wxPayConfig.setNotifyUrl(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_NOTIFY_URL).orElse(""));
        wxPayConfig.setKeyPath(systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_KEY_PATH).orElse(""));
        wxPayConfig.setTradeType("JSAPI");
        return wxPayConfig;
    }
}
