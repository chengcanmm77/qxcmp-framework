package com.qxcmp.message;


import com.qxcmp.user.User;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 短信服务平台统一接口
 * <p>
 * 短信服务默认没有开通，需要添加短信管理后台模板来进行相关配置
 * <p>
 * 短信服务配置完成以后便可以进行短信发送服务
 * <p>
 * 短信服务以资源包的形式提供，当资源包耗尽以后短信服务将会失败
 *
 * @author aaric
 */
public interface SmsService {

    /**
     * 验证码短信参数
     */
    String CAPTCHA_PARAMETER = "captcha";

    /**
     * 向手机发送指定短信
     *
     * @param phones    手机号
     * @param tClass    短信模板业务
     * @param parameter 短信参数
     */
    void send(Set<String> phones, Class<? extends SmsTemplateExtension> tClass, Map<String, String> parameter);

    /**
     * 向手机发送指定短信
     *
     * @param phones    手机号
     * @param tClass    短信模板业务
     * @param parameter 短信参数
     */
    void send(Set<String> phones, Class<? extends SmsTemplateExtension> tClass, Consumer<Map<String, String>> parameter);

    /**
     * 向手机发送指定短信
     *
     * @param phone     手机号
     * @param tClass    短信模板业务
     * @param parameter 短信参数
     */
    void send(String phone, Class<? extends SmsTemplateExtension> tClass, Map<String, String> parameter);

    /**
     * 向手机发送指定短信
     *
     * @param phone     手机号
     * @param tClass    短信模板业务
     * @param parameter 短信参数
     */
    void send(String phone, Class<? extends SmsTemplateExtension> tClass, Consumer<Map<String, String>> parameter);

    /**
     * 向用户发送指定短信
     *
     * @param users     用户
     * @param tClass    短信模板业务
     * @param parameter 短信参数
     */
    void sendToUsers(Set<User> users, Class<? extends SmsTemplateExtension> tClass, Map<String, String> parameter);

    /**
     * 向用户发送指定短信
     *
     * @param users     用户
     * @param tClass    短信模板业务
     * @param parameter 短信参数
     */
    void sendToUsers(Set<User> users, Class<? extends SmsTemplateExtension> tClass, Consumer<Map<String, String>> parameter);

    /**
     * 向用户发送指定短信
     *
     * @param user      用户
     * @param tClass    短信模板业务
     * @param parameter 短信参数
     */
    void sendToUser(User user, Class<? extends SmsTemplateExtension> tClass, Map<String, String> parameter);

    /**
     * 向用户发送指定短信
     *
     * @param user      用户
     * @param tClass    短信模板业务
     * @param parameter 短信参数
     */
    void sendToUser(User user, Class<? extends SmsTemplateExtension> tClass, Consumer<Map<String, String>> parameter);

    /**
     * 发送短信验证码到指定手机
     * <p>
     * 平台需要至少一个验证码短信业务扩展 {@link SmsTemplateExtension}
     *
     * @param phone   手机号
     * @param captcha 验证码
     */
    void sendCaptcha(String phone, String captcha);

    /**
     * 配置短信服务
     * <p>
     * 从系统配置里面取出相应短信服务参数进行配置
     */
    void config();
}
