package com.qxcmp.article.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.article.ArticleService;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.article.ChannelService;
import com.qxcmp.article.event.AdminNewsArticleDisableEvent;
import com.qxcmp.article.event.AdminNewsArticleEnableEvent;
import com.qxcmp.audit.ActionException;
import com.qxcmp.user.User;
import com.qxcmp.web.model.RestfulResponse;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

/**
 * @author Aaric
 */
public abstract class AbstractNewsPageController extends QxcmpAdminController {

    protected ArticleService articleService;
    protected ChannelService channelService;

    /**
     * 批量删除文章
     *
     * @param keys  文章ID
     * @param force 是否强制，强制不会检查文章所有者
     *
     * @return 删除结果
     */
    protected ResponseEntity<RestfulResponse> batchDelete(List<String> keys, boolean force) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return execute("批量删除文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(Long.parseLong(key))
                            .filter(article -> !article.getStatus().equals(ArticleStatus.PUBLISHED))
                            .filter(article -> force || StringUtils.equals(article.getUserId(), user.getId()))
                            .ifPresent(articleService::delete);
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    /**
     * 批量禁用文章
     *
     * @param keys  文章ID
     * @param force 是否强制，强制不会检查文章所有者
     *
     * @return 禁用结果
     */
    protected ResponseEntity<RestfulResponse> batchDisable(List<String> keys, boolean force) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return execute("批量禁用文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(Long.parseLong(key))
                            .filter(article -> force || StringUtils.equals(article.getUserId(), user.getId()))
                            .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                            .ifPresent(article -> applicationContext.publishEvent(new AdminNewsArticleDisableEvent(user, articleService.update(article.getId(), a -> {
                                a.setDatePublished(new Date());
                                a.setStatus(ArticleStatus.DISABLED);
                                a.setDisableUser(user.getId());
                            }))));
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    /**
     * 启用一篇文章
     *
     * @param id    文章ID
     * @param force 是否强制，强制不会检查文章所有者
     *
     * @return 启用结果
     */
    protected ResponseEntity<RestfulResponse> enableArticle(Long id, boolean force) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.DISABLED))
                .filter(article -> force || StringUtils.equals(article.getUserId(), user.getId()))
                .map(article -> execute("启用文章", context -> {
                    try {
                        applicationContext.publishEvent(new AdminNewsArticleEnableEvent(user, articleService.update(article.getId(), a -> {
                            a.setDatePublished(new Date());
                            a.setStatus(ArticleStatus.PUBLISHED);
                            a.setAuditor(user.getId());
                        })));
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    /**
     * 禁用一篇文章
     *
     * @param id    文章ID
     * @param force 是否强制，强制不会检查文章所有者
     *
     * @return 禁用结果
     */
    protected ResponseEntity<RestfulResponse> disableArticle(Long id, boolean force) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                .filter(article -> force || StringUtils.equals(article.getUserId(), user.getId()))
                .map(article -> execute("禁用文章", context -> {
                    try {
                        applicationContext.publishEvent(new AdminNewsArticleDisableEvent(user, articleService.update(article.getId(), a -> {
                            a.setDateDisabled(DateTime.now().toDate());
                            a.setStatus(ArticleStatus.DISABLED);
                            a.setDisableUser(user.getId());
                        })));
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    /**
     * 删除一篇文章
     *
     * @param id    文章ID
     * @param force 是否强制，强制不会检查文章所有者
     *
     * @return 删除结果
     */
    protected ResponseEntity<RestfulResponse> deleteArticle(Long id, boolean force) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> !article.getStatus().equals(ArticleStatus.PUBLISHED))
                .filter(article -> force || StringUtils.equals(article.getUserId(), user.getId()))
                .map(article -> execute("删除文章", context -> {
                    try {
                        articleService.delete(article);
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Autowired
    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }
}
