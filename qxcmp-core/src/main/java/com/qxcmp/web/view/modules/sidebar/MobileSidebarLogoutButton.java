package com.qxcmp.web.view.modules.sidebar;

import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aaric
 */
@Getter
@Setter
public class MobileSidebarLogoutButton extends AbstractComponent {
    @Override
    public String getFragmentFile() {
        return "qxcmp/modules/sidebar";
    }

    @Override
    public String getFragmentName() {
        return "logout-button";
    }
}
