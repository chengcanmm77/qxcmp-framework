package com.qxcmp.web.api;

import com.qxcmp.core.QxcmpConfiguration;
import com.qxcmp.web.QxcmpController;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.qxcmp.core.QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_DEBUG;

/**
 * 微信公众平台网关路由
 * <p>
 * 负责处理所有平台发送过来的消息，包括 <ol> <li>安全性验证，即消息是否来自微信服务器</li> <li>处理微信服务器发来的消息，并回复</li> </ol>
 *
 * @author aaric
 */
@Controller
@RequestMapping("/api/wxmp-cgi")
@AllArgsConstructor
public class WeixinMpAPI extends QxcmpController {

    private WxMpService wxMpService;

    private WxMpConfigStorage wxMpConfigStorage;

    private WxMpMessageRouter wxMpMessageRouter;

    private QxcmpConfiguration qxcmpConfiguration;

    /**
     * 微信公众平台网关路由，负责处理所有平台发送过来的消息
     *
     * @param request  微信服务器发来的请求
     * @param response 返回给微信服务器的响应
     * @throws IOException 抛出{@link IOException}
     */
    @RequestMapping("")
    public void gateWay(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String echostr = request.getParameter("echostr");


        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().print(echostr);
            return;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ? "raw" : request.getParameter("encrypt_type");

        WxMpXmlMessage inMessage;

        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
        } else if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
        } else {
            response.getWriter().println("不可识别的加密类型");
            return;
        }

        WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);

        if (outMessage != null) {
            if ("raw".equals(encryptType)) {
                response.getWriter().write(outMessage.toXml());
            } else if ("aes".equals(encryptType)) {
                response.getWriter().write(outMessage.toEncryptedXml(wxMpConfigStorage));
            }
        } else {
            response.getWriter().write("success");
        }
    }

    /**
     * 微信网页开发JS Config接口
     * <p>
     * 用于获取当前Url的JS Config配置
     *
     * @param url 页面Url
     * @return JS Config 配置
     */
    @GetMapping("/jsapi")
    public ResponseEntity<WxJsapiSignature> jsApi(@RequestParam String url) throws WxErrorException {

        try {
            me.chanjar.weixin.common.bean.WxJsapiSignature wxJsapiSignature = wxMpService.createJsapiSignature(url);

            WxJsapiSignature jsapiSignature = new WxJsapiSignature();

            jsapiSignature.setAppId(wxJsapiSignature.getAppId());
            jsapiSignature.setNonceStr(wxJsapiSignature.getNonceStr());
            jsapiSignature.setSignature(wxJsapiSignature.getSignature());
            jsapiSignature.setTimestamp(wxJsapiSignature.getTimestamp());
            jsapiSignature.setUrl(wxJsapiSignature.getUrl());
            jsapiSignature.setDebug(systemConfigService.getBoolean(SYSTEM_CONFIG_WECHAT_DEBUG).orElse(false));

            return ResponseEntity.ok(jsapiSignature);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }

    }

    public class WxJsapiSignature extends me.chanjar.weixin.common.bean.WxJsapiSignature {

        private boolean debug;

        public boolean isDebug() {
            return debug;
        }

        public void setDebug(boolean debug) {
            this.debug = debug;
        }
    }

}
