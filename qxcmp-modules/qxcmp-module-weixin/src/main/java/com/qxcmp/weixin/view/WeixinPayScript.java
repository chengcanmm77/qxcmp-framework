package com.qxcmp.weixin.view;

import com.qxcmp.finance.DepositOrder;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;

import java.util.Map;

import static com.qxcmp.weixin.WeixinModule.WEIXIN_FRAGMENT;

/**
 * @author Aaric
 */
@Getter
public class WeixinPayScript extends AbstractComponent {

    private final Map<String, String> payInfo;
    private final DepositOrder depositOrder;

    public WeixinPayScript(Map<String, String> payInfo, DepositOrder depositOrder) {
        this.payInfo = payInfo;
        this.depositOrder = depositOrder;
    }

    @Override
    public String getFragmentFile() {
        return WEIXIN_FRAGMENT;
    }

    @Override
    public String getFragmentName() {
        return "weixin-pay-script";
    }
}
