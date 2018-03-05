package com.qxcmp.web.view.elements.message;

import com.qxcmp.web.view.elements.list.List;

public class Message extends AbstractMessage {
    public Message(String content) {
        super(content);
    }

    public Message(String title, String content) {
        super(title, content);
    }

    public Message(String title, String content, List list) {
        super(title, content, list);
    }

    public Message(String title, List list) {
        super(title, list);
    }
}
