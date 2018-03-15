package com.qxcmp.article;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.core.navigation.Navigation;
import com.qxcmp.core.navigation.NavigationLoader;
import com.qxcmp.core.navigation.NavigationService;
import com.qxcmp.web.view.elements.icon.Icon;
import org.springframework.stereotype.Component;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.NAVIGATION_ADMIN_SIDEBAR;
import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleSecurity.*;

/**
 * @author Aaric
 */
@Component
public class NewsModuleNavigation implements NavigationLoader {

    public static final String NAVIGATION_ADMIN_SIDEBAR_ARTICLE = NAVIGATION_ADMIN_SIDEBAR + "-ARTICLE";

    public static final String ADMIN_MENU_ARTICLE = "ADMIN-ARTICLE";
    public static final String ADMIN_MENU_ARTICLE_USER_CHANNEL = ADMIN_MENU_ARTICLE + "-USER-CHANNEL";
    public static final String ADMIN_MENU_ARTICLE_USER_ARTICLE = ADMIN_MENU_ARTICLE + "-USER-ARTICLE";
    public static final String ADMIN_MENU_ARTICLE_AUDITING = ADMIN_MENU_ARTICLE + "-ARTICLE-AUDITING";
    public static final String ADMIN_MENU_ARTICLE_PUBLISHED = ADMIN_MENU_ARTICLE + "-ARTICLE-PUBLISHED";
    public static final String ADMIN_MENU_ARTICLE_DISABLED = ADMIN_MENU_ARTICLE + "-ARTICLE-DISABLED";
    public static final String ADMIN_MENU_ARTICLE_CHANNEL = ADMIN_MENU_ARTICLE + "-CHANNEL";

    public static final String ADMIN_MENU_USER_ARTICLE = "ADMIN-ARTICLE-USER-ARTICLE";
    public static final String ADMIN_MENU_USER_ARTICLE_DRAFT = ADMIN_MENU_USER_ARTICLE + "-DRAFT";
    public static final String ADMIN_MENU_USER_ARTICLE_AUDITING = ADMIN_MENU_USER_ARTICLE + "-AUDITING";
    public static final String ADMIN_MENU_USER_ARTICLE_REJECTED = ADMIN_MENU_USER_ARTICLE + "-REJECTED";
    public static final String ADMIN_MENU_USER_ARTICLE_PUBLISHED = ADMIN_MENU_USER_ARTICLE + "-PUBLISHED";
    public static final String ADMIN_MENU_USER_ARTICLE_DISABLED = ADMIN_MENU_USER_ARTICLE + "-DISABLED";

    @Override
    public void configNavigation(NavigationService navigationService) {
        navigationService.get(NAVIGATION_ADMIN_SIDEBAR)
                .addItem(new Navigation(NAVIGATION_ADMIN_SIDEBAR_ARTICLE, "新闻管理", ADMIN_NEWS_URL).setIcon(new Icon("newspaper")).setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)));
        navigationService.add(new Navigation(ADMIN_MENU_ARTICLE, "新闻管理导航栏")
                .addItem(new Navigation(ADMIN_MENU_ARTICLE_USER_ARTICLE, "我的文章", ADMIN_NEWS_URL + "/user/article").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(ADMIN_MENU_ARTICLE_USER_CHANNEL, "我的栏目", ADMIN_NEWS_URL + "/user/channel").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(ADMIN_MENU_ARTICLE_AUDITING, "待审核文章", ADMIN_NEWS_URL + "/article/auditing").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_AUDIT)))
                .addItem(new Navigation(ADMIN_MENU_ARTICLE_PUBLISHED, "已发布文章", ADMIN_NEWS_URL + "/article/published").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)))
                .addItem(new Navigation(ADMIN_MENU_ARTICLE_DISABLED, "已禁用文章", ADMIN_NEWS_URL + "/article/disabled").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)))
                .addItem(new Navigation(ADMIN_MENU_ARTICLE_CHANNEL, "栏目管理", ADMIN_NEWS_URL + "/channel").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS_CHANNEL)))
        );
        navigationService.add(new Navigation(ADMIN_MENU_USER_ARTICLE, "我的文章导航栏")
                .addItem(new Navigation(ADMIN_MENU_USER_ARTICLE_DRAFT, "草稿箱", ADMIN_NEWS_URL + "/user/article/draft").setOrder(10).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(ADMIN_MENU_USER_ARTICLE_AUDITING, "审核中", ADMIN_NEWS_URL + "/user/article/auditing").setOrder(20).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(ADMIN_MENU_USER_ARTICLE_REJECTED, "未通过", ADMIN_NEWS_URL + "/user/article/rejected").setOrder(30).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(ADMIN_MENU_USER_ARTICLE_PUBLISHED, "已发布", ADMIN_NEWS_URL + "/user/article/published").setOrder(40).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
                .addItem(new Navigation(ADMIN_MENU_USER_ARTICLE_DISABLED, "已禁用", ADMIN_NEWS_URL + "/user/article/disabled").setOrder(50).setPrivilegesAnd(ImmutableSet.of(PRIVILEGE_NEWS)))
        );
    }
}
