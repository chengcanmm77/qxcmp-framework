package com.qxcmp.web.view.elements.menu;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

@Getter
@Setter
public class AccordionMenu extends AbstractMenu {
    /**
     * ID
     * <p>
     * 用于 JS 初始化
     */
    private String id = "accordion-" + RandomStringUtils.randomAlphanumeric(10);

    /**
     * 是否一次只打开一个区块
     */
    private boolean exclusive;

    @Override
    public String getFragmentName() {
        return "menu-accordion";
    }

    @Override
    public String getClassSuffix() {
        return "vertical accordion " + super.getClassSuffix();
    }
}
