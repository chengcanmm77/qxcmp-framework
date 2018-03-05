package com.qxcmp.web.view;

/**
 * 自定义组件
 *
 * @author aaric
 */
public class CustomComponent extends AbstractComponent {

    private String fragmentFile;

    private String fragmentName = DEFAULT_FRAGMENT_NAME;

    public CustomComponent(String fragmentFile) {
        this.fragmentFile = fragmentFile;
    }

    public CustomComponent(String fragmentFile, String fragmentName) {
        this.fragmentFile = fragmentFile;
        this.fragmentName = fragmentName;
    }

    @Override
    public String getFragmentFile() {
        return fragmentFile;
    }

    @Override
    public String getFragmentName() {
        return fragmentName;
    }
}
