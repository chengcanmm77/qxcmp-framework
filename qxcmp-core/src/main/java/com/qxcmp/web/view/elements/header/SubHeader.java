package com.qxcmp.web.view.elements.header;

/**
 * 子抬头
 * <p>
 * 子抬头拥有较小的大小
 *
 * @author Aaric
 */
public class SubHeader extends AbstractHeader {

    public SubHeader(String title) {
        super(title);
    }

    @Override
    public String getClassContent() {
        return super.getClassContent() + " sub";
    }
}
