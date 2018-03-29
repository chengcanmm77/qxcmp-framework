package com.qxcmp.message;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.qxcmp.config.SystemConfigChangeEvent;
import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.init.QxcmpInitializer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.qxcmp.core.QxcmpSystemConfig.*;

/**
 * @author Aaric
 */
@Service
@RequiredArgsConstructor
public class SmsAliyunService extends AbstractSmsService implements QxcmpInitializer {

    public static final String ALIYUM_SMS_CODE_SUCCSEE = "OK";
    private static final String ALIYUN_SMS_PRODUCT = "Dysmsapi";
    private static final String ALIYUN_SMS_DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String ALIYUN_SMS_REGION = "cn-hangzhou";

    private final SystemConfigService systemConfigService;
    private final SmsSendRecordService smsSendRecordService;

    private IAcsClient client;
    private String defaultSignName;

    @Override
    public void send(Set<String> phones, Class<? extends SmsTemplateExtension> tClass, Object parameter) {
        SmsTemplateExtension templateExtension = getTemplateExtension(tClass);
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(StringUtils.join(phones, ","));
        request.setSignName(StringUtils.isNotBlank(templateExtension.getSignName()) ? templateExtension.getSignName() : defaultSignName);
        request.setTemplateCode(templateExtension.getTemplateCode());
        request.setTemplateParam(new Gson().toJson(parameter));
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            createSendRecord(request, response, templateExtension);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void config() {
        defaultSignName = systemConfigService.getString(MESSAGE_SMS_SIGN_NAME).orElse("");
        try {
            DefaultProfile profile = DefaultProfile.getProfile(ALIYUN_SMS_REGION, systemConfigService.getString(MESSAGE_SMS_ACCESS_KEY).orElse(""), systemConfigService.getString(MESSAGE_SMS_ACCESS_SECRET).orElse(""));
            DefaultProfile.addEndpoint(ALIYUN_SMS_REGION, ALIYUN_SMS_REGION, ALIYUN_SMS_PRODUCT, ALIYUN_SMS_DOMAIN);
            client = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @EventListener
    public void onSystemConfigChange(SystemConfigChangeEvent event) {
        if (StringUtils.startsWith(event.getName(), "qxcmp.message.sms")) {
            config();
        }
    }

    @Override
    public void init() {
        config();
    }

    /**
     * 保存发送记录
     *
     * @param request           发送请求
     * @param response          发送结果
     * @param templateExtension 短信业务模板
     */
    private void createSendRecord(SendSmsRequest request, SendSmsResponse response, SmsTemplateExtension templateExtension) {
        SmsSendRecord next = smsSendRecordService.next();
        next.setPhones(request.getPhoneNumbers());
        next.setSignName(request.getSignName());
        next.setTemplateCode(request.getTemplateCode());
        next.setTemplateParameter(request.getTemplateParam());
        next.setId(response.getRequestId());
        next.setCode(response.getCode());
        next.setMessage(response.getMessage());
        next.setDateCreated(DateTime.now().toDate());
        next.setTemplateName(templateExtension.getName());
        next.setContent(templateExtension.getContent());
        smsSendRecordService.create(next);
    }

}
