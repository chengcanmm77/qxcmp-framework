package com.qxcmp.core;

import com.qxcmp.config.SystemConfigLoader;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.context.annotation.Configuration;

/**
 * 平台系统配置
 *
 * @author Aaric
 */
@Configuration
public class QxcmpSystemConfigConfiguration implements SystemConfigLoader {

    /*
     * 系统核心相关配置
     * */
    public static String SYSTEM_CONFIG_FILE_UPLOAD_TEMP_FILE_RESERVE_DURATION = "";
    /* 上传临时文件保留时间（分）, 默认为1天 */
    public static int SYSTEM_CONFIG_FILE_UPLOAD_TEMP_FILE_RESERVE_DURATION_DEFAULT_VALUE = 3600 * 24;

    /*
     * 账户注册相关配置
     * */
    public static String SYSTEM_CONFIG_ACCOUNT_ENABLE_USERNAME = "";
    public static String SYSTEM_CONFIG_ACCOUNT_ENABLE_EMAIL = "";
    public static String SYSTEM_CONFIG_ACCOUNT_ENABLE_PHONE = "";
    public static String SYSTEM_CONFIG_ACCOUNT_ENABLE_INVITE = "";

    /*
     * 任务调度相关配置
     * */
    public static String SYSTEM_CONFIG_TASK_EXECUTOR_CORE_POOL_SIZE = "task.executor.core.pool.size";
    public static Integer SYSTEM_CONFIG_TASK_EXECUTOR_CORE_POOL_SIZE_DEFAULT_VALUE = 10;
    public static String SYSTEM_CONFIG_TASK_EXECUTOR_MAX_POOL_SIZE = "task.executor.max.pool.size";
    public static Integer SYSTEM_CONFIG_TASK_EXECUTOR_MAX_POOL_SIZE_DEFAULT_VALUE = 50;
    public static String SYSTEM_CONFIG_TASK_EXECUTOR_QUEUE_CAPACITY = "task.executor.queue.capacity";
    public static Integer SYSTEM_CONFIG_TASK_EXECUTOR_QUEUE_CAPACITY_DEFAULT_VALUE = 100;

    /*
     * 系统会话相关配置
     * */
    public static String SYSTEM_CONFIG_SESSION_TIMEOUT = "session.timeout";
    public static Integer SYSTEM_CONFIG_SESSION_TIMEOUT_DEFAULT_VALUE = 3600;
    public static String SYSTEM_CONFIG_SESSION_MAX_ACTIVE_COUNT = "session.max.active.count";
    public static Integer SYSTEM_CONFIG_SESSION_MAX_ACTIVE_COUNT_DEFAULT_VALUE = 1;
    public static String SYSTEM_CONFIG_SESSION_MAX_PREVENT_LOGIN = "session.max.prevent.login";
    public static Boolean SYSTEM_CONFIG_SESSION_MAX_PREVENT_LOGIN_DEFAULT_VALUE = false;

    /*
     * 网站配置
     * */
    public static String SYSTEM_CONFIG_SITE_DOMAIN = "";
    public static String SYSTEM_CONFIG_SITE_TITLE = "";
    public static String SYSTEM_CONFIG_SITE_KEYWORDS = "";
    public static String SYSTEM_CONFIG_SITE_DESCRIPTION = "";
    public static String SYSTEM_CONFIG_SITE_LOGO = "";
    public static String SYSTEM_CONFIG_SITE_LOGO_DEFAULT_VALUE = "/assets/images/logo.png";
    public static String SYSTEM_CONFIG_SITE_FAVICON = "";
    public static String SYSTEM_CONFIG_SITE_FAVICON_DEFAULT_VALUE = "/assets/icons/favicon.png";

    /*
     * 邮件服务相关配置
     * */
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME = "message.email.hostname";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME_DEFAULT_VALUE = "smtp.qq.com";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_PORT = "message.email.port";
    public static int SYSTEM_CONFIG_MESSAGE_EMAIL_PORT_DEFAULT_VALUE = 465;
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_USERNAME = "message.email.username";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_USERNAME_DEFAULT_VALUE = "qxcmp@foxmail.com";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD = "message.email.password";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD_DEFAULT_VALUE = "krdhkmdilrhwceed";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT = "message.email.account.reset.subject";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT_DEFAULT_VALUE = "请重置你的密码";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT = "message.email.account.reset.content";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT_DEFAULT_VALUE = "点击该链接来重置你的密码: ${link}";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT = "message.email.account.activate.subject";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT_DEFAULT_VALUE = "请激活你的账户";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT = "message.email.account.activate.content";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT_DEFAULT_VALUE = "点击该链接来激活您的账户： ${link}";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT = "message.email.account.binding.subject";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT_DEFAULT_VALUE = "账户绑定验证码";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT = "message.email.account.binding.content";
    public static String SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT_DEFAULT_VALUE = "你的账户绑定验证码为： ${captcha}";

    /*
     * 短信服务相关配置
     * */
    public static String SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY = "message.sms.accessKey";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY_DEFAULT_VALUE = "";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET = "message.sms.accessSecret";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET_DEFAULT_VALUE = "";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_END_POINT = "message.sms.endPoint";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_END_POINT_DEFAULT_VALUE = "";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF = "message.sms.topicRef";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF_DEFAULT_VALUE = "";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_SIGN = "message.sms.sign";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_SIGN_DEFAULT_VALUE = "";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE = "message.sms.captcha.template.code";
    public static String SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE_DEFAULT_VALUE = "";

    /*
     * 系统认证相关配置
     * */
    public static String SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_THRESHOLD = "";
    public static Integer SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_THRESHOLD_DEFAULT_VALUE = 3;
    public static String SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_LENGTH = "";
    public static Integer SYSTEM_CONFIG_AUTHENTICATION_CAPTCHA_LENGTH_DEFAULT_VALUE = 4;
    public static String SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_LOCK = "";
    public static Boolean SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_LOCK_DEFAULT_VALUE = false;
    public static String SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD = "";
    public static Integer SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD_DEFAULT_VALUE = 5;
    public static String SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_LOCK_DURATION = "";
    public static Integer SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_LOCK_DURATION_DEFAULT_VALUE = 15;
    public static String SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_EXPIRE = "";
    public static Boolean SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_EXPIRE_DEFAULT_VALUE = false;
    public static String SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_EXPIRE_DURATION = "";
    public static Integer SYSTEM_CONFIG_AUTHENTICATION_ACCOUNT_EXPIRE_DURATION_DEFAULT_VALUE = 180;
    public static String SYSTEM_CONFIG_AUTHENTICATION_CREDENTIAL_EXPIRE = "";
    public static Boolean SYSTEM_CONFIG_AUTHENTICATION_CREDENTIAL_EXPIRE_DEFAULT_VALUE = false;
    public static String SYSTEM_CONFIG_AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION = "";
    public static Integer SYSTEM_CONFIG_AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION_DEFAULT_VALUE = 90;
    public static String SYSTEM_CONFIG_AUTHENTICATION_CREDENTIAL_UNIQUE = "";
    public static Boolean SYSTEM_CONFIG_AUTHENTICATION_CREDENTIAL_UNIQUE_DEFAULT_VALUE = false;

    /*
     * 系统字典相关配置
     * */
    public static String SYSTEM_CONFIG_DICTIONARY_INITIAL_FLAG = "";

    /*
     * 系统地区相关配置
     * */
    public static String SYSTEM_CONFIG_REGION_INITIAL_FLAG = "";

    /*
     * 系统水印相关配置
     * */
    public static String SYSTEM_CONFIG_IMAGE_WATERMARK_ENABLE = "";
    public static boolean SYSTEM_CONFIG_IMAGE_WATERMARK_ENABLE_DEFAULT_VALUE = true;
    public static String SYSTEM_CONFIG_IMAGE_WATERMARK_NAME = "";
    public static String SYSTEM_CONFIG_IMAGE_WATERMARK_POSITION = "";
    public static int SYSTEM_CONFIG_IMAGE_WATERMARK_POSITION_DEFAULT_VALUE = Positions.BOTTOM_RIGHT.ordinal();
    public static String SYSTEM_CONFIG_IMAGE_WATERMARK_FONT_SIZE = "";
    public static int SYSTEM_CONFIG_IMAGE_WATERMARK_FONT_SIZE_DEFAULT_VALUE = 14;

    /*
     * 微信公众平台相关配置
     * */
    public static String SYSTEM_CONFIG_WECHAT_APP_ID = "wechat.app.id";
    public static String SYSTEM_CONFIG_WECHAT_SECRET = "wechat.secret";
    public static String SYSTEM_CONFIG_WECHAT_TOKEN = "wechat.token";
    public static String SYSTEM_CONFIG_WECHAT_AES_KEY = "wechat.aes.key";
    public static String SYSTEM_CONFIG_WECHAT_OAUTH2_CALLBACK_URL = "";
    public static String SYSTEM_CONFIG_WECHAT_OAUTH2_AUTHORIZATION_URL;
    public static String SYSTEM_CONFIG_WECHAT_SUBSCRIBE_WELCOME_MESSAGE = "";
    public static String SYSTEM_CONFIG_WECHAT_DEBUG = "";
    public static String SYSTEM_CONFIG_WECHAT_PAYMENT_ENABLE;
    public static boolean SYSTEM_CONFIG_WECHAT_PAYMENT_ENABLE_DEFAULT_VALUE = false;
    public static String SYSTEM_CONFIG_WECHAT_MCH_ID = "wechat.mch.id";
    public static String SYSTEM_CONFIG_WECHAT_MCH_KEY = "wechat.mch.key";
    public static String SYSTEM_CONFIG_WECHAT_SUB_APP_ID = "wechat.sub.app.id";
    public static String SYSTEM_CONFIG_WECHAT_SUB_MCH_ID = "wechat.sub.mch.id";
    public static String SYSTEM_CONFIG_WECHAT_KEY_PATH = "wechat.key.path";
    public static String SYSTEM_CONFIG_WECHAT_NOTIFY_URL = "wechat.notify.url";
    public static String SYSTEM_CONFIG_FINANCE_PAYMENT_SUPPORT_WEIXIN;
    public static boolean SYSTEM_CONFIG_FINANCE_PAYMENT_SUPPORT_WEIXIN_DEFAULT_VALUE = false;
    public static String SYSTEM_CONFIG_WECHAT_PAYMENT_DEFAULT_TRADE_TYPE;
    public static String SYSTEM_CONFIG_WECHAT_PAYMENT_DEFAULT_TRADE_TYPE_DEFAULT_VALUE = "NATIVE";
    public static String SYSTEM_CONFIG_FINANCE_PAYMENT_SUPPORT_ALIPAY;
    public static boolean SYSTEM_CONFIG_FINANCE_PAYMENT_SUPPORT_ALIPAY_DEFAULT_VALUE = false;

    /*
     * 兑换码相关配置
     * */
    public static String SYSTEM_CONFIG_REDEEM_ENABLE;
    public static boolean SYSTEM_CONFIG_REDEEM_ENABLE_DEFAULT_VALUE = true;
    public static String SYSTEM_CONFIG_REDEEM_DEFAULT_EXPIRE_DURATION;
    public static int SYSTEM_CONFIG_REDEEM_DEFAULT_EXPIRE_DURATION_DEFAULT_VALUE = 7200;
    public static String SYSTEM_CONFIG_REDEEM_TYPE_LIST;

    /*
     * 文章相关配置
     * */
    public static String SYSTEM_CONFIG_ARTICLE_CHANNEL_CATALOG = "";

    /*
     * 商城相关配置
     * */
    public static String SYSTEM_CONFIG_MALL_COMMODITY_CATALOG = "";

    /*
     * 链接管理配置
     * */
    public static String SYSTEM_CONFIG_LINK_TYPE = "";
    public static String SYSTEM_CONFIG_LINK_TYPE_DEFAULT_VALUE = "友情链接\n热门标签";
}
