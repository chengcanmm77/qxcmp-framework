package com.qxcmp.web.view.views;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileListItem extends AbstractComponent {

    /**
     * 左侧文本
     */
    private String title;

    /**
     * 右侧文本 - 可选
     */
    private String subTitle;

    /**
     * 超链接
     */
    private String url;

    /**
     * 图片 - 可选
     */
    private Image image;

    /**
     * 图标 - 可选
     */
    private Icon icon;

    /**
     * 自定义内容
     */
    private Component component;

    public MobileListItem(Component component, String url) {
        this.url = url;
        this.component = component;
    }

    public MobileListItem(Component component, String url, String subTitle) {
        this.url = url;
        this.subTitle = subTitle;
        this.component = component;
    }


    public MobileListItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public MobileListItem(String title, String subTitle, String url) {
        this.title = title;
        this.subTitle = subTitle;
        this.url = url;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/views/mobile-list";
    }

    @Override
    public String getFragmentName() {
        return "item";
    }

    @Override
    public String getClassSuffix() {
        return "item";
    }

    public MobileListItem setImage(Image image) {
        this.image = image;
        return this;
    }

    public MobileListItem setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public MobileListItem setIcon(String icon) {
        this.icon = new Icon(icon);
        return this;
    }
}
