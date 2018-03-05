package com.qxcmp.web.view.elements.html;

import com.google.common.collect.Lists;
import com.qxcmp.web.view.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 基本HTML元素
 *
 * @author aaric
 */
@Getter
@Setter
public abstract class BaseHtmlElement extends AbstractComponent {

    /**
     * 元素 CSS class 名称
     * <p>
     * 该属性用于定制化一些基本HTML元素
     * <p>
     * 当需要在基本元素上增加一些 class 名称的时候使用该属性
     */
    private String className;

    /**
     * 元素文本
     */
    private String text;

    /**
     * 嵌套元素
     * <p>
     * 嵌套元素仅支持内联元素
     * <p>
     * 若使用嵌套，则会忽略 text 属性，直接渲染嵌套元素
     */
    private List<InlineElement> inlineElements = Lists.newArrayList();

    public BaseHtmlElement() {
    }

    public BaseHtmlElement(String text) {
        this.text = text;
    }

    public BaseHtmlElement addInlineElement(InlineElement inlineElement) {
        inlineElements.add(inlineElement);
        return this;
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/globals/html";
    }

    public BaseHtmlElement setClassName(String className) {
        this.className = className;
        return this;
    }
}
