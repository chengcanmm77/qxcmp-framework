package com.qxcmp.weixin.event;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import java.util.Map;

/**
 * 文本消息
 *
 * @author aaric
 */
public class WeixinMpTextMessage extends BaseWeixinMessageEvent {
    public WeixinMpTextMessage(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager wxSessionManager, WxMpXmlOutMessage wxMpXmlOutMessage) {
        super(wxMpXmlMessage, context, wxMpService, wxSessionManager, wxMpXmlOutMessage);
    }
}
