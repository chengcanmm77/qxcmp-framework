package com.qxcmp.advertisement;

import com.qxcmp.core.module.ModuleLoaderAdapter;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * 广告模块
 *
 * @author Aaric
 */
@Component
public class AdvertisementModule extends ModuleLoaderAdapter {

    public static final String ADMIN_ADVERTISEMENT_URL = ADMIN_URL + "/advertisement";

    public static final String PRIVILEGE_ADMIN_ADVERTISEMENT = "广告管理权限";
    public static final String PRIVILEGE_ADMIN_ADVERTISEMENT_DESCRIPTION = "可以管理平台广告";
}
