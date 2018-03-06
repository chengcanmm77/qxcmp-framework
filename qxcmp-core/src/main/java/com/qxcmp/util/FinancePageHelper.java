package com.qxcmp.util;

import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.components.finance.DepositComponent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class FinancePageHelper {
    public AbstractComponent nextDepositComponent(SystemConfigService systemConfigService) {


        boolean supportWeixin = systemConfigService.getBoolean(QxcmpSystemConfig.FINANCE_PAYMENT_SUPPORT_WEIXIN).orElse(QxcmpSystemConfig.FINANCE_PAYMENT_SUPPORT_WEIXIN_DEFAULT);
        boolean supportAlipay = systemConfigService.getBoolean(QxcmpSystemConfig.FINANCE_PAYMENT_SUPPORT_ALIPAY).orElse(QxcmpSystemConfig.FINANCE_PAYMENT_SUPPORT_ALIPAY_DEFAULT);

        String weixinTradeType = systemConfigService.getString(QxcmpSystemConfig.WECHAT_PAYMENT_DEFAULT_TRADE_TYPE).orElse(QxcmpSystemConfig.WECHAT_PAYMENT_DEFAULT_TRADE_TYPE_DEFAULT);
        String weixinActionUrl = "";

        if (StringUtils.equals(weixinTradeType, "NATIVE")) {
            weixinActionUrl = "/api/wxmp-cgi/pay/native";
        } else if (StringUtils.equals(weixinTradeType, "JSAPI")) {
            weixinActionUrl = "/api/wxmp-cgi/pay/mp";
        }

        return new DepositComponent(supportWeixin, supportAlipay, weixinActionUrl);
    }
}
