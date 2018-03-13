package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.security.PrivilegeService;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SECURITY_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY_PRIVILEGE;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminSecurityPrivilegeTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final PrivilegeService privilegeService;
    private final Pageable pageable;

    @Override
    protected EntityTable renderEntityTable() {
        setMenu(ADMIN_MENU_SECURITY, ADMIN_MENU_SECURITY_PRIVILEGE);
        return viewHelper.nextEntityTable(pageable, privilegeService, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("安全配置", ADMIN_SECURITY_URL, "权限管理");
    }
}
