package com.qxcmp.web.view.elements.container;

/**
 * 流容器
 * <p>
 * 该容器会占满父容器宽度
 *
 * @author aaric
 */
public class FluidContainer extends AbstractContainer {

    @Override
    public String getClassContent() {
        return super.getClassContent() + " fluid";
    }
}
