package com.qxcmp.core.extension;

import com.qxcmp.web.view.support.AnchorTarget;

/**
 * 平台后台用户管理页面工具栏扩展
 *
 * @author Aaric
 */
public interface AdminUserDetailsPageToolbarExtension extends Extension {

    /**
     * @return 工具栏名称
     */
    String getTitle();

    /**
     * @return 工具栏按钮后缀
     */
    String getSuffix();

    /**
     * @return 操作的窗口打开方式
     */
    default AnchorTarget getTarget() {
        return AnchorTarget.BLANK;
    }
}
