package com.qxcmp.web.view.elements.grid;

import lombok.Getter;
import lombok.Setter;

/**
 * 列网格
 * <p>
 * 该网格会在列之间显示分隔符
 * <p>
 * 列需要使用行来包裹
 *
 * @author Aaric
 */
@Getter
@Setter
public class DividedGrid extends AbstractGrid {
    @Override
    public String getClassContent() {
        return super.getClassContent() + " divided";
    }
}
