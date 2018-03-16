package com.qxcmp.article.event;

import com.qxcmp.article.Article;
import com.qxcmp.user.User;

/**
 * 文章发布事件
 *
 * @author Aaric
 */
public class ArticlePublishEvent extends BaseArticleStatusChangeEvent {
    public ArticlePublishEvent(User target, Article article) {
        super(target, article);
    }
}
