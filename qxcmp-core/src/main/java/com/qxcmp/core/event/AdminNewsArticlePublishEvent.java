package com.qxcmp.core.event;

import com.qxcmp.news.Article;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * @author Aaric
 */
@Getter
public class AdminNewsArticlePublishEvent {

    private final User user;
    private final Article article;

    public AdminNewsArticlePublishEvent(User user, Article article) {
        this.user = user;
        this.article = article;
    }
}
