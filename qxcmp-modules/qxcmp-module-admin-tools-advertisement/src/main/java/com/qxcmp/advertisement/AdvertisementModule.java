package com.qxcmp.advertisement;

import com.google.common.collect.ImmutableList;
import com.qxcmp.core.module.ModuleLoaderAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * 广告模块
 *
 * @author Aaric
 */
@Component
public class AdvertisementModule extends ModuleLoaderAdapter {

    public static final String ADMIN_ADVERTISEMENT_URL = ADMIN_URL + "/advertisement";
    public static final List<String> SUPPORT_TYPES = ImmutableList.of("横幅", "弹框", "摩天楼");

    public static final String PRIVILEGE_ADMIN_ADVERTISEMENT = "广告管理权限";
    public static final String PRIVILEGE_ADMIN_ADVERTISEMENT_DESCRIPTION = "可以管理平台广告";
}
