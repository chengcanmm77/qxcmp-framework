package com.qxcmp.admin.page;

import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;

/**
 * 后台概览页面
 *
 * @author Aaric
 */
@RequiredArgsConstructor
public abstract class AbstractAdminOverviewPage extends AbstractQxcmpAdminPage {

    protected final Overview overview;
}
