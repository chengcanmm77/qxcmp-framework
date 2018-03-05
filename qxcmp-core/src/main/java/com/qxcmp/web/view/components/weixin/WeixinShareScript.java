package com.qxcmp.web.view.components.weixin;

import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class WeixinShareScript extends AbstractComponent {

    private String title;
    private String link;
    private String imgUrl;
    private String desc;

    public WeixinShareScript(String title, String link, String imgUrl, String desc) {
        this.title = title;
        this.link = link;
        this.imgUrl = imgUrl;
        this.desc = desc;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/components/weixin/weixin-mp";
    }

    @Override
    public String getFragmentName() {
        return "weixin-share-script";
    }
}
