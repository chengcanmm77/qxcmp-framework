package com.qxcmp.web.view.elements.container;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.support.Alignment;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class AbstractContainer extends AbstractComponent {

    /**
     * 容器子元素对齐方式
     */
    private Alignment alignment = Alignment.NONE;

    /**
     * 容器内容
     */
    private List<Component> components = Lists.newArrayList();

    public AbstractContainer addComponent(Component component) {
        components.add(component);
        return this;
    }

    public AbstractContainer addComponents(Collection<? extends Component> components) {
        this.components.addAll(components);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/elements/container";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return alignment.toString();
    }

    @Override
    public String getClassSuffix() {
        return "container";
    }

    public AbstractContainer setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }
}
