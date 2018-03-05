package com.qxcmp.web.view.page;

/**
 * 平台一般页面
 *
 * @author Aaric
 */
public abstract class GenericQxcmpPage extends AbstractQxcmpPage {

    private final static String DEFAULT_STYLESHEET = "/assets/scripts/qxcmp.css";
    private final static String DEFAULT_SCRIPT = "/assets/scripts/qxcmp.js";

    public GenericQxcmpPage() {
        addStylesheet(DEFAULT_STYLESHEET);
        addJavascript(DEFAULT_SCRIPT);
    }
}
