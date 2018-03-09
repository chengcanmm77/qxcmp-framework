package com.qxcmp.link;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * @author Aaric
 */
public class LinkModule {

    public static final String ADMIN_LINK_URL = ADMIN_URL + "/link";
    public static final List<String> SUPPORT_TYPE = ImmutableList.of("友情链接", "热门标签");
    public static final List<String> SUPPORT_TARGET = ImmutableList.of("当前窗口打开", "新窗口打开");
}
