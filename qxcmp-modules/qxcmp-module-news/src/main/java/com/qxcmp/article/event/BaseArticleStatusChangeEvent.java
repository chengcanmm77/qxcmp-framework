package com.qxcmp.article.event;

import com.qxcmp.article.Article;
import com.qxcmp.core.event.QxcmpEvent;
import com.qxcmp.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Aaric
 */
@Getter
@RequiredArgsConstructor
public abstract class BaseArticleStatusChangeEvent implements QxcmpEvent {

    /**
     * 改变文章状态的用户
     */
    private final User target;

    /**
     * 改变状态的文章
     */
    private final Article article;
}
