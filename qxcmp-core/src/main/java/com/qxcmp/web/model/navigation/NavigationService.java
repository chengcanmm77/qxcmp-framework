package com.qxcmp.web.model.navigation;

public interface NavigationService {

    /**
     * 获取一个用户所属的导航栏
     *
     * @param id 全局 ID
     * @return 如果导航栏不存在，抛出异常
     */
    AbstractNavigation get(String id);

    /**
     * 创建一个导航，如果导航存在则抛出运行时异常
     *
     * @param navigation 导航具体类型
     */
    void add(AbstractNavigation navigation);

}
