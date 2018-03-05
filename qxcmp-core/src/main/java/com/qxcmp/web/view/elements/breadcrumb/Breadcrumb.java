package com.qxcmp.web.view.elements.breadcrumb;

import lombok.Getter;

/**
 * 使用文本作为分隔符的面包屑
 *
 * @author Aaric
 */
@Getter
public class Breadcrumb extends AbstractBreadcrumb {

    /**
     * 分隔符文本
     */
    private String separator = "/";

    public Breadcrumb() {
    }

    public Breadcrumb(String separator) {
        this.separator = separator;
    }
}
