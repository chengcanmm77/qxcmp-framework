package com.qxcmp.redeem.listener;

import com.qxcmp.config.SiteService;
import com.qxcmp.config.SystemConfigChangeEvent;
import com.qxcmp.message.FeedService;
import com.qxcmp.redeem.RedeemKeyEvent;
import com.qxcmp.redeem.RedeemModuleSystemConfig;
import com.qxcmp.redeem.event.AdminRedeemGenerateEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.qxcmp.redeem.RedeemModuleSecurity.*;


/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminRedeemEventListener {

    private final FeedService feedService;
    private final SiteService siteService;

    @EventListener
    public void onGenerateEvent(AdminRedeemGenerateEvent event) {
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_REDEEM_GENERATE, event.getTarget(),
                String.format("%s 生成了%d个 <a href='https://%s/admin/redeem'>兑换码</a>",
                        event.getTarget().getDisplayName(),
                        event.getCount(),
                        siteService.getDomain()));
    }

    @EventListener
    public void onSettingsEvent(SystemConfigChangeEvent event) {
        if (StringUtils.equals(event.getNamespace(), RedeemModuleSystemConfig.class.getName())) {
            feedService.feedForUserGroup(PRIVILEGE_ADMIN_REDEEM_SETTING, event.getUser(),
                    String.format("%s 修改了 <a href='https:///%s/admin/redeem/settings'>兑换码配置</a>",
                            event.getUser().getDisplayName(),
                            siteService.getDomain()),
                    String.format("[%s]从[%s]修改为[%s]", event.getName(), event.getPrevious(), event.getCurrent()));
        }
    }

    @EventListener
    public void onRedeemEvent(RedeemKeyEvent event) {
        feedService.feedForUserGroup(PRIVILEGE_ADMIN_REDEEM, event.getUser()
                , String.format("%s 使用了兑换码 [%s][%s]", event.getUser().getDisplayName(), event.getRedeemKey().getType(), event.getRedeemKey().getContent()));
    }
}
