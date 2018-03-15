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
     * 基本权限
     * */

    /*
     * 系统工具权限
     * */

    public static final String PRIVILEGE_ADMIN_SPIDER = "蜘蛛管理权限";
    public static final String PRIVILEGE_ADMIN_SPIDER_DESCRIPTION = "可以管理平台蜘蛛";

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

    public static final String PRIVILEGE_FINANCE = "财务管理权限";
    public static final String PRIVILEGE_FINANCE_DESCRIPTION = "可以查看财务管理入口页面";
    public static final String PRIVILEGE_FINANCE_DEPOSIT = "充值订单管理权限";
    public static final String PRIVILEGE_FINANCE_DEPOSIT_DESCRIPTION = "可以查看充值订单页面";
    public static final String PRIVILEGE_FINANCE_WALLET_MANAGEMENT = "用户钱包管理权限";
    public static final String PRIVILEGE_FINANCE_WALLET_MANAGEMENT_DESCRIPTION = "可以修改用户钱包";

    /*
     * 新闻模块权限
     * */

    public static final String PRIVILEGE_NEWS = "新闻管理权限";
    public static final String PRIVILEGE_NEWS_DESCRIPTION = "可以查看新闻管理入口页面";
    public static final String PRIVILEGE_NEWS_CHANNEL = "栏目管理权限";
    public static final String PRIVILEGE_NEWS_CHANNEL_DESCRIPTION = "可以对栏目进行管理";
    public static final String PRIVILEGE_NEWS_ARTICLE_AUDIT = "文章审核权限";
    public static final String PRIVILEGE_NEWS_ARTICLE_AUDIT_DESCRIPTION = "可以对平台申请审核的文章进行审核，决定发布或驳回";
    public static final String PRIVILEGE_NEWS_ARTICLE_MANAGEMENT = "文章管理权限";
    public static final String PRIVILEGE_NEWS_ARTICLE_MANAGEMENT_DESCRIPTION = "可以对平台已发布文章进行管理，禁用或者删除";

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
