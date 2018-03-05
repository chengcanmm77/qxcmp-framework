package com.qxcmp.web.view.modules.sidebar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccordionMenuSidebar extends MenuSidebar {

    /**
     * 是否一次只打开一个区块
     */
    private boolean exclusive;

    @Override
    public String getFragmentName() {
        return "sidebar-accordion";
    }

    @Override
    public String getClassSuffix() {
        return "accordion " + super.getClassSuffix();
    }
}
