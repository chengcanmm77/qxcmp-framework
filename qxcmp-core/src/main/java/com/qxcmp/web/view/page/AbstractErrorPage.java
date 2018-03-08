package com.qxcmp.web.view.page;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * 平台基本页面类型 - 错误页面
 *
 * @author Aaric
 */
@RequiredArgsConstructor
public abstract class AbstractErrorPage extends AbstractQxcmpPage {

    protected final Map<String, Object> errors;
}
