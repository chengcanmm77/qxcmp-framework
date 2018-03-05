package com.qxcmp.web.view.modules.accordion;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public abstract class AbstractAccordion extends AbstractComponent {

    /**
     * ID
     * <p>
     * 用于 JS 初始化
     */
    private String id = "accordion-" + RandomStringUtils.randomAlphanumeric(10);

    /**
     * 是否占满宽度
     */
    private boolean fluid;

    /**
     * 是否翻转颜色
     */
    private boolean inverted;

    /**
     * 是否一次只打开一个区块
     */
    private boolean exclusive;

    private List<AbstractAccordionItem> items = Lists.newArrayList();

    public AbstractAccordion addItem(AbstractAccordionItem item) {
        items.add(item);
        return this;
    }

    public AbstractAccordion addItems(Collection<? extends AbstractAccordionItem> items) {
        this.items.addAll(items);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/modules/accordion";
    }

    @Override
    public String getFragmentName() {
        return "accordion";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (fluid) {
            stringBuilder.append(" fluid");
        }

        if (inverted) {
            stringBuilder.append(" inverted");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getClassSuffix() {
        return "accordion";
    }

    public AbstractAccordion setFluid() {
        setFluid(true);
        return this;
    }

    public AbstractAccordion setInverted() {
        setInverted(true);
        return this;
    }

    public AbstractAccordion setExclusive() {
        setExclusive(true);
        return this;
    }
}
