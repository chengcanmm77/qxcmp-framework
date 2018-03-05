package com.qxcmp.web.view.modules.sidebar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuSidebar extends Sidebar {

    @Override
    public String getClassContent() {
        return super.getClassContent() + " inverted";
    }

    @Override
    public String getClassSuffix() {
        return "vertical menu " + super.getClassSuffix();
    }
}
