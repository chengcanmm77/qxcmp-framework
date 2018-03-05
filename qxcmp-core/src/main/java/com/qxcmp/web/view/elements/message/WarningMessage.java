package com.qxcmp.web.view.elements.message;

import com.qxcmp.web.view.elements.list.List;

public class WarningMessage extends Message {

    public WarningMessage(String content) {
        super(content);
    }

    public WarningMessage(String title, String content) {
        super(title, content);
    }

    public WarningMessage(String title, String content, List list) {
        super(title, content, list);
    }

    public WarningMessage(String title, List list) {
        super(title, list);
    }

    @Override
    public String getClassSuffix() {
        return "warning " + super.getClassSuffix();
    }
}
