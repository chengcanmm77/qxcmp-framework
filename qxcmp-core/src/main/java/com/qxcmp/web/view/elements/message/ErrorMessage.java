package com.qxcmp.web.view.elements.message;

import com.qxcmp.web.view.elements.list.List;

public class ErrorMessage extends Message {

    public ErrorMessage(String content) {
        super(content);
    }

    public ErrorMessage(String title, String content) {
        super(title, content);
    }

    public ErrorMessage(String title, String content, List list) {
        super(title, content, list);
    }

    public ErrorMessage(String title, List list) {
        super(title, list);
    }

    @Override
    public String getClassSuffix() {
        return "error " + super.getClassSuffix();
    }
}
