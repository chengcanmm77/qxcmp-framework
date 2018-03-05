package com.qxcmp.web.model.navigation;


/**
 * 导航栏配置接口 实现该接口的Spring Bean会在平台启动时自动加载并配置导航栏
 *
 * @author aaric
 */
public interface NavigationConfigurator {

    /**
     * 配置导航栏
     *
     * @param navigationService 导航栏服务
     */
    void configureNavigation(NavigationService navigationService);

    default int order() {
        return 0;
    }
}
