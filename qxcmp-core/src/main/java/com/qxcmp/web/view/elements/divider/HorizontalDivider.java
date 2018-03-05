package com.qxcmp.web.view.elements.divider;

import com.qxcmp.web.view.elements.header.AbstractHeader;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 水平分隔符
 *
 * @author Aaric
 */
@Getter
@Setter
public class HorizontalDivider extends Divider {

    /**
     * 分隔符文本
     */
    private String text;

    /**
     * 分隔符标题元素
     * <p>
     * 用于高级形式的显示
     */
    private AbstractHeader header;

    public HorizontalDivider(String text) {
        this.text = text;
    }

    public HorizontalDivider(AbstractHeader header) {
        this.header = header;
    }

    @Override
    public String getFragmentName() {
        return "horizontal";
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + (Objects.isNull(header) ? " horizontal" : " header horizontal");
    }
}
