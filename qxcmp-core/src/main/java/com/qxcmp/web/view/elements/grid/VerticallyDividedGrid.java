package com.qxcmp.web.view.elements.grid;

import lombok.Getter;
import lombok.Setter;

/**
 * 行网格
 * <p>
 * 该网格会在行之间显示分隔符
 *
 * @author Aaric
 */
@Getter
@Setter
public class VerticallyDividedGrid extends AbstractGrid {
    @Override
    public String getClassContent() {
        return super.getClassContent() + " vertically divided";
    }
}
