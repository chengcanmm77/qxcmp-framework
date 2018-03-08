package com.qxcmp.admin.page;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * 平台基本页面类型 - 错误页面
 *
 * @author Aaric
 */
@RequiredArgsConstructor
public abstract class AbstractAdminErrorPage extends AbstractQxcmpAdminPage {

    protected final Map<String, Object> errors;
}
