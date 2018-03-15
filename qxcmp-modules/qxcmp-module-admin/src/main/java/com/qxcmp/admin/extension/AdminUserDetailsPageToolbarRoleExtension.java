package com.qxcmp.admin.extension;

import org.springframework.stereotype.Component;

/**
 * @author Aaric
 */
@Component
public class AdminUserDetailsPageToolbarRoleExtension implements AdminUserDetailsPageToolbarExtension {
    @Override
    public String getTitle() {
        return "编辑角色";
    }

    @Override
    public String getSuffix() {
        return "role";
    }
}
