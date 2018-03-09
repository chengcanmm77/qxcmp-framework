package com.qxcmp.admin.view;

import com.qxcmp.web.view.elements.menu.item.AbstractMenuItem;
import lombok.Getter;

/**
 * 后台顶部菜单专用菜单项
 *
 * @author Aaric
 */
@Getter
public class AdminTopMenuAlarmItem extends AbstractMenuItem {

    private long messageCount;

    public AdminTopMenuAlarmItem(long messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public String getFragmentName() {
        return "item-alarm";
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/admin/menu";
    }
}
