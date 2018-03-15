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

    @Override
    public void configNavigation(NavigationService navigationService) {

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
    }
}
