package com.qxcmp.core;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.qxcmp.config.SystemConfigService;
import com.qxcmp.statistics.AccessAddressService;
import com.qxcmp.util.IpAddressResolver;
import com.qxcmp.weixin.WeixinMpMessageHandler;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * 清醒内容管理平台总配置
 * <p>
 * 包含了平台通用常量
 * <p>
 * 负责创建Spring IoC初始Bean
 *
 * @author aaric
 */
@Configuration
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@RequiredArgsConstructor
public class QxcmpConfiguration {

    public static final String QXCMP = "清醒内容管理平台";
    public static final String QXCMP_BACKEND_URL = "/admin";
    public static final String QXCMP_ACCOUNT_URL = "/account";
    public static final String QXCMP_LOGIN_URL = "/login";
    public static final String QXCMP_LOGOUT_URL = "/logout";
    public static final String QXCMP_FILE_UPLOAD_TEMP_FOLDER = "/tmp/";

    private final WeixinMpMessageHandler defaultMessageHandler;
    private final SystemConfigService systemConfigService;
    private final ApplicationContext applicationContext;
    private final AccessAddressService accessAddressService;
    private final IpAddressResolver ipAddressResolver;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(systemConfigService.getInteger(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_TASK_EXECUTOR_CORE_POOL_SIZE).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_TASK_EXECUTOR_CORE_POOL_SIZE_DEFAULT_VALUE));
        threadPoolTaskExecutor.setMaxPoolSize(systemConfigService.getInteger(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_TASK_EXECUTOR_MAX_POOL_SIZE).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_TASK_EXECUTOR_MAX_POOL_SIZE_DEFAULT_VALUE));
        threadPoolTaskExecutor.setQueueCapacity(systemConfigService.getInteger(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_TASK_EXECUTOR_QUEUE_CAPACITY).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_TASK_EXECUTOR_QUEUE_CAPACITY_DEFAULT_VALUE));
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setProtocol("smtp");
        javaMailSender.setHost(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME_DEFAULT_VALUE));
        javaMailSender.setPort(systemConfigService.getInteger(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_PORT).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_PORT_DEFAULT_VALUE));
        javaMailSender.setUsername(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_USERNAME).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_USERNAME_DEFAULT_VALUE));
        javaMailSender.setPassword(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD_DEFAULT_VALUE));

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME).orElse(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME_DEFAULT_VALUE));
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.port", systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_PORT).orElse(String.valueOf(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_EMAIL_PORT_DEFAULT_VALUE)));
        properties.put("mail.smtp.socketFactory.port", javaMailSender.getPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.starttls.enable", "true");

        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_APP_ID).orElse(""));
        configStorage.setSecret(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_SECRET).orElse(""));
        configStorage.setToken(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_TOKEN).orElse(""));
        configStorage.setAesKey(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_AES_KEY).orElse(""));
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
        wxPayConfig.setAppId(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_APP_ID).orElse(""));
        wxPayConfig.setMchId(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_MCH_ID).orElse(""));
        wxPayConfig.setMchKey(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_MCH_KEY).orElse(""));
        wxPayConfig.setSubAppId(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_SUB_APP_ID).orElse(""));
        wxPayConfig.setSubMchId(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_SUB_MCH_ID).orElse(""));
        wxPayConfig.setNotifyUrl(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_NOTIFY_URL).orElse(""));
        wxPayConfig.setKeyPath(systemConfigService.getString(QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_WECHAT_KEY_PATH).orElse(""));
        wxPayConfig.setTradeType("JSAPI");
        return wxPayConfig;
    }

    @Bean
    public DeviceResolver deviceResolver() {
        return new LiteDeviceResolver();
    }
}
