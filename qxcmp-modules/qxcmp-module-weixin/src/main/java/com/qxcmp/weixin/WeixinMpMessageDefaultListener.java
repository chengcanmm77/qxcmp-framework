package com.qxcmp.weixin;

import com.qxcmp.config.SystemConfigService;
import com.qxcmp.user.UserService;
import com.qxcmp.weixin.event.WeixinMpSubscribeEvent;
import com.qxcmp.weixin.event.WeixinMpUnsubscribeEvent;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.qxcmp.weixin.WeixinModuleSystemConfig.SUBSCRIBE_WELCOME_MESSAGE;

/**
 * 平台默认微信消息事件监听器
 * <p>
 * 负责处理关注和取关事件
 *
 * @author aaric
 */
@Component
@RequiredArgsConstructor
public class WeixinMpMessageDefaultListener {

    private final WeixinService wechatService;

    private final UserService userService;

    private final SystemConfigService systemConfigService;

    @EventListener
    public void onSubscribe(WeixinMpSubscribeEvent event) {
        try {
            WxMpUser wxMpUser = event.getWxMpService().getUserService().userInfo(event.getWxMpXmlMessage().getFromUser());

            wechatService.getSyncService().syncUser(wxMpUser);

            event.setWxMpXmlOutMessage(WxMpXmlOutMessage.TEXT().content(systemConfigService.getString(SUBSCRIBE_WELCOME_MESSAGE).orElse("")).build());
        } catch (WxErrorException e) {
            event.setWxMpXmlOutMessage(WxMpXmlOutMessage.TEXT().content("关注失败，平台出现错误: " + e.getMessage()).build());
        }
    }

    @EventListener
    public void onUnsubscribe(WeixinMpUnsubscribeEvent event) {
        userService.findByOpenID(event.getWxMpXmlMessage().getFromUser()).ifPresent(user -> userService.update(user.getId(), u -> u.setSubscribe(false)));
    }
}
