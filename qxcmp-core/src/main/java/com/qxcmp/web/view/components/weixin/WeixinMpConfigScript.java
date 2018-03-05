package com.qxcmp.web.view.components.weixin;

import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;

import java.util.List;

public class WeixinMpConfigScript extends AbstractComponent {

    @Getter
    private List<String> apiList;

    public WeixinMpConfigScript(List<String> apiList) {
        this.apiList = apiList;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/components/weixin/weixin-mp";
    }

    @Override
    public String getFragmentName() {
        return "config";
    }
}
