package com.qxcmp.web.view.elements.html;

/**
 * Html 文本展示
 * <p>
 * 依赖于QuillJs
 *
 * @author Aaric
 */
public class HtmlText extends BlockElement {
    public HtmlText(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "html-text";
    }
}
