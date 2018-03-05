package com.qxcmp.web.view.elements.message;

import com.qxcmp.web.view.elements.list.List;

public class InfoMessage extends Message {

    public InfoMessage(String content) {
        super(content);
    }

    public InfoMessage(String title, String content) {
        super(title, content);
    }

    public InfoMessage(String title, String content, List list) {
        super(title, content, list);
    }

    public InfoMessage(String title, List list) {
        super(title, list);
    }

    @Override
    public String getClassSuffix() {
        return "info " + super.getClassSuffix();
    }
}
