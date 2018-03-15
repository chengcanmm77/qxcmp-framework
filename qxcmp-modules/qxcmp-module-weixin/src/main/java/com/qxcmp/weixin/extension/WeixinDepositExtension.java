package com.qxcmp.weixin.extension;

import com.qxcmp.config.SystemConfigService;
import com.qxcmp.finance.DepositExtension;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.qxcmp.weixin.WeixinModuleSystemConfig.PAYMENT_ENABLE;
import static com.qxcmp.weixin.WeixinModuleSystemConfig.PAYMENT_ENABLE_DEFAULT;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class WeixinDepositExtension implements DepositExtension {

    private final SystemConfigService systemConfigService;

    @Override
    public boolean isEnabled() {
        return systemConfigService.getBoolean(PAYMENT_ENABLE).orElse(PAYMENT_ENABLE_DEFAULT);
    }

    @Override
    public String getActionImage() {
        return "/assets/weixin/weixinpay-logo.png";
    }

    @Override
    public String getActionTitle() {
        return "微信支付 | 微信安全支付";
    }

    @Override
    public String getActionUrl() {
        return "/api/wxmp-cgi/pay/mp";
    }
}
