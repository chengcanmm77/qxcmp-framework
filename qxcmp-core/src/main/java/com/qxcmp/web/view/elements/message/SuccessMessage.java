package com.qxcmp.web.view.elements.message;

import com.qxcmp.web.view.elements.list.List;

public class SuccessMessage extends Message {

    public SuccessMessage(String content) {
        super(content);
    }

    public SuccessMessage(String title, String content) {
        super(title, content);
    }

    public SuccessMessage(String title, String content, List list) {
        super(title, content, list);
    }

    public SuccessMessage(String title, List list) {
        super(title, list);
    }

    @Override
    public String getClassSuffix() {
        return "success " + super.getClassSuffix();
    }
}
