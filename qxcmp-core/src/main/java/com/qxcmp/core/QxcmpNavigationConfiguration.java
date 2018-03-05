package com.qxcmp.core;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.web.model.navigation.Navigation;
import com.qxcmp.web.model.navigation.NavigationConfigurator;
import com.qxcmp.web.model.navigation.NavigationService;
import com.qxcmp.web.view.elements.icon.Icon;
import org.springframework.context.annotation.Configuration;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpSecurityConfiguration.*;

/**
 * 平台导航配置
 *
 * @author Aaric
 */
@Configuration
public class QxcmpNavigationConfiguration implements NavigationConfigurator {

    /*
     * 移动端导航栏扩展
     * */

    public static final String NAVIGATION_GLOBAL_MOBILE_TOP = "GLOBAL-MOBILE-TOP";
    public static final String NAVIGATION_GLOBAL_MOBILE_BOTTOM = "GLOBAL-MOBILE-BOTTOM";
    public static final String NAVIGATION_GLOBAL_MOBILE_SIDEBAR = "GLOBAL-MOBILE-SIDEBAR";

    /*
     * 侧边导航栏
     * */

    public static final String NAVIGATION_ADMIN_SIDEBAR = "ADMIN-SIDEBAR";
    public static final String NAVIGATION_ADMIN_SIDEBAR_USER = NAVIGATION_ADMIN_SIDEBAR + "-USER";
    public static final String NAVIGATION_ADMIN_SIDEBAR_NEWS = NAVIGATION_ADMIN_SIDEBAR + "-NEWS";
    public static final String NAVIGATION_ADMIN_SIDEBAR_MALL = NAVIGATION_ADMIN_SIDEBAR + "-MALL";
    public static final String NAVIGATION_ADMIN_SIDEBAR_MESSAGE = NAVIGATION_ADMIN_SIDEBAR + "-MESSAGE";
    public static final String NAVIGATION_ADMIN_SIDEBAR_WEIXIN = NAVIGATION_ADMIN_SIDEBAR + "-WEIXIN";
    public static final String NAVIGATION_ADMIN_SIDEBAR_FINANCE = NAVIGATION_ADMIN_SIDEBAR + "-FINANCE";
    public static final String NAVIGATION_ADMIN_SIDEBAR_TOOLS = NAVIGATION_ADMIN_SIDEBAR + "-TOOLS";
    public static final String NAVIGATION_ADMIN_SIDEBAR_SETTINGS = NAVIGATION_ADMIN_SIDEBAR + "-SETTINGS";
    public static final String NAVIGATION_ADMIN_SIDEBAR_STATISTIC = NAVIGATION_ADMIN_SIDEBAR + "-STATISTIC";

    /*
     * 个人中心导航栏
     * */

    public static final String NAVIGATION_ADMIN_PROFILE = "ADMIN-PROFILE";
    public static final String NAVIGATION_ADMIN_PROFILE_INFO = NAVIGATION_ADMIN_PROFILE + "-INFO";
    public static final String NAVIGATION_ADMIN_PROFILE_SECURITY = NAVIGATION_ADMIN_PROFILE + "-SECURITY";

    /*
     * 系统设置导航栏
     * */

    public static final String NAVIGATION_ADMIN_SETTINGS = "ADMIN-SETTINGS";
    public static final String NAVIGATION_ADMIN_SETTINGS_SITE = NAVIGATION_ADMIN_SETTINGS + "-SITE";
    public static final String NAVIGATION_ADMIN_SETTINGS_DICTIONARY = NAVIGATION_ADMIN_SETTINGS + "-DICTIONARY";
    public static final String NAVIGATION_ADMIN_SETTINGS_REGION = NAVIGATION_ADMIN_SETTINGS + "-REGION";
    public static final String NAVIGATION_ADMIN_SETTINGS_SECURITY = NAVIGATION_ADMIN_SETTINGS + "-SECURITY";

    /*
     * 安全配置导航栏
     * */

    public static final String NAVIGATION_ADMIN_SECURITY = "ADMIN-SECURITY";
    public static final String NAVIGATION_ADMIN_SECURITY_ROLE = NAVIGATION_ADMIN_SECURITY + "-ROLE";
    public static final String NAVIGATION_ADMIN_SECURITY_PRIVILEGE = NAVIGATION_ADMIN_SECURITY + "-PRIVILEGE";
    public static final String NAVIGATION_ADMIN_SECURITY_AUTHENTICATION = NAVIGATION_ADMIN_SECURITY + "-AUTHENTICATION";

    /*
     * 新闻管理导航栏
     * */

    public static final String NAVIGATION_ADMIN_NEWS = "ADMIN-NEWS";
    public static final String NAVIGATION_ADMIN_NEWS_USER_ARTICLE = NAVIGATION_ADMIN_NEWS + "-USER-ARTICLE";
    public static final String NAVIGATION_ADMIN_NEWS_USER_CHANNEL = NAVIGATION_ADMIN_NEWS + "-USER-CHANNEL";
    public static final String NAVIGATION_ADMIN_NEWS_ARTICLE = NAVIGATION_ADMIN_NEWS + "-ARTICLE";
    public static final String NAVIGATION_ADMIN_NEWS_CHANNEL = NAVIGATION_ADMIN_NEWS + "-CHANNEL";

    /*
     * 我的文章导航栏
     * */

    public static final String NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT = "ADMIN-NEWS-USER-ARTICLE-MANAGEMENT";
    public static final String NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT = NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT + "-DRAFT";
    public static final String NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_AUDITING = NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT + "-AUDITING";
    public static final String NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_REJECTED = NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT + "-REJECTED";
    public static final String NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_PUBLISHED = NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT + "-PUBLISHED";
    public static final String NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DISABLED = NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT + "-DISABLED";

    /*
     * 文章管理导航栏
     * */

    public static final String NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT = "ADMIN-NEWS-ARTICLE-MANAGEMENT";
    public static final String NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_AUDITING = NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT + "-AUDITING";
    public static final String NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_PUBLISHED = NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT + "-PUBLISHED";
    public static final String NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_DISABLED = NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT + "-DISABLED";

    /*
     * 商城管理导航栏
     * */

    public static final String NAVIGATION_ADMIN_MALL = "ADMIN-MALL";
    public static final String NAVIGATION_ADMIN_MALL_USER_STORE = NAVIGATION_ADMIN_MALL + "-USER-STORE";
    public static final String NAVIGATION_ADMIN_MALL_ORDER = NAVIGATION_ADMIN_MALL + "-ORDER";
    public static final String NAVIGATION_ADMIN_MALL_COMMODITY = NAVIGATION_ADMIN_MALL + "-COMMODITY";
    public static final String NAVIGATION_ADMIN_MALL_STORE = NAVIGATION_ADMIN_MALL + "-STORE";
    public static final String NAVIGATION_ADMIN_MALL_SETTINGS = NAVIGATION_ADMIN_MALL + "-SETTINGS";

    /*
     * 我的店铺导航栏
     * */

    public static final String NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT = "ADMIN-MALL-USER-STORE-MANAGEMENT";
    public static final String NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_ORDER = NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT + "-ORDER";
    public static final String NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY = NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT + "-COMMODITY";
    public static final String NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_STORE = NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT + "-STORE";

    /*
     * 财务管理导航栏
     * */

    public static final String NAVIGATION_ADMIN_FINANCE = "ADMIN-FINANCE";
    public static final String NAVIGATION_ADMIN_FINANCE_DEPOSIT = NAVIGATION_ADMIN_FINANCE + "-WALLET-DEPOSIT";
    public static final String NAVIGATION_ADMIN_FINANCE_WALLET_MANAGEMENT = NAVIGATION_ADMIN_FINANCE + "-WALLET-MANAGEMENT";
    public static final String NAVIGATION_ADMIN_FINANCE_WEIXIN_SETTINGS = NAVIGATION_ADMIN_FINANCE + "-WEIXIN-SETTINGS";

    /*
     * 消息服务导航栏
     * */

    public static final String NAVIGATION_ADMIN_MESSAGE = "ADMIN-MESSAGE";
    public static final String NAVIGATION_ADMIN_MESSAGE_SMS_SEND = NAVIGATION_ADMIN_MESSAGE + "-SMS-SEND";
    public static final String NAVIGATION_ADMIN_MESSAGE_EMAIL_SEND = NAVIGATION_ADMIN_MESSAGE + "-EMAIL-SEND";
    public static final String NAVIGATION_ADMIN_MESSAGE_EMAIL_SETTINGS = NAVIGATION_ADMIN_MESSAGE + "-EMAIL-SETTINGS";
    public static final String NAVIGATION_ADMIN_MESSAGE_SMS_SETTINGS = NAVIGATION_ADMIN_MESSAGE + "-SMS-SETTINGS";
    public static final String NAVIGATION_ADMIN_MESSAGE_INNER_MESSAGE = NAVIGATION_ADMIN_MESSAGE + "-INNER-MESSAGE";
    public static final String NAVIGATION_ADMIN_MESSAGE_SITE_NOTIFICATION = NAVIGATION_ADMIN_MESSAGE + "-SITE-NOTIFICATION";

    /*
     * 兑换码管理导航栏
     * */

    public static final String NAVIGATION_ADMIN_REDEEM = "ADMIN-REDEEM";
    public static final String NAVIGATION_ADMIN_REDEEM_MANAGEMENT = NAVIGATION_ADMIN_REDEEM + "-MANAGEMENT";
    public static final String NAVIGATION_ADMIN_REDEEM_SETTINGS = NAVIGATION_ADMIN_REDEEM + "-SETTINGS";

    /*
     * 蜘蛛管理导航栏
     * */

    public static final String NAVIGATION_ADMIN_SPIDER = "ADMIN-SPIDER";
    public static final String NAVIGATION_ADMIN_SPIDER_STATUS = NAVIGATION_ADMIN_SPIDER + "-STATUS";
    public static final String NAVIGATION_ADMIN_SPIDER_LOG = NAVIGATION_ADMIN_SPIDER + "-LOG";

    /*
     * 微信公众平台导航栏
     * */

    public static final String NAVIGATION_ADMIN_WEIXIN = "ADMIN-WEIXIN";
    public static final String NAVIGATION_ADMIN_WEIXIN_MATERIAL = NAVIGATION_ADMIN_WEIXIN + "-MATERIAL";
    public static final String NAVIGATION_ADMIN_WEIXIN_MENU = NAVIGATION_ADMIN_SPIDER + "-MENU";
    public static final String NAVIGATION_ADMIN_WEIXIN_SETTINGS = NAVIGATION_ADMIN_SPIDER + "-SETTINGS";

    /*
     * 用户管理导航栏
     * */

    public static final String NAVIGATION_ADMIN_USER = "ADMIN-USER";
    public static final String NAVIGATION_ADMIN_USER_ALL = NAVIGATION_ADMIN_USER + "-ALL";
    public static final String NAVIGATION_ADMIN_USER_WEIXIN = NAVIGATION_ADMIN_USER + "-WEIXIN";

    /*
     * 链接管理导航栏
     * */

    public static final String NAVIGATION_ADMIN_LINK = "ADMIN-LINK";
    public static final String NAVIGATION_ADMIN_LINK_ALL = NAVIGATION_ADMIN_LINK + "-ALL";
    public static final String NAVIGATION_ADMIN_LINK_SETTINGS = NAVIGATION_ADMIN_LINK + "-SETTINGS";

    /*
     * 网站统计导航栏
     * */

    public static final String NAVIGATION_ADMIN_STATISTIC = "ADMIN-STATISTIC";
    public static final String NAVIGATION_ADMIN_STATISTIC_PAGES = NAVIGATION_ADMIN_STATISTIC + "-PAGES";
    public static final String NAVIGATION_ADMIN_STATISTIC_KEYWORDS = NAVIGATION_ADMIN_STATISTIC + "-KEYWORDS";
    public static final String NAVIGATION_ADMIN_STATISTIC_ACCESS_HISTORY = NAVIGATION_ADMIN_STATISTIC + "-ACCESS-HISTORY";
    public static final String NAVIGATION_ADMIN_STATISTIC_ACCESS_ADDRESS = NAVIGATION_ADMIN_STATISTIC + "-ACCESS-ADDRESS";
    public static final String NAVIGATION_ADMIN_STATISTIC_BAIDU_LINK = NAVIGATION_ADMIN_STATISTIC + "-BAIDU-LINK";
    public static final String NAVIGATION_ADMIN_STATISTIC_SETTINGS = NAVIGATION_ADMIN_STATISTIC + "-SETTINGS";

    @Override
    public void configureNavigation(NavigationService navigationService) {
        navigationService.add(new Navigation(NAVIGATION_ADMIN_SIDEBAR, "侧边导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_USER, "用户管理", QXCMP_BACKEND_URL + "/user").setIcon(new Icon("users")).setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_USER)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_NEWS, "新闻管理", QXCMP_BACKEND_URL + "/news").setIcon(new Icon("newspaper")).setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_MALL, "商城管理", QXCMP_BACKEND_URL + "/mall").setIcon(new Icon("shipping")).setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_MESSAGE, "消息服务", QXCMP_BACKEND_URL + "/message").setIcon(new Icon("announcement")).setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MESSAGE)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_WEIXIN, "微信公众平台", QXCMP_BACKEND_URL + "/weixin").setIcon(new Icon("wechat")).setOrder(50).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_WEIXIN)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_FINANCE, "财务管理", QXCMP_BACKEND_URL + "/finance").setIcon(new Icon("yen")).setOrder(60).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_STATISTIC, "网站统计", QXCMP_BACKEND_URL + "/statistic").setIcon(new Icon("bar chart")).setOrder(70).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_TOOLS, "系统工具", QXCMP_BACKEND_URL + "/tools").setIcon(new Icon("lab")).setOrder(80).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_TOOL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_SETTINGS, "系统设置", QXCMP_BACKEND_URL + "/settings").setIcon(new Icon("settings")).setOrder(90).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_PROFILE, "个人中心导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_PROFILE_INFO, "基本资料", QXCMP_BACKEND_URL + "/profile/info").setIcon(new Icon("user")).setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_SYSTEM_ADMIN)))
                .addItem(new Navigation(NAVIGATION_ADMIN_PROFILE_SECURITY, "安全设置", QXCMP_BACKEND_URL + "/profile/security").setIcon(new Icon("lock")).setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_SYSTEM_ADMIN)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_NEWS, "新闻管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE, "我的文章", QXCMP_BACKEND_URL + "/news/user/article").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_CHANNEL, "我的栏目", QXCMP_BACKEND_URL + "/news/user/channel").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE, "文章管理", QXCMP_BACKEND_URL + "/news/article").setOrder(30).setPrivilegesOr(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_AUDIT, PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_CHANNEL, "栏目管理", QXCMP_BACKEND_URL + "/news/channel").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_CHANNEL)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, "我的文章导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT, "草稿箱", QXCMP_BACKEND_URL + "/news/user/article/draft").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_AUDITING, "审核中", QXCMP_BACKEND_URL + "/news/user/article/auditing").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_REJECTED, "未通过", QXCMP_BACKEND_URL + "/news/user/article/rejected").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_PUBLISHED, "已发布", QXCMP_BACKEND_URL + "/news/user/article/published").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DISABLED, "已禁用", QXCMP_BACKEND_URL + "/news/user/article/disabled").setOrder(50).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT, "文章管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_AUDITING, "待审核文章", QXCMP_BACKEND_URL + "/news/article/auditing").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_AUDIT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_PUBLISHED, "已发布文章", QXCMP_BACKEND_URL + "/news/article/published").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_DISABLED, "已禁用文章", QXCMP_BACKEND_URL + "/news/article/disabled").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_MALL, "商城管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE, "我的店铺", QXCMP_BACKEND_URL + "/mall/user/store").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_ORDER, "订单管理", QXCMP_BACKEND_URL + "/mall/order").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL_ORDER)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_COMMODITY, "商品管理", QXCMP_BACKEND_URL + "/mall/commodity").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL_COMMODITY)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_STORE, "店铺管理", QXCMP_BACKEND_URL + "/mall/store").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL_STORE)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_SETTINGS, "商城设置", QXCMP_BACKEND_URL + "/mall/settings").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL_SETTINGS)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, "我的店铺导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_ORDER, "订单管理", QXCMP_BACKEND_URL + "/mall/user/store/order").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY, "商品管理", QXCMP_BACKEND_URL + "/mall/user/store/commodity").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_STORE, "店铺设置", QXCMP_BACKEND_URL + "/mall/user/store/settings").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_SETTINGS, "系统配置导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_SETTINGS_SITE, "网站配置", QXCMP_BACKEND_URL + "/settings/site").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SETTINGS_DICTIONARY, "系统字典", QXCMP_BACKEND_URL + "/settings/dictionary").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SETTINGS_REGION, "地区管理", QXCMP_BACKEND_URL + "/settings/region").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SETTINGS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SETTINGS_SECURITY, "安全配置", QXCMP_BACKEND_URL + "/security").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SECURITY)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_SECURITY, "安全配置导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_SECURITY_ROLE, "角色管理", QXCMP_BACKEND_URL + "/security/role").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SECURITY)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SECURITY_PRIVILEGE, "权限管理", QXCMP_BACKEND_URL + "/security/privilege").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SECURITY)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SECURITY_AUTHENTICATION, "认证配置", QXCMP_BACKEND_URL + "/security/authentication").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SECURITY)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_FINANCE, "财务管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_FINANCE_DEPOSIT, "充值订单管理", QXCMP_BACKEND_URL + "/finance/deposit").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE_DEPOSIT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_FINANCE_WALLET_MANAGEMENT, "用户钱包管理", QXCMP_BACKEND_URL + "/finance/wallet").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE_WALLET_MANAGEMENT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_FINANCE_WEIXIN_SETTINGS, "微信支付配置", QXCMP_BACKEND_URL + "/finance/weixin").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE_WEIXIN)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_MESSAGE, "消息服务导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_MESSAGE_SMS_SEND, "短信发送服务", QXCMP_BACKEND_URL + "/message/sms/send").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MESSAGE_SMS_SEND)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MESSAGE_EMAIL_SEND, "邮件发送服务", QXCMP_BACKEND_URL + "/message/email/send").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MESSAGE_EMAIL_SEND)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MESSAGE_INNER_MESSAGE, "站内信服务", QXCMP_BACKEND_URL + "/message/inner/message").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MESSAGE_SITE_NOTIFICATION)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MESSAGE_SITE_NOTIFICATION, "网站通知服务", QXCMP_BACKEND_URL + "/message/site/notification").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MESSAGE_INNER_MESSAGE)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MESSAGE_SMS_SETTINGS, "短信服务配置", QXCMP_BACKEND_URL + "/message/sms/settings").setOrder(50).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MESSAGE_SMS_CONFIG)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MESSAGE_EMAIL_SETTINGS, "邮件服务配置", QXCMP_BACKEND_URL + "/message/email/settings").setOrder(60).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MESSAGE_EMAIL_CONFIG)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_REDEEM, "兑换码管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_REDEEM_MANAGEMENT, "兑换码管理", QXCMP_BACKEND_URL + "/redeem").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_REDEEM)))
                .addItem(new Navigation(NAVIGATION_ADMIN_REDEEM_SETTINGS, "兑换码配置", QXCMP_BACKEND_URL + "/redeem/settings").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_REDEEM)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_SPIDER, "蜘蛛管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_SPIDER_STATUS, "运行状态", QXCMP_BACKEND_URL + "/spider/status").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SPIDER)))
                .addItem(new Navigation(NAVIGATION_ADMIN_SPIDER_LOG, "抓取日志", QXCMP_BACKEND_URL + "/spider/log").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_ADMIN_SPIDER)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_WEIXIN, "微信公众号导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_WEIXIN_MATERIAL, "素材管理", QXCMP_BACKEND_URL + "/weixin/material").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_WEIXIN_MATERIAL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_WEIXIN_MENU, "公众号菜单", QXCMP_BACKEND_URL + "/weixin/menu").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_WEIXIN_MENU)))
                .addItem(new Navigation(NAVIGATION_ADMIN_WEIXIN_SETTINGS, "公众号配置", QXCMP_BACKEND_URL + "/weixin/settings").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_WEIXIN_SETTINGS)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_USER, "用户管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_USER_ALL, "全部用户", QXCMP_BACKEND_URL + "/user/all").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_USER)))
                .addItem(new Navigation(NAVIGATION_ADMIN_USER_WEIXIN, "微信用户", QXCMP_BACKEND_URL + "/user/weixin").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_USER)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_LINK, "链接管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_LINK_ALL, "链接管理", QXCMP_BACKEND_URL + "/link").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_USER)))
                .addItem(new Navigation(NAVIGATION_ADMIN_LINK_SETTINGS, "链接设置", QXCMP_BACKEND_URL + "/link/settings").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_USER)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_STATISTIC, "网站统计导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_STATISTIC_PAGES, "页面访问统计", QXCMP_BACKEND_URL + "/statistic/pages").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_STATISTIC)))
                .addItem(new Navigation(NAVIGATION_ADMIN_STATISTIC_KEYWORDS, "关键字统计", QXCMP_BACKEND_URL + "/statistic/keywords").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_STATISTIC)))
                .addItem(new Navigation(NAVIGATION_ADMIN_STATISTIC_ACCESS_HISTORY, "访问历史记录", QXCMP_BACKEND_URL + "/statistic/access/history").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_STATISTIC)))
                .addItem(new Navigation(NAVIGATION_ADMIN_STATISTIC_ACCESS_ADDRESS, "访问地址管理", QXCMP_BACKEND_URL + "/statistic/access/address").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_STATISTIC)))
                .addItem(new Navigation(NAVIGATION_ADMIN_STATISTIC_BAIDU_LINK, "百度链接提交", QXCMP_BACKEND_URL + "/statistic/baidu/link").setOrder(50).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_STATISTIC)))
                .addItem(new Navigation(NAVIGATION_ADMIN_STATISTIC_SETTINGS, "网站统计设置", QXCMP_BACKEND_URL + "/statistic/settings").setOrder(60).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_STATISTIC_SETTINGS)))
        );
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

}
