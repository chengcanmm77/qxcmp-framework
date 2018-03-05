package com.qxcmp.web.view.elements.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerticalMenu extends AbstractMenu {

    /**
     * 是否占满父容器
     */
    private boolean fluid;

    @Override
    public String getClassContent() {
        return super.getClassContent() + (fluid ? " fluid" : "");
    }

    @Override
    public String getClassSuffix() {
        return "vertical " + super.getClassSuffix();
    }

    public VerticalMenu setFluid() {
        setFluid(true);
        return this;
    }
}
