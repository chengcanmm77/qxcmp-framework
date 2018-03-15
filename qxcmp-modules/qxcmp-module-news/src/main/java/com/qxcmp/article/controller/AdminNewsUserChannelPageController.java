package com.qxcmp.article.controller;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.article.*;
import com.qxcmp.article.form.AdminNewsUserChannelAdminEditForm;
import com.qxcmp.article.form.AdminNewsUserChannelOwnerEditForm;
import com.qxcmp.article.page.AdminUserChannelDetailsPage;
import com.qxcmp.article.page.AdminUserChannelEditPage;
import com.qxcmp.article.page.AdminUserChannelTablePage;
import com.qxcmp.article.support.AdminNewsPageHelper;
import com.qxcmp.audit.ActionException;
import com.qxcmp.user.User;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.elements.grid.AbstractGrid;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.html.HtmlText;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Wide;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_NEWS;
import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_NEWS_USER_CHANNEL;
import static com.qxcmp.article.NewsModuleSecurity.PRIVILEGE_NEWS;
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_NEWS_URL + "/user/channel")
@RequiredArgsConstructor
public class AdminNewsUserChannelPageController extends QxcmpAdminController {

    private final ChannelService channelService;
    private final ArticleService articleService;
    private final AdminNewsPageHelper adminNewsPageHelper;

    @GetMapping("")
    public ModelAndView userChannelPage(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<Channel> channels = channelService.findByUser(user, pageable);
        return page(AdminUserChannelTablePage.class, channels);
    }

    @GetMapping("/{id}/details")
    public ModelAndView userChannelDetailsPage(@PathVariable Long id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(id)
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> entityDetailsPage(AdminUserChannelDetailsPage.class, id, channelService))
                .orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @GetMapping("/{id}/edit")
    public ModelAndView userChannelEditPage(@PathVariable Long id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(id)
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> {
                    Object form;
                    if (StringUtils.equals(channel.getOwner().getId(), user.getId())) {
                        form = new AdminNewsUserChannelOwnerEditForm();
                        channelService.mergeToObject(channel, form);
                    } else {
                        form = new AdminNewsUserChannelAdminEditForm();
                        channelService.mergeToObject(channel, form);
                    }
                    return page(AdminUserChannelEditPage.class, form, null)
                            .addObject(form)
                            .addObject("selection_items_owner", userService.findByAuthority(PRIVILEGE_NEWS))
                            .addObject("selection_items_admins", userService.findByAuthority(PRIVILEGE_NEWS));
                })
                .orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @PostMapping("/owner/edit")
    public ModelAndView userChannelOwnerPage(@Valid final AdminNewsUserChannelOwnerEditForm form) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(form.getId())
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> updateEntity(form.getId(), channelService, form, overview -> overview.addLink("返回", ADMIN_NEWS_URL + "/user/channel")))
                .orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @PostMapping("/admin/edit")
    public ModelAndView userChannelAdminPage(@Valid final AdminNewsUserChannelAdminEditForm form) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(form.getId())
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> updateEntity(form.getId(), channelService, form, overview -> overview.addLink("返回", ADMIN_NEWS_URL + "/user/channel")))
                .orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @GetMapping("/{id}/article")
    public ModelAndView userChannelArticlePage(@PathVariable String id, Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(id)
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> {

                    Page<Article> articles = articleService.findByChannelsAndStatuses(ImmutableSet.of(channel), ImmutableSet.of(ArticleStatus.PUBLISHED, ArticleStatus.DISABLED), pageable);

                    return page().addComponent(convertToTable("userChannel", String.format(QXCMP_ADMIN_URL + "/news/user/channel/%d/article", channel.getId()), Article.class, articles))
                            .setBreadcrumb("控制台", "", "新闻管理", "news", "我的栏目", "news/user/channel", channel.getName())
                            .setVerticalNavigation(ADMIN_MENU_NEWS, ADMIN_MENU_NEWS_USER_CHANNEL)
                            .build();
                }).orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @GetMapping("/{id}/article/{articleId}/preview")
    public ModelAndView userChannelArticlePreviewPage(@PathVariable String id, @PathVariable String articleId) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUser(user);

        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> articleService.findOne(articleId).filter(article -> article.getChannels().contains(channel)).map(article -> page().addComponent(new Overview(article.getTitle(), article.getAuthor()).setAlignment(Alignment.CENTER)
                        .addComponent(getArticlePreviewContent(article))
                        .addLink("我的栏目", QXCMP_ADMIN_URL + "/news/user/channel")
                        .addLink("栏目文章", String.format(QXCMP_ADMIN_URL + "/news/user/channel/%d/article", channel.getId())))
                        .setBreadcrumb("控制台", "", "新闻管理", "news", "我的栏目", "news/user/channel", channel.getName(), String.format("news/user/channel/%d/article", channel.getId()), "文章预览")
                        .setVerticalNavigation(ADMIN_MENU_NEWS, ADMIN_MENU_NEWS_USER_CHANNEL)
                        .build()).orElse(page(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_ADMIN_URL + "/news/user/channel")).build())
                ).orElse(page(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_ADMIN_URL + "/news/user/channel")).build());
    }

    @PostMapping("/{id}/article/{articleId}/disable")
    public ResponseEntity<RestfulResponse> userChannelArticleDisable(@PathVariable String id, @PathVariable String articleId) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUser(user);

        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> articleService.findOne(articleId).filter(article -> article.getChannels().contains(channel))
                        .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                        .map(article -> {
                            RestfulResponse restfulResponse = audit("禁用栏目文章", context -> {
                                try {
                                    articleService.update(article.getId(), a -> {
                                        a.setDatePublished(new Date());
                                        a.setStatus(ArticleStatus.DISABLED);
                                        a.setDisableUser(user.getId());
                                    });
                                } catch (Exception e) {
                                    throw new ActionException(e.getMessage(), e);
                                }
                            });
                            return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/{id}/article/{articleId}/enable")
    public ResponseEntity<RestfulResponse> userChannelArticleEnable(@PathVariable String id, @PathVariable String articleId) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUser(user);

        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> articleService.findOne(articleId).filter(article -> article.getChannels().contains(channel))
                        .filter(article -> article.getStatus().equals(ArticleStatus.DISABLED))
                        .map(article -> {
                            RestfulResponse restfulResponse = audit("启用栏目文章", context -> {
                                try {
                                    articleService.update(article.getId(), a -> {
                                        a.setDatePublished(new Date());
                                        a.setStatus(ArticleStatus.PUBLISHED);
                                    });
                                } catch (Exception e) {
                                    throw new ActionException(e.getMessage(), e);
                                }
                            });
                            return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    private Component getArticlePreviewContent(Article article) {
        final AbstractGrid grid = new VerticallyDividedGrid().setVerticallyPadded();
        grid.addItem(new Row()
                .addCol(new Col().setComputerWide(Wide.FOUR).setMobileWide(Wide.SIXTEEN).addComponent(new Image(article.getCover()).setCentered().setBordered().setRounded()))
                .addCol(new Col().setComputerWide(Wide.TWELVE).setMobileWide(Wide.SIXTEEN).addComponent(convertToTable(adminNewsPageHelper.getArticleInfoTable(article))))
        );
        grid.addItem(new Row().addCol(new Col().setGeneralWide(Wide.SIXTEEN).addComponent(new HtmlText(article.getContent()))));
        return grid;
    }
}
