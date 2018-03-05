package com.qxcmp.web.view.elements.grid;

import lombok.Getter;
import lombok.Setter;

/**
 * 单元格网格
 * <p>
 * 该网格会显示单元格边框
 *
 * @author Aaric
 */
@Getter
@Setter
public class CelledGrid extends AbstractGrid {
    @Override
    public String getClassContent() {
        return super.getClassContent() + " celled";
    }
}
