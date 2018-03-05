package com.qxcmp.web.view.elements.html;

import lombok.Getter;
import lombok.Setter;

/**
 * 块级元素
 *
 * @author aaric
 */
@Getter
@Setter
public abstract class BlockElement extends BaseHtmlElement {
    public BlockElement() {
    }

    public BlockElement(String text) {
        super(text);
    }
}
