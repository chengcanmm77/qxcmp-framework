package com.qxcmp.web.view;

import org.apache.commons.lang3.StringUtils;

/**
 * UI 组件接口
 *
 * @author aaric
 */
public interface Component {

    String DEFAULT_FRAGMENT_NAME = "default";

    /**
     * @return 获取组件对应的渲染模板文件名称
     */
    String getFragmentFile();

    Object getContext(String key);

    /**
     * 为组件增加自定义 class 属性
     *
     * @param customClass 自定义class属性
     * @return 组件本身
     */
    Component setCustomClass(String customClass);

    String getCustomClass();

    /**
     * @return 获取组件对应的渲染模板片段名称
     */
    default String getFragmentName() {
        return DEFAULT_FRAGMENT_NAME;
    }

    /**
     * @return 组件 CSS class 前缀
     */
    default String getClassPrefix() {
        return "";
    }

    /**
     * @return 组件 CSS class 内容
     */
    default String getClassContent() {
        return "";
    }

    /**
     * @return 组件 CSS class 后缀
     */
    default String getClassSuffix() {
        return "";
    }

    /**
     * @return 该组件经过计算后的组件 CSS class 名称
     */
    default String getClassName() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClassPrefix()).append(" ").append(getClassContent()).append(" ").append(getClassSuffix());

        if (StringUtils.isNotBlank(getCustomClass())) {
            stringBuilder.append(" ").append(getCustomClass());
        }

        return stringBuilder.toString().trim().replaceAll("\\s+", " ");
    }

}
