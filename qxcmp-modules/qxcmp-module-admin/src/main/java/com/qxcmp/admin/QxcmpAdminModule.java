package com.qxcmp.admin;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * 后台模块全局配置
 *
 * @author Aaric
 */
public class QxcmpAdminModule {

    public static final String ADMIN_URL = "/admin";
    public static final String ADMIN_AUDIT_LOG_URL = ADMIN_URL + "/audit";
    public static final String ADMIN_TOOLS_URL = ADMIN_URL + "/tools";
    public static final String ADMIN_SETTINGS_URL = ADMIN_URL + "/settings";
    public static final String ADMIN_SETTINGS_SITE_URL = ADMIN_SETTINGS_URL + "/site";
    public static final String ADMIN_SETTINGS_DICTIONARY_URL = ADMIN_SETTINGS_URL + "/dictionary";

    public static final List<String> WATERMARK_POSITIONS = ImmutableList.of("左上", "中上", "右上", "左中", "居中", "右中", "左下", "中下", "右下");
}
