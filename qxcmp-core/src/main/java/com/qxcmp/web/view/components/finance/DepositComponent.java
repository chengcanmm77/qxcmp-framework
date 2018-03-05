package com.qxcmp.web.view.components.finance;

import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositComponent extends AbstractComponent {

    private boolean supportWeixin;

    private boolean supportAlipay;

    private String weixinActionUrl;

    public DepositComponent(boolean supportWeixin, boolean supportAlipay, String weixinActionUrl) {
        this.supportWeixin = supportWeixin;
        this.supportAlipay = supportAlipay;
        this.weixinActionUrl = weixinActionUrl;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/components/finance/deposit";
    }

}
