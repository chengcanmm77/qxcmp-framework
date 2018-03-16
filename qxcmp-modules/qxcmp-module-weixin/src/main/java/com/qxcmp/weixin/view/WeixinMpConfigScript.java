package com.qxcmp.weixin.view;

import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;

import java.util.List;

import static com.qxcmp.weixin.WeixinModule.WEIXIN_FRAGMENT;

/**
 * @author Aaric
 */
@Getter
public class WeixinMpConfigScript extends AbstractComponent {

    private final List<String> apiList;

    public WeixinMpConfigScript(List<String> apiList) {
        this.apiList = apiList;
    }

    @Override
    public String getFragmentFile() {
        return WEIXIN_FRAGMENT;
    }

    @Override
    public String getFragmentName() {
        return "config";
    }
}
