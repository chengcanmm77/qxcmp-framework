package com.qxcmp.web.view.elements.header;

import com.qxcmp.web.view.support.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 内容抬头
 * <p>
 * 用来对一部分内容进行概括
 * <p>
 * 内容抬头使用 em 控制大小，根据上下文大小的不同而不同
 *
 * @author Aaric
 */
@Getter
@Setter
public class ContentHeader extends AbstractHeader {

    /**
     * 大小，只支持 Huge, Large, Medium, Small, Tiny
     */
    private Size size = Size.NONE;

    public ContentHeader(String title, Size size) {
        super(title);
        this.size = size;
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + size.toString();
    }
}
