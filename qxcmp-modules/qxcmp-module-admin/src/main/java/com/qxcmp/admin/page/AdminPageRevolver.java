package com.qxcmp.admin.page;

/**
 * 平台页面解析服务
 * <p>
 * 用于解析基础页面的实现类型
 *
 * @author Aaric
 */
public interface AdminPageRevolver {

    /**
     * 解析后台错误页面
     *
     * @return 错误页面类型
     */
    Class<? extends AbstractAdminErrorPage> getAdminErrorPage();

    /**
     * 解析后台概览页面
     *
     * @return 概览页面类型
     */
    Class<? extends AbstractAdminOverviewPage> getAdminOverviewPage();

}
