package com.qxcmp.message;

import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestSmsTemplate implements SmsTemplateExtension {
    @Override
    public String getSignName() {
        return "挚友旅游";
    }

    @Override
    public String getTemplateCode() {
        return "78325270";
    }

    @Override
    public Set<String> getParameters() {
        return ImmutableSet.of("captcha");
    }
}
