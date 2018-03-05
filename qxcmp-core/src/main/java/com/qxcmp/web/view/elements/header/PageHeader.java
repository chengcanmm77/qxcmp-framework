package com.qxcmp.web.view.elements.header;

import lombok.Getter;
import lombok.Setter;

/**
 * 页面抬头
 * <p>
 * 页面抬头用来表述页面层级
 * <p>
 * 页面抬头使用 rem 作为大小，不受上下文大小的影响
 *
 * @author Aaric
 */
@Getter
@Setter
public class PageHeader extends AbstractHeader {

    private HeaderType type;

    public PageHeader(HeaderType type, String title) {
        super(title);
        this.type = type;
    }

    @Override
    public String getFragmentName() {
        return "page-header";
    }
}
