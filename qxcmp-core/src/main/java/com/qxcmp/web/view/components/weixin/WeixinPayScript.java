package com.qxcmp.web.view.components.weixin;

import com.qxcmp.finance.DepositOrder;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;

import java.util.Map;


public class WeixinPayScript extends AbstractComponent {

    @Getter
    private Map<String, String> payInfo;

    @Getter
    private DepositOrder depositOrder;

    public WeixinPayScript(Map<String, String> payInfo, DepositOrder depositOrder) {
        this.payInfo = payInfo;
        this.depositOrder = depositOrder;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/components/weixin/weixin-mp";
    }

    @Override
    public String getFragmentName() {
        return "weixin-pay-script";
    }
}
