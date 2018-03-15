package com.qxcmp.weixin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.page.AbstractQxcmpAdminPage;
import com.qxcmp.web.view.elements.divider.HorizontalDivider;
import com.qxcmp.web.view.modules.table.AbstractTable;
import com.qxcmp.weixin.WeixinModuleSystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.weixin.WeixinModuleNavigation.ADMIN_MENU_WEIXIN;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminWeixinOverviewPage extends AbstractQxcmpAdminPage {

    @Override
    public void render() {
        setMenu(ADMIN_MENU_WEIXIN, "");
        addComponent(viewHelper.nextInfoOverview("微信平台配置")
                .addComponent(new HorizontalDivider("公众号配置"))
                .addComponent(getMpOverviewDetailsTable())
                .addComponent(new HorizontalDivider("微信支付配置"))
                .addComponent(getPaymentDetailsTable()));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("微信平台");
    }

    private AbstractTable getMpOverviewDetailsTable() {
        return viewHelper.nextTable(objectObjectMap -> {
            objectObjectMap.put("App ID", systemConfigService.getString(WeixinModuleSystemConfig.APP_ID).orElse(""));
            objectObjectMap.put("App Secret", systemConfigService.getString(WeixinModuleSystemConfig.APP_SECRET).orElse(""));
            objectObjectMap.put("Token", systemConfigService.getString(WeixinModuleSystemConfig.TOKEN).orElse(""));
            objectObjectMap.put("AES Key", systemConfigService.getString(WeixinModuleSystemConfig.AES_KEY).orElse(""));
            objectObjectMap.put("授权回调链接", systemConfigService.getString(WeixinModuleSystemConfig.OAUTH2_CALLBACK_URL).orElse(""));
            objectObjectMap.put("网页授权链接", systemConfigService.getString(WeixinModuleSystemConfig.AUTHORIZATION_URL).orElse(""));
            objectObjectMap.put("调试模式", systemConfigService.getString(WeixinModuleSystemConfig.DEBUG_MODE).orElse(""));
            objectObjectMap.put("欢迎语", systemConfigService.getString(WeixinModuleSystemConfig.SUBSCRIBE_WELCOME_MESSAGE).orElse(""));
        });
    }

    private AbstractTable getPaymentDetailsTable() {
        return viewHelper.nextTable(objectObjectMap -> {
            objectObjectMap.put("是否开启", systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_ENABLE).orElse(""));
            objectObjectMap.put("商户 ID", systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_MCH_ID).orElse(""));
            objectObjectMap.put("商户 Key", systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_MCH_KEY).orElse(""));
            objectObjectMap.put("子商户 APP ID", systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_SUB_APP_ID).orElse(""));
            objectObjectMap.put("子商户 MCH Secret", systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_SUB_MCH_ID).orElse(""));
            objectObjectMap.put("Key Path", systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_KEY_PATH).orElse(""));
            objectObjectMap.put("通知 Url", systemConfigService.getString(WeixinModuleSystemConfig.PAYMENT_NOTIFY_URL).orElse(""));
        });
    }
}
