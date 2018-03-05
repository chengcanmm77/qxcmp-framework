package com.qxcmp.web.view.elements.breadcrumb;

import lombok.Getter;

/**
 * 使用图标作为分隔符的面包屑
 *
 * @author Aaric
 */
@Getter
public class IconBreadCrumb extends AbstractBreadcrumb {

    private String iconName = "right chevron";

    public IconBreadCrumb() {
    }

    public IconBreadCrumb(String iconName) {
        this.iconName = iconName;
    }

    @Override
    public String getFragmentName() {
        return "icon";
    }
}
