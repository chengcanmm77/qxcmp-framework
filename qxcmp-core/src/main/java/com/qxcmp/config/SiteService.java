package com.qxcmp.config;

/**
 * 网站相关服务
 *
 * @author Aaric
 */
public interface SiteService {

    /**
     * 获取网站名称
     *
     * @return 获取网站名称
     */
    String getTitle();

    /**
     * 获取网站协议
     * <p>
     * {@code http://} 或者 {@code https://}
     *
     * @return 获取网站协议
     */
    String getProtocol();

    /**
     * 获取网站域名
     *
     * @return 获取网站域名
     */
    String getDomain();

    /**
     * 获取网站首页链接
     * <p>
     * {@link #getProtocol()} + {@link #getDomain()}
     *
     * @return 获取网站首页链接
     */
    String getHomeUrl();

    /**
     * 获取网站 LOGO 链接
     *
     * @return 获取网站 LOGO 链接
     */
    String getLogo();

    /**
     * 获取网站图标链接
     *
     * @return 获取网站图标链接
     */
    String getFavicon();

    /**
     * 获取网站关键字
     *
     * @return 获取网站关键字
     */
    String getKeywords();

    /**
     * 获取网站描述
     *
     * @return 获取网站描述
     */
    String getDescription();
}
