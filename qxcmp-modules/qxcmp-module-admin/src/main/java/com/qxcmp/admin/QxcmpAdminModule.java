package com.qxcmp.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 后台基础模块
 *
 * @author Aaric
 */
@Slf4j
@Component
public class QxcmpAdminModule {

    /*
     * 全局常量
     * */

    public static final String ADMIN_URL = "/admin";
    public static final String ADMIN_AUDIT_LOG_URL = ADMIN_URL + "/audit";

}
