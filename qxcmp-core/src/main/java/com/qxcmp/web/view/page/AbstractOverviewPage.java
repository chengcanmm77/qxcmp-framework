package com.qxcmp.web.view.page;

import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;

/**
 * 平台基本页面类型 - 概览页面
 *
 * @author Aaric
 */
@RequiredArgsConstructor
public abstract class AbstractOverviewPage extends AbstractQxcmpPage {

    protected final Overview overview;
}
