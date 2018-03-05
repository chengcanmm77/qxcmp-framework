package com.qxcmp.web.view.elements.list.item;

import com.qxcmp.web.view.elements.html.Div;
import lombok.Getter;
import lombok.Setter;

/**
 * 纯文本项目
 *
 * @author Aaric
 */
@Getter
@Setter
public class TextItem extends AbstractListItem {

    /**
     * 文本内容
     * <p>
     * 支持嵌套
     */
    private Div content;

    public TextItem(Div content) {
        this.content = content;
    }

    public TextItem(String content) {
        this.content = new Div(content);
    }

    @Override
    public String getFragmentName() {
        return "item-text";
    }
}
