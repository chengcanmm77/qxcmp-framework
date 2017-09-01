package com.qxcmp.framework.web.view.elements.divider;

import com.qxcmp.framework.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
public abstract class AbstractDivider extends AbstractComponent {

    /**
     * 分隔符文本
     */
    private String text;

    public AbstractDivider() {
        super("qxcmp/elements/divider");
    }

    public AbstractDivider(String text) {
        this();
        this.text = text;
    }

    @Override
    public String getClassName() {
        return "ui";
    }
}
