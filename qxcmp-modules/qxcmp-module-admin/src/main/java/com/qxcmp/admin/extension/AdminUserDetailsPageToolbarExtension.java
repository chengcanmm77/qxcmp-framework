package com.qxcmp.admin.extension;

import com.qxcmp.core.extension.Extension;
import com.qxcmp.web.view.support.AnchorTarget;

/**
 * 平台后台用户管理页面工具栏扩展
 *
 * @author Aaric
 */
public interface AdminUserDetailsPageToolbarExtension extends Extension {

    /**
     * 工具栏名称
     *
     * @return 工具栏名称
     */
    String getTitle();

    /**
     * 工具栏按钮后缀
     *
     * @return 工具栏按钮后缀
     */
    String getSuffix();

    /**
     * 操作的窗口打开方式
     *
     * @return 操作的窗口打开方式
     */
    default AnchorTarget getTarget() {
        return AnchorTarget.BLANK;
    }
}
