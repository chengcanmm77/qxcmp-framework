package com.qxcmp.core;

import com.qxcmp.security.SecurityLoader;
import org.springframework.context.annotation.Configuration;


/**
 * 平台权限配置
 *
 * @author Aaric
 */
@Configuration
public class QxcmpSecurityConfiguration implements SecurityLoader {

    /*
     * 用户管理模块
     * */

    public static final String PRIVILEGE_USER = "用户管理权限";
    public static final String PRIVILEGE_USER_DESCRIPTION = "可以查看用户管理入口页面";
    public static final String PRIVILEGE_USER_ROLE = "用户角色管理权限";
    public static final String PRIVILEGE_USER_ROLE_DESCRIPTION = "可以修改用户角色";
    public static final String PRIVILEGE_USER_STATUS = "用户状态管理权限";
    public static final String PRIVILEGE_USER_STATUS_DESCRIPTION = "可以修改用户状态";

    /*
     * 财务模块权限
     * */

    /*
     * 新闻模块权限
     * */

    /*
     * 商城模块权限
     * */

    public static final String PRIVILEGE_MALL = "商城管理权限";
    public static final String PRIVILEGE_MALL_DESCRIPTION = "可以查看商城管理入口页面";
    public static final String PRIVILEGE_MALL_ORDER = "商城订单管理权限";
    public static final String PRIVILEGE_MALL_ORDER_DESCRIPTION = "可以管理商城所有订单";
    public static final String PRIVILEGE_MALL_COMMODITY = "商城商品管理权限";
    public static final String PRIVILEGE_MALL_COMMODITY_DESCRIPTION = "可以管理商城所有商品";
    public static final String PRIVILEGE_MALL_STORE = "商城店铺管理权限";
    public static final String PRIVILEGE_MALL_STORE_DESCRIPTION = "可以管理商城店铺，店铺所有者和管理员能够管理自己店铺的商品和订单";
    public static final String PRIVILEGE_MALL_SETTINGS = "商城配置管理权限";
    public static final String PRIVILEGE_MALL_SETTINGS_DESCRIPTION = "可以管理商城配置";

    /*
     * 网站统计管理权限
     * */

    public static final String PRIVILEGE_STATISTIC = "网站统计管理权限";
    public static final String PRIVILEGE_STATISTIC_DESCRIPTION = "网站统计管理权限入口页面";
    public static final String PRIVILEGE_STATISTIC_SETTINGS = "网站统计配置管理权限";
    public static final String PRIVILEGE_STATISTIC_SETTINGS_DESCRIPTION = "可以修改网站统计配置";


}
