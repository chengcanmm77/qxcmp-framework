package com.qxcmp.web.view.elements.breadcrumb;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * 面包屑组件
 *
 * @author Aaric
 */
@Getter
@Setter
public abstract class AbstractBreadcrumb extends AbstractComponent {

    /**
     * 大小
     */
    private Size size = Size.NONE;

    private List<BreadcrumbItem> items = Lists.newArrayList();

    public AbstractBreadcrumb addItem(BreadcrumbItem item) {
        items.add(item);
        return this;
    }

    public AbstractBreadcrumb addItems(Collection<? extends BreadcrumbItem> items) {
        this.items.addAll(items);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/breadcrumb";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return size.toString();
    }

    @Override
    public String getClassSuffix() {
        return "breadcrumb";
    }

    public AbstractBreadcrumb setSize(Size size) {
        this.size = size;
        return this;
    }
}
