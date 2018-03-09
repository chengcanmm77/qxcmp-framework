package com.qxcmp.admin;

import com.qxcmp.core.module.ModuleLoaderAdapter;
import com.qxcmp.core.navigation.NavigationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 后台基础模块
 *
 * @author Aaric
 */
@Slf4j
@Component
public class QxcmpAdminModule extends ModuleLoaderAdapter {

    public static final String ADMIN_URL = "/admin";
    public static final String ADMIN_AUDIT_LOG_URL = ADMIN_URL + "/audit";

    public static final String PRIVILEGE_SYSTEM_ADMIN = "系统管理员权限";
    public static final String PRIVILEGE_SYSTEM_ADMIN_DESCRIPTION = "可以进入后台系统";

    @Override
    public void configNavigation(NavigationService navigationService) {
        log.info("Load Admin Module Navigation");
    }
}
