package com.qxcmp.weixin.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import java.util.Map;

/**
 * 微信消息事件基本类
 *
 * @author aaric
 */
@Data
@AllArgsConstructor
public abstract class BaseWeixinMessageEvent {

    /**
     * 收到的微信消息
     */
    private WxMpXmlMessage wxMpXmlMessage;

    /**
     * 上下文，用于不用监听器交换数据
     */
    private Map<String, Object> context;

    /**
     * 微信服务
     */
    private WxMpService wxMpService;

    /**
     * 会话管理
     */
    private WxSessionManager wxSessionManager;

    /**
     * 要返回的消息
     */
    private WxMpXmlOutMessage wxMpXmlOutMessage;

}
