package com.qxcmp.message;

import com.aliyun.mns.common.ServiceException;

import java.util.List;
import java.util.Map;
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
     * 发送短信到指定手机号码
     *
     * @param phones       手机号码列表
     * @param templateCode 短信模板Code
     * @param parameters   短信模板用到的参数
     *
     * @throws ServiceException 如果发送短信失败，则抛出该异常
     */
    void send(List<String> phones, String templateCode, Consumer<Map<String, String>> parameters) throws ServiceException;

    /**
     * 发送短信验证码到指定手机
     *
     * @param phone   手机号
     * @param captcha 验证码
     *
     * @throws ServiceException 如果发送短信失败，则抛出该异常
     */
    void sendCaptcha(String phone, String captcha) throws ServiceException;

    /**
     * 配置短信服务
     * <p>
     * 从系统配置里面取出相应短信服务参数进行配置
     */
    void config();
}
