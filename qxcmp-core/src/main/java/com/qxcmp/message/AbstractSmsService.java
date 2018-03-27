package com.qxcmp.message;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.qxcmp.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Aaric
 */
public abstract class AbstractSmsService implements SmsService {

    private SmsTemplateExtensionPoint smsTemplateExtensionPoint;

    @Override
    public void send(Set<String> phones, Class<? extends SmsTemplateExtension> tClass, Consumer<Map<String, String>> parameter) {
        Map<String, String> stringStringMap = Maps.newConcurrentMap();
        parameter.accept(stringStringMap);
        send(phones, tClass, stringStringMap);
    }

    @Override
    public void send(String phone, Class<? extends SmsTemplateExtension> tClass, Map<String, String> parameter) {
        send(ImmutableSet.of(phone), tClass, parameter);
    }

    @Override
    public void send(String phone, Class<? extends SmsTemplateExtension> tClass, Consumer<Map<String, String>> parameter) {
        send(ImmutableSet.of(phone), tClass, parameter);
    }

    @Override
    public void sendToUsers(Set<User> users, Class<? extends SmsTemplateExtension> tClass, Map<String, String> parameter) {
        send(users.stream().filter(user -> StringUtils.isNotBlank(user.getPhone())).map(User::getPhone).collect(Collectors.toSet()), tClass, parameter);
    }

    @Override
    public void sendToUsers(Set<User> users, Class<? extends SmsTemplateExtension> tClass, Consumer<Map<String, String>> parameter) {
        Map<String, String> stringStringMap = Maps.newConcurrentMap();
        parameter.accept(stringStringMap);
        sendToUsers(users, tClass, stringStringMap);
    }

    @Override
    public void sendToUser(User user, Class<? extends SmsTemplateExtension> tClass, Map<String, String> parameter) {
        sendToUsers(ImmutableSet.of(user), tClass, parameter);
    }

    @Override
    public void sendToUser(User user, Class<? extends SmsTemplateExtension> tClass, Consumer<Map<String, String>> parameter) {
        sendToUsers(ImmutableSet.of(user), tClass, parameter);
    }

    @Override
    public void sendCaptcha(String phone, String captcha) {
        send(phone, smsTemplateExtensionPoint.getCaptchaTemplate().getClass(), stringStringMap -> stringStringMap.put(CAPTCHA_PARAMETER, captcha));
    }

    @Autowired
    public void setSmsTemplateExtensionPoint(SmsTemplateExtensionPoint smsTemplateExtensionPoint) {
        this.smsTemplateExtensionPoint = smsTemplateExtensionPoint;
    }

    /**
     * 获取对应的扩展对象
     *
     * @param tClass 短信业务类型
     *
     * @return 对应的扩展对象
     */
    protected SmsTemplateExtension getTemplateExtension(Class<? extends SmsTemplateExtension> tClass) {
        return smsTemplateExtensionPoint.getExtensions().stream().filter(extension -> extension.getClass().equals(tClass)).findAny().orElseThrow(() -> new RuntimeException("Can't find SMS template extension: " + tClass.getName()));
    }
}
