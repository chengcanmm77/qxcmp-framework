package com.qxcmp.web.view.elements.message;

import com.qxcmp.web.view.elements.list.List;

public class PositiveMessage extends Message {

    public PositiveMessage(String content) {
        super(content);
    }

    public PositiveMessage(String title, String content) {
        super(title, content);
    }

    public PositiveMessage(String title, String content, List list) {
        super(title, content, list);
    }

    public PositiveMessage(String title, List list) {
        super(title, list);
    }

    @Override
    public String getClassSuffix() {
        return "positive " + super.getClassSuffix();
    }
}
