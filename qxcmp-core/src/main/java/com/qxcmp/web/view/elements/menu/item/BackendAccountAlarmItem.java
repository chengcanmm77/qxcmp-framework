package com.qxcmp.web.view.elements.menu.item;

import lombok.Getter;

/**
 * 后台顶部菜单专用菜单项
 *
 * @author Aaric
 */
@Getter
public class BackendAccountAlarmItem extends AbstractMenuItem {

    private long messageCount;

    public BackendAccountAlarmItem(long messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public String getFragmentName() {
        return "item-backend-alarm";
    }
}
