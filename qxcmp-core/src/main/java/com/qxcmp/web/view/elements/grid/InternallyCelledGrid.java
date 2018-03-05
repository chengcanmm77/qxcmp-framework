package com.qxcmp.web.view.elements.grid;

import lombok.Getter;
import lombok.Setter;

/**
 * 内部单元格网格
 * <p>
 * 该网格会在单元格内部显示边框
 *
 * @author Aaric
 */
@Getter
@Setter
public class InternallyCelledGrid extends AbstractGrid {
    @Override
    public String getClassContent() {
        return super.getClassContent() + " internally celled";
    }
}
