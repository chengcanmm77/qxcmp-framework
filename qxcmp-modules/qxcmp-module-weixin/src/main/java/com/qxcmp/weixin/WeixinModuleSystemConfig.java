package com.qxcmp.weixin;

import com.qxcmp.config.SystemConfigLoader;
import org.springframework.stereotype.Component;

/**
 * @author Aaric
 */
@Component
public class WeixinModuleSystemConfig implements SystemConfigLoader {
    public static String APP_ID = "weixin.app.id";
    public static String APP_SECRET = "weixin.app.secret";
    public static String TOKEN = "weixin.token";
    public static String AES_KEY = "weixin.aes.key";
    public static String OAUTH2_CALLBACK_URL;
    public static String AUTHORIZATION_URL;
    public static String SUBSCRIBE_WELCOME_MESSAGE;
    public static String DEBUG_MODE;
    public static String PAYMENT_ENABLE;
    public static Boolean PAYMENT_ENABLE_DEFAULT = false;
    public static String PAYMENT_MCH_ID = "weixin.payment.mch.id";
    public static String PAYMENT_MCH_KEY = "weixin.payment.mch.key";
    public static String PAYMENT_SUB_APP_ID = "weixin.payment.sub.app.id";
    public static String PAYMENT_SUB_MCH_ID = "weixin.payment.sub.mch.id";
    public static String PAYMENT_KEY_PATH = "weixin.payment.key.path";
    public static String PAYMENT_NOTIFY_URL = "weixin.payment.notify.url";
}
