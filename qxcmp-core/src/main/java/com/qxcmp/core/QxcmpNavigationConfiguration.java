package com.qxcmp.core;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpSecurityConfiguration.*;

/**
 * 平台导航配置
 *
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class QxcmpNavigationConfiguration implements NavigationLoader {

    /*
     * 移动端导航栏扩展
     * */

    public static final String NAVIGATION_GLOBAL_MOBILE_TOP = "GLOBAL-MOBILE-TOP";
    public static final String NAVIGATION_GLOBAL_MOBILE_BOTTOM = "GLOBAL-MOBILE-BOTTOM";
    public static final String NAVIGATION_GLOBAL_MOBILE_SIDEBAR = "GLOBAL-MOBILE-SIDEBAR";


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

    @Override
    public void configNavigation(NavigationService navigationService) {

        navigationService.add(new Navigation(NAVIGATION_ADMIN_NEWS, "新闻管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE, "我的文章", QXCMP_ADMIN_URL + "/news/user/article").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_CHANNEL, "我的栏目", QXCMP_ADMIN_URL + "/news/user/channel").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE, "文章管理", QXCMP_ADMIN_URL + "/news/article").setOrder(30).setPrivilegesOr(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_AUDIT, PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_CHANNEL, "栏目管理", QXCMP_ADMIN_URL + "/news/channel").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_CHANNEL)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, "我的文章导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT, "草稿箱", QXCMP_ADMIN_URL + "/news/user/article/draft").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_AUDITING, "审核中", QXCMP_ADMIN_URL + "/news/user/article/auditing").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_REJECTED, "未通过", QXCMP_ADMIN_URL + "/news/user/article/rejected").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_PUBLISHED, "已发布", QXCMP_ADMIN_URL + "/news/user/article/published").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DISABLED, "已禁用", QXCMP_ADMIN_URL + "/news/user/article/disabled").setOrder(50).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT, "文章管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_AUDITING, "待审核文章", QXCMP_ADMIN_URL + "/news/article/auditing").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_AUDIT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_PUBLISHED, "已发布文章", QXCMP_ADMIN_URL + "/news/article/published").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_DISABLED, "已禁用文章", QXCMP_ADMIN_URL + "/news/article/disabled").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_MALL, "商城管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE, "我的店铺", QXCMP_ADMIN_URL + "/mall/user/store").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_ORDER, "订单管理", QXCMP_ADMIN_URL + "/mall/order").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL_ORDER)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_COMMODITY, "商品管理", QXCMP_ADMIN_URL + "/mall/commodity").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL_COMMODITY)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_STORE, "店铺管理", QXCMP_ADMIN_URL + "/mall/store").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL_STORE)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_SETTINGS, "商城设置", QXCMP_ADMIN_URL + "/mall/settings").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL_SETTINGS)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, "我的店铺导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_ORDER, "订单管理", QXCMP_ADMIN_URL + "/mall/user/store/order").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY, "商品管理", QXCMP_ADMIN_URL + "/mall/user/store/commodity").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_STORE, "店铺设置", QXCMP_ADMIN_URL + "/mall/user/store/settings").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_MALL)))
        );


        navigationService.add(new Navigation(NAVIGATION_ADMIN_FINANCE, "财务管理导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_FINANCE_DEPOSIT, "充值订单管理", QXCMP_ADMIN_URL + "/finance/deposit").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE_DEPOSIT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_FINANCE_WALLET_MANAGEMENT, "用户钱包管理", QXCMP_ADMIN_URL + "/finance/wallet").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE_WALLET_MANAGEMENT)))
                .addItem(new Navigation(NAVIGATION_ADMIN_FINANCE_WEIXIN_SETTINGS, "微信支付配置", QXCMP_ADMIN_URL + "/finance/weixin").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_FINANCE_WEIXIN)))
        );

        navigationService.add(new Navigation(NAVIGATION_ADMIN_WEIXIN, "微信公众号导航栏")
                .addItem(new Navigation(NAVIGATION_ADMIN_WEIXIN_MATERIAL, "素材管理", QXCMP_ADMIN_URL + "/weixin/material").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_WEIXIN_MATERIAL)))
                .addItem(new Navigation(NAVIGATION_ADMIN_WEIXIN_MENU, "公众号菜单", QXCMP_ADMIN_URL + "/weixin/menu").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_WEIXIN_MENU)))
                .addItem(new Navigation(NAVIGATION_ADMIN_WEIXIN_SETTINGS, "公众号配置", QXCMP_ADMIN_URL + "/weixin/settings").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_WEIXIN_SETTINGS)))
        );
    }
}
