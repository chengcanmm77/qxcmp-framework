package com.qxcmp.web.view.page;

/**
 * 平台页面解析服务
 * <p>
 * 用于解析基础页面的实现类型
 *
 * @author Aaric
 */
public interface QxcmpPageResolver {

    /**
     * 解析平台错误页面
     *
     * @return 错误页面类型
     */
    Class<? extends QxcmpPage> getErrorPage();

    /**
     * 解析平台概览页面
     *
     * @return 概览页面类型
     */
    Class<? extends QxcmpPage> getOverviewPage();

}
