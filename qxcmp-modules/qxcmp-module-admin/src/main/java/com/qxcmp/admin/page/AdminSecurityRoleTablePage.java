package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.security.RoleService;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SECURITY_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_ROLE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminSecurityRoleTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final RoleService roleService;
    private final Pageable pageable;

    @Override
    protected EntityTable renderEntityTable() {
        setMenu(ADMIN_MENU_SECURITY, ADMIN_MENU_SECURITY_ROLE);
        return viewHelper.nextEntityTable(pageable, roleService, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("安全配置", ADMIN_SECURITY_URL, "角色管理");
    }
}
