package com.qxcmp.article.listener;

import com.qxcmp.article.Article;
import com.qxcmp.article.Channel;
import com.qxcmp.article.event.AdminNewsArticleDisableEvent;
import com.qxcmp.article.event.AdminNewsArticleEnableEvent;
import com.qxcmp.article.event.AdminNewsArticlePublishEvent;
import com.qxcmp.config.SiteService;
import com.qxcmp.core.entity.EntityCreateEvent;
import com.qxcmp.core.entity.EntityDeleteEvent;
import com.qxcmp.core.entity.EntityUpdateEvent;
import com.qxcmp.message.FeedService;
import com.qxcmp.message.MessageService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleSecurity.PRIVILEGE_NEWS_ARTICLE_MANAGEMENT;
import static com.qxcmp.article.NewsModuleSecurity.PRIVILEGE_NEWS_CHANNEL;


/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminNewsArticleEventListener {

    private final FeedService feedService;
    private final MessageService messageService;
    private final UserService userService;
    private final SiteService siteService;

    @EventListener
    public void onChannelCreate(EntityCreateEvent<Channel> event) {
        feedService.feedForUserGroup(PRIVILEGE_NEWS_CHANNEL, event.getUser(),
                String.format("%s 新建了栏目 <a href='https://%s%s/channel/%d/edit'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getEntity().getId(),
                        event.getEntity().getName()));
    }

    @EventListener
    public void onChannelUpdate(EntityUpdateEvent<Channel> event) {
        feedService.feedForUserGroup(PRIVILEGE_NEWS_CHANNEL, event.getUser(),
                String.format("%s 编辑了栏目 <a href='https://%s%s/channel/%d/edit'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getEntity().getId(),
                        event.getEntity().getName()));
    }

    @EventListener
    public void onChannelDelete(EntityDeleteEvent<Channel> event) {
        feedService.feedForUserGroup(PRIVILEGE_NEWS_CHANNEL, event.getUser(),
                String.format("%s 删除了栏目 <a href='https://%s%s/channel'>%s</a>",
                        event.getUser().getDisplayName(),
                        siteService.getDomain(),
                        ADMIN_NEWS_URL,
                        event.getEntity().getName()));
    }

    @EventListener
    public void onDisableEvent(AdminNewsArticleDisableEvent event) {
        Article article = event.getArticle();
        User user = event.getUser();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT);
        feedUsers.add(user);
        userService.findOne(article.getAuthor()).ifPresent(feedUsers::add);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), user,
                String.format("%s 删除了文章 <a href='https://%s/admin/news/user/article'>%s</a>",
                        user.getDisplayName(),
                        siteService.getDomain(),
                        article.getTitle()));
    }

    @EventListener
    public void onEnableEvent(AdminNewsArticleEnableEvent event) {
        Article article = event.getArticle();
        User user = event.getUser();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT);
        feedUsers.add(user);
        userService.findOne(article.getAuthor()).ifPresent(feedUsers::add);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), user,
                String.format("%s 启用了文章 <a href='https://%s/admin/news/user/article'>%s</a>",
                        user.getDisplayName(),
                        siteService.getDomain(),
                        article.getTitle()));
    }

    @EventListener
    public void onPublishEvent(AdminNewsArticlePublishEvent event) {
        Article article = event.getArticle();
        User user = event.getUser();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT);
        feedUsers.add(user);
        userService.findOne(article.getAuthor()).ifPresent(feedUsers::add);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), user,
                String.format("%s 发布了文章 <a href='https://%s/admin/news/user/article'>%s</a>",
                        user.getDisplayName(),
                        siteService.getDomain(),
                        article.getTitle()));
    }
}
