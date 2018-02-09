package com.qxcmp.core.extension;

import org.springframework.stereotype.Component;

/**
 * @author Aaric
 */
@Component
public class AdminUserDetailsPageToolbarStatusExtension implements AdminUserDetailsPageToolbarExtension {

    @Override
    public String getTitle() {
        return "编辑状态";
    }

    @Override
    public String getSuffix() {
        return "status";
    }
}
