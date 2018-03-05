package com.qxcmp.web.view.elements.list.item;

import com.qxcmp.web.view.elements.html.Div;
import com.qxcmp.web.view.elements.icon.Icon;
import lombok.Getter;
import lombok.Setter;

/**
 * 图标项目
 *
 * @author aaric
 */
@Getter
@Setter
public class IconHeaderItem extends AbstractListItem {

    /**
     * 图标
     */
    private Icon icon;

    /**
     * 标题
     */
    private Div header;

    /**
     * 描述
     */
    private Div description;

    public IconHeaderItem(Icon icon, Div header) {
        this.icon = icon;
        this.header = header;
        this.header.setClassName("header");
    }

    public IconHeaderItem(Icon icon, Div header, Div description) {
        this.icon = icon;
        this.header = header;
        this.description = description;
        this.header.setClassName("header");
        this.description.setClassName("description");
    }

    public IconHeaderItem(Icon icon, String header) {
        this.icon = icon;
        this.header = new Div(header);
        this.header.setClassName("header");
    }

    public IconHeaderItem(Icon icon, String header, String description) {
        this.icon = icon;
        this.header = new Div(header);
        this.description = new Div(description);
        this.header.setClassName("header");
        this.description.setClassName("description");
    }

    @Override
    public String getFragmentName() {
        return "item-icon-header";
    }
}
