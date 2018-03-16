package com.qxcmp.article.listener;

import com.google.common.collect.Sets;
import com.qxcmp.article.Article;
import com.qxcmp.article.Channel;
import com.qxcmp.article.event.*;
import com.qxcmp.config.SiteService;
import com.qxcmp.core.entity.EntityCreateEvent;
import com.qxcmp.core.entity.EntityDeleteEvent;
import com.qxcmp.core.entity.EntityUpdateEvent;
import com.qxcmp.message.FeedService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleSecurity.PRIVILEGE_NEWS_ARTICLE_AUDIT;
import static com.qxcmp.article.NewsModuleSecurity.PRIVILEGE_NEWS_CHANNEL;


/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminNewsArticleEventListener {

    private final FeedService feedService;
    private final SiteService siteService;
    private final UserService userService;

    @EventListener
    public void onChannelCreate(EntityCreateEvent<Channel> event) {
        List<User> users = getChannelUsers(event.getEntity());
        feedService.feedForUsers(users, event.getUser(),
                String.format("%s 新建了栏目 <a href='https://%s%s/channel/%d/edit'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getEntity().getId(),
                        event.getEntity().getName()));
    }

    @EventListener
    public void onChannelUpdate(EntityUpdateEvent<Channel> event) {
        List<User> users = getChannelUsers(event.getEntity());
        feedService.feedForUsers(users, event.getUser(),
                String.format("%s 编辑了栏目 <a href='https://%s%s/channel/%d/edit'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getEntity().getId(),
                        event.getEntity().getName()));
    }

    @EventListener
    public void onChannelDelete(EntityDeleteEvent<Channel> event) {
        List<User> users = getChannelUsers(event.getEntity());
        feedService.feedForUsers(users, event.getUser(),
                String.format("%s 删除了栏目 <a href='https://%s%s/channel'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getEntity().getName()));
    }

    /**
     * 通知所有拥有审核权限的用户
     *
     * @param event 文章申请审核事件
     */
    @EventListener
    public void onArticleAuditing(ArticleAuditingEvent event) {
        feedService.feedForUserGroup(PRIVILEGE_NEWS_ARTICLE_AUDIT, event.getTarget(),
                String.format("%s 申请审核文章 <a href='https://%s%s/article/%d/audit'>%s</a>",
                        event.getTarget().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getArticle().getId(),
                        event.getArticle().getTitle()));
    }

    /**
     * 通知文章所有人和文章所属栏目的所有人
     *
     * @param event 文章发布事件
     */
    @EventListener
    public void onArticlePublish(ArticlePublishEvent event) {
        feedService.feedForUsers(getArticleAndChannelUser(event.getArticle()), event.getTarget(),
                String.format("%s 通过并发布了文章 %s", event.getTarget().getDisplayName(), event.getArticle().getTitle()));
    }

    /**
     * 通知文章所有人和文章所属栏目的所有人
     *
     * @param event 文章驳回事件
     */
    @EventListener
    public void onArticleReject(ArticleRejectEvent event) {
        feedService.feedForUsers(getArticleAndChannelUser(event.getArticle()), event.getTarget(),
                String.format("%s 驳回了文章 <a href='https://%s%s/user/article/%d/audit'>%s</a>",
                        event.getTarget().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getArticle().getId(),
                        event.getArticle().getTitle()),
                "驳回原因：" + event.getReason());
    }

    /**
     * 通知文章所有人和文章所属栏目的所有人
     *
     * @param event 文章禁用事件
     */
    @EventListener
    public void onArticleDisable(ArticleDisableEvent event) {
        feedService.feedForUsers(getArticleAndChannelUser(event.getArticle()), event.getTarget(),
                String.format("%s 禁用了文章 <a href='https://%s%s/user/article'>%s</a>",
                        event.getTarget().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getArticle().getTitle()));
    }

    /**
     * 通知文章所有人和文章所属栏目的所有人
     *
     * @param event 文章启用事件
     */
    @EventListener
    public void onArticleEnable(ArticleEnableEvent event) {
        feedService.feedForUsers(getArticleAndChannelUser(event.getArticle()), event.getTarget(),
                String.format("%s 启用了文章 <a href='https://%s%s/user/article'>%s</a>",
                        event.getTarget().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getArticle().getTitle()));
    }

    /**
     * 获取拥有栏目管理权限以及栏目所有用户的用户组
     *
     * @param entity 栏目
     *
     * @return 用户组
     */
    private List<User> getChannelUsers(Channel entity) {
        List<User> users = userService.findByAuthority(PRIVILEGE_NEWS_CHANNEL);
        users.add(entity.getOwner());
        users.addAll(entity.getAdmins());
        return users;
    }

    /**
     * 获取文章所有者以及文章所在栏目的所有用户
     *
     * @param article 文章
     *
     * @return 用户
     */
    private Set<User> getArticleAndChannelUser(Article article) {
        Set<User> users = Sets.newHashSet();
        userService.findOne(article.getUserId()).ifPresent(users::add);
        article.getChannels().forEach(channel -> {
            users.add(channel.getOwner());
            users.addAll(channel.getAdmins());
        });
        return users;
    }
}
