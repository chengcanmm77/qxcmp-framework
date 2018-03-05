package com.qxcmp.web.view.modules.accordion;

public class StyledAccordion extends AbstractAccordion {

    @Override
    public String getClassSuffix() {
        return "styled " + super.getClassSuffix();
    }
}
