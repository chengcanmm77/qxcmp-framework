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
public class QxcmpSystemConfig implements SystemConfigLoader {

    /*
     * 系统核心相关配置
     * */

    /**
     * 上传临时文件清理周期（秒），默认为一天
     */
    public static String FILE_UPLOAD_TEMP_FILE_RESERVE_DURATION = "";
    public static Integer FILE_UPLOAD_TEMP_FILE_RESERVE_DURATION_DEFAULT = 3600 * 24;

    /*
     * 账户注册相关配置
     * */

    public static String ACCOUNT_ENABLE_USERNAME = "";
    public static String ACCOUNT_ENABLE_EMAIL = "";
    public static String ACCOUNT_ENABLE_PHONE = "";
    public static String ACCOUNT_ENABLE_INVITE = "";

    /*
     * 任务调度相关配置
     * */

    public static String TASK_EXECUTOR_CORE_POOL_SIZE = "qxcmp.task.executor.core.pool.size";
    public static Integer TASK_EXECUTOR_CORE_POOL_SIZE_DEFAULT = 10;
    public static String TASK_EXECUTOR_MAX_POOL_SIZE = "qxcmp.task.executor.max.pool.size";
    public static Integer TASK_EXECUTOR_MAX_POOL_SIZE_DEFAULT = 50;
    public static String TASK_EXECUTOR_QUEUE_CAPACITY = "qxcmp.task.executor.queue.capacity";
    public static Integer TASK_EXECUTOR_QUEUE_CAPACITY_DEFAULT = 100;

    /*
     * 系统会话相关配置
     * */

    public static String SESSION_TIMEOUT = "qxcmp.session.timeout";
    public static Integer SESSION_TIMEOUT_DEFAULT = 3600;
    public static String SESSION_MAX_ACTIVE_COUNT = "qxcmp.session.max.active.count";
    public static Integer SESSION_MAX_ACTIVE_COUNT_DEFAULT = 1;
    public static String SESSION_PREVENT_LOGIN = "qxcmp.session.max.prevent.login";
    public static Boolean SESSION_PREVENT_LOGIN_DEFAULT = false;

    /*
     * 网站配置
     * */

    public static String SITE_DOMAIN = "";
    public static String SITE_TITLE = "";
    public static String SITE_KEYWORDS = "";
    public static String SITE_DESCRIPTION = "";
    public static String SITE_LOGO = "";
    public static String SITE_LOGO_DEFAULT = "/assets/images/logo.png";
    public static String SITE_FAVICON = "";
    public static String SITE_FAVICON_DEFAULT = "/assets/icons/favicon.png";

    /*
     * 邮件服务相关配置
     * */

    public static String MESSAGE_EMAIL_HOSTNAME = "qxcmp.message.email.hostname";
    public static String MESSAGE_EMAIL_HOSTNAME_DEFAULT = "smtp.qq.com";
    public static String MESSAGE_EMAIL_PORT = "qxcmp.message.email.port";
    public static Integer MESSAGE_EMAIL_PORT_DEFAULT = 465;
    public static String MESSAGE_EMAIL_USERNAME = "qxcmp.message.email.username";
    public static String MESSAGE_EMAIL_USERNAME_DEFAULT = "qxcmp@foxmail.com";
    public static String MESSAGE_EMAIL_PASSWORD = "qxcmp.message.email.password";
    public static String MESSAGE_EMAIL_PASSWORD_DEFAULT = "krdhkmdilrhwceed";
    public static String MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT = "qxcmp.message.email.account.reset.subject";
    public static String MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT_DEFAULT = "请重置你的密码";
    public static String MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT = "qxcmp.message.email.account.reset.content";
    public static String MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT_DEFAULT = "点击该链接来重置你的密码: ${link}";
    public static String MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT = "qxcmp.message.email.account.activate.subject";
    public static String MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT_DEFAULT = "请激活你的账户";
    public static String MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT = "qxcmp.message.email.account.activate.content";
    public static String MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT_DEFAULT = "点击该链接来激活您的账户： ${link}";
    public static String MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT = "qxcmp.message.email.account.binding.subject";
    public static String MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT_DEFAULT = "账户绑定验证码";
    public static String MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT = "qxcmp.message.email.account.binding.content";
    public static String MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT_DEFAULT = "你的账户绑定验证码为： ${captcha}";

    /*
     * 短信服务相关配置
     * */

    public static String MESSAGE_SMS_ACCESS_KEY = "qxcmp.message.sms.accessKey";
    public static String MESSAGE_SMS_ACCESS_SECRET = "qxcmp.message.sms.accessSecret";
    public static String MESSAGE_SMS_END_POINT = "qxcmp.message.sms.endPoint";
    public static String MESSAGE_SMS_TOPIC_REF = "qxcmp.message.sms.topicRef";
    public static String MESSAGE_SMS_SIGN = "qxcmp.message.sms.sign";
    public static String MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE = "qxcmp.message.sms.captcha.template.code";

    /*
     * 系统认证相关配置
     * */

    public static String AUTHENTICATION_CAPTCHA_THRESHOLD = "";
    public static Integer AUTHENTICATION_CAPTCHA_THRESHOLD_DEFAULT = 3;
    public static String AUTHENTICATION_CAPTCHA_LENGTH = "";
    public static Integer AUTHENTICATION_CAPTCHA_LENGTH_DEFAULT = 4;
    public static String AUTHENTICATION_ACCOUNT_LOCK = "";
    public static Boolean AUTHENTICATION_ACCOUNT_LOCK_DEFAULT = false;
    public static String AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD = "";
    public static Integer AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD_DEFAULT = 5;
    public static String AUTHENTICATION_ACCOUNT_LOCK_DURATION = "";
    public static Integer AUTHENTICATION_ACCOUNT_LOCK_DURATION_DEFAULT = 15;
    public static String AUTHENTICATION_ACCOUNT_EXPIRE = "";
    public static Boolean AUTHENTICATION_ACCOUNT_EXPIRE_DEFAULT = false;
    public static String AUTHENTICATION_ACCOUNT_EXPIRE_DURATION = "";
    public static Integer AUTHENTICATION_ACCOUNT_EXPIRE_DURATION_DEFAULT = 180;
    public static String AUTHENTICATION_CREDENTIAL_EXPIRE = "";
    public static Boolean AUTHENTICATION_CREDENTIAL_EXPIRE_DEFAULT = false;
    public static String AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION = "";
    public static Integer AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION_DEFAULT = 90;
    public static String AUTHENTICATION_CREDENTIAL_UNIQUE = "";
    public static Boolean AUTHENTICATION_CREDENTIAL_UNIQUE_DEFAULT = false;

    /*
     * 系统字典相关配置
     * */

    public static String DICTIONARY_INITIAL_FLAG = "";

    /*
     * 系统地区相关配置
     * */

    public static String REGION_INITIAL_FLAG = "";

    /*
     * 系统水印相关配置
     * */

    public static String IMAGE_WATERMARK_ENABLE = "";
    public static Boolean IMAGE_WATERMARK_ENABLE_DEFAULT = true;
    public static String IMAGE_WATERMARK_NAME = "";
    public static String IMAGE_WATERMARK_POSITION = "";
    public static Integer IMAGE_WATERMARK_POSITION_DEFAULT = Positions.BOTTOM_RIGHT.ordinal();
    public static String IMAGE_WATERMARK_FONT_SIZE = "";
    public static Integer IMAGE_WATERMARK_FONT_SIZE_DEFAULT = 14;

    /*
     * 微信公众平台相关配置
     * */

    public static String WECHAT_APP_ID = "qxcmp.wechat.app.id";
    public static String WECHAT_SECRET = "qxcmp.wechat.secret";
    public static String WECHAT_TOKEN = "qxcmp.wechat.token";
    public static String WECHAT_AES_KEY = "qxcmp.wechat.aes.key";
    public static String WECHAT_OAUTH2_CALLBACK_URL = "";
    public static String WECHAT_OAUTH2_AUTHORIZATION_URL;
    public static String WECHAT_SUBSCRIBE_WELCOME_MESSAGE = "";
    public static String WECHAT_DEBUG = "";
    public static String WECHAT_PAYMENT_ENABLE;
    public static Boolean WECHAT_PAYMENT_ENABLE_DEFAULT = false;
    public static String WECHAT_MCH_ID = "qxcmp.wechat.mch.id";
    public static String WECHAT_MCH_KEY = "qxcmp.wechat.mch.key";
    public static String WECHAT_SUB_APP_ID = "qxcmp.wechat.sub.app.id";
    public static String WECHAT_SUB_MCH_ID = "qxcmp.wechat.sub.mch.id";
    public static String WECHAT_KEY_PATH = "qxcmp.wechat.key.path";
    public static String WECHAT_NOTIFY_URL = "qxcmp.wechat.notify.url";
    public static String FINANCE_PAYMENT_SUPPORT_WEIXIN;
    public static Boolean FINANCE_PAYMENT_SUPPORT_WEIXIN_DEFAULT = false;
    public static String WECHAT_PAYMENT_DEFAULT_TRADE_TYPE;
    public static String WECHAT_PAYMENT_DEFAULT_TRADE_TYPE_DEFAULT = "NATIVE";
    public static String FINANCE_PAYMENT_SUPPORT_ALIPAY;
    public static Boolean FINANCE_PAYMENT_SUPPORT_ALIPAY_DEFAULT = false;

    /*
     * 文章相关配置
     * */

    public static String ARTICLE_CHANNEL_CATALOG = "";

    /*
     * 商城相关配置
     * */

    public static String MALL_COMMODITY_CATALOG = "";

    /*
     * 链接管理配置
     * */

    public static String LINK_TYPE = "";
}
