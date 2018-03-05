package com.qxcmp.web.view.elements.breadcrumb;

import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.AnchorTarget;
import lombok.Getter;

/**
 * 面包屑项目
 *
 * @author Aaric
 */
@Getter
public class BreadcrumbItem extends AbstractComponent {

    /**
     * 文本
     */
    private String text;

    /**
     * 超链接
     */
    private String url;

    /**
     * 超链接打开方式
     */
    private String urlTarget;

    /**
     * 是否为激活状态
     */
    private boolean active;

    public BreadcrumbItem(String text) {
        this.text = text;
    }

    public BreadcrumbItem(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public BreadcrumbItem(String text, String url, AnchorTarget target) {
        this.text = text;
        this.url = url;
        this.urlTarget = target.toString();
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/breadcrumb";
    }

    @Override
    public String getFragmentName() {
        return "item";
    }

    @Override
    public String getClassContent() {
        return active ? "active" : "";
    }

    @Override
    public String getClassSuffix() {
        return "section";
    }

    public BreadcrumbItem setActive() {
        this.active = true;
        return this;
    }
}
