package com.qxcmp.admin;

import com.qxcmp.core.module.ModuleLoaderAdapter;
import com.qxcmp.core.navigation.NavigationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 平台后台基础模块
 *
 * @author Aaric
 */
@Slf4j
@Component
public class QxcmpAdminModule extends ModuleLoaderAdapter {
    @Override
    public void configNavigation(NavigationService navigationService) {
        log.info("Load Admin Module Navigation");
    }
}
