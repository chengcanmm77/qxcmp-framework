package com.qxcmp.core.event;

import com.qxcmp.news.Article;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminNewsArticleEnableEvent {

    private final User user;
    private final Article article;

    public AdminNewsArticleEnableEvent(User user, Article article) {
        this.user = user;
        this.article = article;
    }
}
