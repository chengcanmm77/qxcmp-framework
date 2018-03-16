package com.qxcmp.article.event;

import com.qxcmp.article.Article;
import com.qxcmp.user.User;
import lombok.Getter;

/**
 * 文章被驳回事件
 *
 * @author Aaric
 */
@Getter
public class ArticleRejectEvent extends BaseArticleStatusChangeEvent {

    /**
     * 驳回原因
     */
    private final String reason;

    public ArticleRejectEvent(User target, Article article, String reason) {
        super(target, article);
        this.reason = reason;
    }
}
