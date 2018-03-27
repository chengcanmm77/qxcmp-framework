package com.qxcmp.message;

import com.qxcmp.core.extension.Extension;

/**
 * 短信业务扩展
 * <p>
 * 定义一个短信发送业务
 *
 * @author Aaric
 */
public interface SmsTemplateExtension extends Extension {

    /**
     * 获取业务名称
     * <p>
     * 业务应该唯一
     *
     * @return 业务名称
     */
    String getName();

    /**
     * 获取业务的短信模板
     *
     * @return 短信模板
     */
    String getTemplateCode();

    /**
     * 获取短信模板内容
     *
     * @return 短信模板内容
     */
    String getContent();

    /**
     * 获取业务的短信签名
     * <p>
     * 如未填写则使用默认短信签名 {@link com.qxcmp.core.QxcmpSystemConfig#MESSAGE_SMS_SIGN_NAME}
     *
     * @return 短信签名
     */
    default String getSignName() {
        return "";
    }

    /**
     * 是否为验证码业务
     *
     * @return 是否为验证码业务
     */
    default boolean isCaptcha() {
        return false;
    }
}
