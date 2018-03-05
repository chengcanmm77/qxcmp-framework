package com.qxcmp.article.controller;

import com.qxcmp.article.*;
import com.qxcmp.article.event.AdminNewsArticleDisableEvent;
import com.qxcmp.article.event.AdminNewsArticleEnableEvent;
import com.qxcmp.article.form.AdminNewsUserArticleAuditForm;
import com.qxcmp.article.form.AdminNewsUserArticleEditForm;
import com.qxcmp.article.form.AdminNewsUserArticleNewForm;
import com.qxcmp.article.support.AdminNewsPageHelper;
import com.qxcmp.audit.ActionException;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.page.AbstractPage;
import com.qxcmp.web.page.BackendPage;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.elements.grid.AbstractGrid;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Row;
import com.qxcmp.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.html.HtmlText;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.image.Image;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Wide;
import com.qxcmp.web.view.support.utils.TableHelper;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/news/user/article")
@RequiredArgsConstructor
public class AdminNewsUserArticlePageController extends QxcmpController {

    private final ArticleService articleService;
    private final ChannelService channelService;
    private final TableHelper tableHelper;
    private final AdminNewsPageHelper adminNewsPageHelper;

    @GetMapping("")
    public ModelAndView userArticlePage(Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        Page<Article> articles = articleService.findByUserId(user.getId(), new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateModified"));

        return calculateBadge(page().addComponent(convertToTable("user", Article.class, articles))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, ""), user)
                .build();
    }

    @GetMapping("/draft")
    public ModelAndView userArticleDraftPage(Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.NEW, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateModified"));

        return calculateBadge(page().addComponent(convertToTable("userDraft", Article.class, articles))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "草稿箱")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT), user)
                .build();
    }

    @GetMapping("/auditing")
    public ModelAndView userArticleAuditingPage(Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.AUDITING, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateAuditing"));

        return calculateBadge(page().addComponent(convertToTable("userAuditing", Article.class, articles))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "审核中文章")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_AUDITING), user)
                .build();
    }

    @GetMapping("/rejected")
    public ModelAndView userArticleRejectedPage(Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.REJECT, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateRejected"));

        return calculateBadge(page().addComponent(convertToTable("userRejected", Article.class, articles))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "未通过文章")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_REJECTED), user)
                .build();
    }

    @GetMapping("/published")
    public ModelAndView userArticlePublishedPage(Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.PUBLISHED, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "datePublished"));

        return calculateBadge(page().addComponent(convertToTable("userPublished", Article.class, articles))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "已发布文章")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_PUBLISHED), user)
                .build();
    }

    @GetMapping("/disabled")
    public ModelAndView userArticleDisabledPage(Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.DISABLED, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateDisabled"));

        return calculateBadge(page().addComponent(convertToTable("userDisabled", Article.class, articles))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "已禁用文章")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DISABLED), user)
                .build();
    }

    @GetMapping("/new")
    public ModelAndView userArticleNewPage(final AdminNewsUserArticleNewForm form) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        form.setAuthor(user.getDisplayName());

        return calculateBadge((BackendPage) page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "新建文章")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT)
                .addObject("selection_items_channels", channelService.findByUserId(user)), user)
                .build();
    }

    @PostMapping("/new")
    public ModelAndView userArticleNewPage(@Valid final AdminNewsUserArticleNewForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUserId(user);

        if (form.getChannels().stream().anyMatch(channel -> !channels.contains(channel))) {
            bindingResult.rejectValue("channels", "", "不能指定不属于自己管理的栏目");
        }

        if (bindingResult.hasErrors()) {
            return calculateBadge((BackendPage) page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "新建文章")
                    .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT)
                    .addObject("selection_items_channels", channels), user)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                Article article = articleService.next();
                article.setUserId(user.getId());
                article.setStatus(ArticleStatus.NEW);
                article.setCover(form.getCover());
                article.setTitle(form.getTitle());
                article.setAuthor(form.getAuthor());
                article.setDigest(form.getDigest());
                article.setChannels(form.getChannels());
                article.setContent(form.getContent());
                article.setContentQuill(form.getContentQuill());
                Article article1 = articleService.create(() -> article);
                context.put("article", article1);
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (context, overview) -> {
            Article article = (Article) context.get("article");
            overview
                    .addLink("我的文章", QXCMP_BACKEND_URL + "/news/user/article")
                    .addLink("草稿箱", QXCMP_BACKEND_URL + "/news/user/article/draft")
                    .addLink("预览文章", QXCMP_BACKEND_URL + "/news/user/article/" + article.getId() + "/preview")
                    .addLink("继续新建文章", QXCMP_BACKEND_URL + "/news/user/article/new");
        });
    }

    @GetMapping("/{id}/edit")
    public ModelAndView userArticleEditPage(@PathVariable String id, final AdminNewsUserArticleEditForm form) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.NEW) || article.getStatus().equals(ArticleStatus.REJECT))
                .map(article -> {

                    form.setCover(article.getCover());
                    form.setTitle(article.getTitle());
                    form.setAuthor(article.getAuthor());
                    form.setDigest(article.getDigest());
                    form.setChannels(article.getChannels());
                    form.setContent(article.getContent());
                    form.setContentQuill(article.getContentQuill());

                    return calculateBadge((BackendPage) page().addComponent(new Segment().addComponent(convertToForm(form)))
                            .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "编辑文章")
                            .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT)
                            .addObject("selection_items_channels", channelService.findByUserId(user)), user)
                            .build();
                }).orElse(page(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/article")).build());
    }

    @PostMapping("/{id}/edit")
    public ModelAndView userArticleEditPage(@PathVariable String id, final AdminNewsUserArticleEditForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUserId(user);

        if (form.getChannels().stream().anyMatch(channel -> !channels.contains(channel))) {
            bindingResult.rejectValue("channels", "", "不能指定不属于自己管理的栏目");
        }

        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.NEW) || article.getStatus().equals(ArticleStatus.REJECT))
                .map(article -> {

                    if (bindingResult.hasErrors()) {
                        return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "编辑文章")
                                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT)
                                .addObject("selection_items_channels", channels)
                                .build();
                    }

                    return submitForm(form, context -> {
                        try {
                            articleService.update(article.getId(), a -> {
                                a.setCover(form.getCover());
                                a.setTitle(form.getTitle());
                                a.setAuthor(form.getAuthor());
                                a.setDigest(form.getDigest());
                                a.setChannels(form.getChannels());
                                a.setContent(form.getContent());
                                a.setContentQuill(form.getContentQuill());
                                a.setDateModified(new Date());
                            });
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    }, (context, overview) -> {
                        if (article.getStatus().equals(ArticleStatus.NEW)) {
                            overview
                                    .addLink("我的文章", QXCMP_BACKEND_URL + "/news/user/article")
                                    .addLink("草稿箱", QXCMP_BACKEND_URL + "/news/user/article/draft")
                                    .addLink("预览文章", QXCMP_BACKEND_URL + "/news/user/article/" + article.getId() + "/preview")
                                    .addLink("新建文章", QXCMP_BACKEND_URL + "/news/user/article/new");
                        } else if (article.getStatus().equals(ArticleStatus.REJECT)) {
                            overview.addLink("返回", QXCMP_BACKEND_URL + "/news/user/article/rejected");
                        }
                    });
                }).orElse(page(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/article")).build());
    }

    @GetMapping("/{id}/preview")
    public ModelAndView userArticlePreviewPage(@PathVariable String id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .map(article -> calculateBadge(page().addComponent(new Overview(article.getTitle(), article.getAuthor()).setAlignment(Alignment.CENTER)
                        .addComponent(getArticlePreviewContent(article))
                        .addLink("返回我的文章", QXCMP_BACKEND_URL + "/news/user/article")
                        .addLink("返回草稿箱", QXCMP_BACKEND_URL + "/news/user/article/draft")
                        .addLink("新建文章", QXCMP_BACKEND_URL + "/news/user/article/new"))
                        .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "预览文章")
                        .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, ""), user)
                        .build())
                .orElse(page(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/article")).build());
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> userArticleRemove(@PathVariable String id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> !article.getStatus().equals(ArticleStatus.PUBLISHED))
                .map(article -> {
                    RestfulResponse restfulResponse = audit("删除文章", context -> {
                        try {
                            articleService.delete(article);
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @GetMapping("/{id}/audit")
    public ModelAndView userArticleAuditPage(@PathVariable String id, final AdminNewsUserArticleAuditForm form) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.NEW) || article.getStatus().equals(ArticleStatus.REJECT))
                .map(article -> calculateBadge(page().addComponent(new Overview(article.getTitle(), article.getAuthor()).setAlignment(Alignment.CENTER)
                        .addComponent(getArticleAuditContent(article, form))
                        .addLink("返回我的文章", QXCMP_BACKEND_URL + "/news/user/article")
                        .addLink("返回草稿箱", QXCMP_BACKEND_URL + "/news/user/article/draft")
                        .addLink("新建文章", QXCMP_BACKEND_URL + "/news/user/article/new"))
                        .setBreadcrumb("控制台", "", "新闻管理", "news", "我的文章", "news/user/article", "申请审核")
                        .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT, ""), user)
                        .build())
                .orElse(page(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/article")).build());
    }

    @PostMapping("/{id}/audit")
    public ModelAndView userArticleAuditPage(@PathVariable String id, final AdminNewsUserArticleAuditForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.NEW) || article.getStatus().equals(ArticleStatus.REJECT))
                .map(article -> submitForm("申请文章审核", form, context -> {
                            try {
                                articleService.update(article.getId(), a -> {
                                    a.setAuditRequest(form.getAuditRequest());
                                    a.setDateAuditing(new Date());
                                    a.setStatus(ArticleStatus.AUDITING);
                                });
                            } catch (Exception e) {
                                throw new ActionException(e.getMessage(), e);
                            }
                        }, (stringObjectMap, overview) -> overview
                                .addLink("我的文章", QXCMP_BACKEND_URL + "/news/user/article")
                                .addLink("草稿箱", QXCMP_BACKEND_URL + "/news/user/article/draft")
                                .addLink("未通过文章", QXCMP_BACKEND_URL + "/news/user/article/rejected")
                ))
                .orElse(page(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/article")).build());
    }

    @PostMapping("/{id}/repeal")
    public ResponseEntity<RestfulResponse> userArticleRepeal(@PathVariable String id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                .map(article -> {
                    RestfulResponse restfulResponse = audit("撤销文章审核申请", context -> {
                        try {
                            articleService.update(article.getId(), a -> {
                                a.setStatus(ArticleStatus.NEW);
                                a.setDateModified(new Date());
                            });
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<RestfulResponse> userArticleDisable(@PathVariable String id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                .map(article -> {
                    RestfulResponse restfulResponse = audit("禁用文章", context -> {
                        try {
                            articleService.update(article.getId(), a -> {
                                a.setDatePublished(new Date());
                                a.setStatus(ArticleStatus.DISABLED);
                                a.setDisableUser(user.getId());
                            });

                            applicationContext.publishEvent(new AdminNewsArticleDisableEvent(user, article));
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<RestfulResponse> userArticleEnable(@PathVariable String id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.DISABLED))
                .map(article -> {
                    RestfulResponse restfulResponse = audit("启用文章", context -> {
                        try {
                            articleService.update(article.getId(), a -> {
                                a.setDatePublished(new Date());
                                a.setStatus(ArticleStatus.PUBLISHED);
                            });

                            applicationContext.publishEvent(new AdminNewsArticleEnableEvent(user, article));
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @PostMapping("/remove")
    public ResponseEntity<RestfulResponse> userArticleBatchRemove(@RequestParam("keys[]") List<String> keys) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        RestfulResponse restfulResponse = audit("批量删除文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(key)
                            .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                            .filter(article -> !article.getStatus().equals(ArticleStatus.PUBLISHED))
                            .ifPresent(articleService::delete);
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @PostMapping("/audit")
    public ResponseEntity<RestfulResponse> userArticleBatchAudit(@RequestParam("keys[]") List<String> keys) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        RestfulResponse restfulResponse = audit("批量申请文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(key)
                            .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                            .filter(article -> article.getStatus().equals(ArticleStatus.NEW) || article.getStatus().equals(ArticleStatus.REJECT))
                            .ifPresent(article -> articleService.update(article.getId(), a -> {
                                a.setAuditRequest("批量申请文章");
                                a.setDateAuditing(new Date());
                                a.setStatus(ArticleStatus.AUDITING);
                            }));
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @PostMapping("/disable")
    public ResponseEntity<RestfulResponse> userArticleBatchDisable(@RequestParam("keys[]") List<String> keys) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        RestfulResponse restfulResponse = audit("批量禁用文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(key)
                            .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                            .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                            .ifPresent(article -> {
                                articleService.update(article.getId(), a -> {
                                    a.setDatePublished(new Date());
                                    a.setStatus(ArticleStatus.DISABLED);
                                    a.setDisableUser(user.getId());
                                });

                                applicationContext.publishEvent(new AdminNewsArticleDisableEvent(user, article));
                            });
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
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

    private Component getArticleAuditContent(Article article, AdminNewsUserArticleAuditForm form) {
        final AbstractGrid grid = new VerticallyDividedGrid().setVerticallyPadded();
        grid.addItem(new Row()
                .addCol(new Col().setComputerWide(Wide.FOUR).setMobileWide(Wide.SIXTEEN).addComponent(new Image(article.getCover()).setCentered().setBordered().setRounded()))
                .addCol(new Col().setComputerWide(Wide.TWELVE).setMobileWide(Wide.SIXTEEN).addComponent(convertToTable(adminNewsPageHelper.getArticleInfoTable(article))).addComponent(convertToForm(form)))
        );
        grid.addItem(new Row().addCol(new Col().setGeneralWide(Wide.SIXTEEN).addComponent(new HtmlText(article.getContent()))));
        return grid;
    }


    private AbstractPage calculateBadge(AbstractPage backendPage, User user) {

        final int MAX_COUNT = 100;

        long draftCount = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.NEW, new PageRequest(0, MAX_COUNT)).getTotalElements();
        long auditingCount = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.AUDITING, new PageRequest(0, MAX_COUNT)).getTotalElements();
        long rejectCount = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.REJECT, new PageRequest(0, MAX_COUNT)).getTotalElements();
        long publishCount = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.PUBLISHED, new PageRequest(0, MAX_COUNT)).getTotalElements();
        long disableCount = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.DISABLED, new PageRequest(0, MAX_COUNT)).getTotalElements();

        if (draftCount > 0) {
            if (draftCount < 100) {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT, String.valueOf(draftCount));
            } else {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DRAFT, "99+");
            }
        }

        if (auditingCount > 0) {
            if (auditingCount < 100) {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_AUDITING, String.valueOf(auditingCount));
            } else {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_AUDITING, "99+");
            }
        }

        if (rejectCount > 0) {
            if (rejectCount < 100) {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_REJECTED, String.valueOf(rejectCount));
            } else {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_REJECTED, "99+");
            }
        }

        if (publishCount > 0) {
            if (publishCount < 100) {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_PUBLISHED, String.valueOf(publishCount));
            } else {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_PUBLISHED, "99+");
            }
        }

        if (disableCount > 0) {
            if (disableCount < 100) {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DISABLED, String.valueOf(disableCount));
            } else {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_USER_ARTICLE_MANAGEMENT_DISABLED, "99+");
            }
        }

        return backendPage;
    }
}
