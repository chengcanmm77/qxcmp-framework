package com.qxcmp.web.view;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 页面组件基本类
 * <p>
 * 每个组件最终会被渲染为一个Html元素
 *
 * @author aaric
 */
public abstract class AbstractComponent implements Component {

    private String customClass;

    private Map<String, Object> context = Maps.newLinkedHashMap();

    public AbstractComponent addContext(String key, Object object) {
        context.put(key, object);
        return this;
    }

    @Override
    public Component setCustomClass(String customClass) {
        this.customClass = customClass;
        return this;
    }

    @Override
    public String getCustomClass() {
        return customClass;
    }

    @Override
    public Object getContext(String key) {
        return context.get(key);
    }
}
