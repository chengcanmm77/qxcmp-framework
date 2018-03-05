package com.qxcmp.core.extension;

/**
 * 后台工具页面扩展
 *
 * @author Aaric
 */
public interface AdminToolPageExtension extends Extension {

    /**
     * 工具栏图标
     *
     * @return 工具栏图标
     */
    String getIcon();

    /**
     * 工具栏标题
     *
     * @return 工具栏标题
     */
    String getTitle();

    /**
     * 工具栏超链接
     *
     * @return 工具栏超链接
     */
    String getUrl();
}
