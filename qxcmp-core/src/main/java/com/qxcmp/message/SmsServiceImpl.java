package com.qxcmp.message;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.qxcmp.config.SystemConfigChangeEvent;
import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Aaric
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    private SystemConfigService systemConfigService;

    private String accessKey;

    private String accessSecret;

    private String endPoint;

    private String topicRef;

    private String sign;

    private String captchaTemplateCode;

    public SmsServiceImpl(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
        config();
    }

    @Override
    public void send(List<String> phones, String templateCode, Consumer<Map<String, String>> consumer) throws ServiceException {

        Map<String, String> parameters = Maps.newLinkedHashMap();
        consumer.accept(parameters);

        CloudAccount cloudAccount = new CloudAccount(accessKey, accessSecret, endPoint);
        CloudTopic topic = cloudAccount.getMNSClient().getTopicRef(topicRef);

        RawTopicMessage topicMessage = new RawTopicMessage();
        topicMessage.setMessageBody("清醒内容管理平台");

        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();

        batchSmsAttributes.setFreeSignName(sign);
        batchSmsAttributes.setTemplateCode(templateCode);

        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        parameters.forEach(smsReceiverParams::setParam);

        phones.forEach(phone -> batchSmsAttributes.addSmsReceiver(phone, smsReceiverParams));

        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);

        topic.publishMessage(topicMessage, messageAttributes);
    }

    @Override
    public void sendCaptcha(String phone, String captcha) throws ServiceException {
        send(ImmutableList.of(phone), captchaTemplateCode, stringStringMap -> stringStringMap.put("captcha", captcha));
    }

    @Override
    public void config() {
        log.info("Loading sms service");
        accessKey = systemConfigService.getString(QxcmpSystemConfig.MESSAGE_SMS_ACCESS_KEY).orElse("");
        accessSecret = systemConfigService.getString(QxcmpSystemConfig.MESSAGE_SMS_ACCESS_SECRET).orElse("");
        endPoint = systemConfigService.getString(QxcmpSystemConfig.MESSAGE_SMS_END_POINT).orElse("");
        topicRef = systemConfigService.getString(QxcmpSystemConfig.MESSAGE_SMS_TOPIC_REF).orElse("");
        sign = systemConfigService.getString(QxcmpSystemConfig.MESSAGE_SMS_SIGN).orElse("");
        captchaTemplateCode = systemConfigService.getString(QxcmpSystemConfig.MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE).orElse("");
    }

    @EventListener
    public void onSystemConfigChange(SystemConfigChangeEvent event) {
        if (StringUtils.startsWith(event.getName(), "qxcmp.message.sms")) {
            config();
        }
    }
}
