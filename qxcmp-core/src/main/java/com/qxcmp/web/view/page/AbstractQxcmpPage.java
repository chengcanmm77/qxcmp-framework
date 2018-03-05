package com.qxcmp.web.view.page;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.Component;
import lombok.Getter;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 平台页面实现
 *
 * @author Aaric
 */
public abstract class AbstractQxcmpPage implements QxcmpPage {

    private static final String PAGE = "qxcmp";
    private static final String BASE_MODEL_OBJECT = "page";

    private final ModelAndView modelAndView = new ModelAndView(PAGE);

    @Getter
    private String title;
    @Getter
    private List<Component> components = Lists.newArrayList();
    @Getter
    private List<String> stylesheets = Lists.newArrayList();
    @Getter
    private List<String> javaScripts = Lists.newArrayList();
    @Getter
    private List<String> bodyJavaScripts = Lists.newArrayList();

    @Override
    public QxcmpPage setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public QxcmpPage addComponent(Component component) {
        components.add(component);
        return this;
    }

    @Override
    public QxcmpPage addComponent(Supplier<Component> supplier) {
        Component component = supplier.get();
        if (Objects.nonNull(component)) {
            components.add(component);
        }
        return this;
    }

    @Override
    public QxcmpPage addComponents(Collection<Component> components) {
        this.components.addAll(components);
        return this;
    }

    @Override
    public QxcmpPage addStylesheet(String stylesheet) {
        this.stylesheets.add(stylesheet);
        return this;
    }

    @Override
    public QxcmpPage addJavascript(String javascript) {
        this.javaScripts.add(javascript);
        return this;
    }

    @Override
    public QxcmpPage addJavascriptToBody(String javascript) {
        this.bodyJavaScripts.add(javascript);
        return this;
    }

    @Override
    public ModelAndView build() {
        modelAndView.addObject(BASE_MODEL_OBJECT, this);
        return modelAndView;
    }

    @Override
    public void renderNormal() {
        render();
    }

    @Override
    public void renderTablet() {
        render();
    }

    @Override
    public void renderMobile() {
        render();
    }
}
