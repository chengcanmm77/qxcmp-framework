package com.qxcmp.web.view.elements.message;

import com.qxcmp.web.view.elements.list.List;

public class NegativeMessage extends Message {

    public NegativeMessage(String content) {
        super(content);
    }

    public NegativeMessage(String title, String content) {
        super(title, content);
    }

    public NegativeMessage(String title, String content, List list) {
        super(title, content, list);
    }

    public NegativeMessage(String title, List list) {
        super(title, list);
    }

    @Override
    public String getClassSuffix() {
        return "negative " + super.getClassSuffix();
    }
}
