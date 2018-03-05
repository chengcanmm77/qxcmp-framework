package com.qxcmp.web.view.elements.loader;

/**
 * 简单加载器
 *
 * @author Aaric
 */
public class SimpleLoader extends AbstractLoader {

    public SimpleLoader() {
    }

    public SimpleLoader(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "simple";
    }
}
