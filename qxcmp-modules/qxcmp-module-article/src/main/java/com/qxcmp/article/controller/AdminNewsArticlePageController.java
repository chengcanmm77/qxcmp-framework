package com.qxcmp.article.controller;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Article;
import com.qxcmp.article.ArticleService;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.article.event.AdminNewsArticleDisableEvent;
import com.qxcmp.article.event.AdminNewsArticleEnableEvent;
import com.qxcmp.article.event.AdminNewsArticlePublishEvent;
import com.qxcmp.article.form.AdminNewsArticleAuditForm;
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
import com.qxcmp.web.view.support.Alignment;
import com.qxcmp.web.view.support.Wide;
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

import java.util.Date;
import java.util.List;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/news/article")
@RequiredArgsConstructor
public class AdminNewsArticlePageController extends QxcmpController {

    private final ArticleService articleService;
    private final AdminNewsPageHelper adminNewsPageHelper;

    @GetMapping("")
    public ModelAndView articlePage() {

        Long auditingArticleCount = articleService.count((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), ArticleStatus.AUDITING));
        Long publishedArticleCount = articleService.count((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED));
        Long disabledArticleCount = articleService.count((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), ArticleStatus.DISABLED));

        return calculateBadge(page().addComponent(new Overview("文章管理")
                .addComponent(convertToTable(stringObjectMap -> {
                    stringObjectMap.put("待审核文章数量", auditingArticleCount);
                    stringObjectMap.put("已发布文章数量", publishedArticleCount);
                    stringObjectMap.put("已禁用文章数量", disabledArticleCount);
                })))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "文章管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT, ""))
                .build();
    }

    @GetMapping("/auditing")
    public ModelAndView articleAuditingPage(Pageable pageable) {

        Page<Article> articles = articleService.findByStatus(ArticleStatus.AUDITING, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateAuditing"));

        return calculateBadge(page().addComponent(convertToTable("auditing", Article.class, articles))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "文章管理", "news/article", "待审核文章")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_AUDITING))
                .build();
    }

    @GetMapping("/published")
    public ModelAndView articlePublishedPage(Pageable pageable) {

        Page<Article> articles = articleService.findByStatus(ArticleStatus.PUBLISHED, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "datePublished"));

        return calculateBadge(page().addComponent(convertToTable("published", Article.class, articles))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "文章管理", "news/article", "已发布文章")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_PUBLISHED))
                .build();
    }

    @GetMapping("/disabled")
    public ModelAndView articleDisabledPage(Pageable pageable) {

        Page<Article> articles = articleService.findByStatus(ArticleStatus.DISABLED, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateDisabled"));

        return calculateBadge(page().addComponent(convertToTable("disabled", Article.class, articles))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "文章管理", "news/article", "已禁用文章")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_DISABLED))
                .build();
    }

    @GetMapping("/{id}/preview")
    public ModelAndView userArticlePreviewPage(@PathVariable String id) {
        return articleService.findOne(id)
                .map(article -> calculateBadge(page().addComponent(new Overview(article.getTitle(), article.getAuthor()).setAlignment(Alignment.CENTER)
                        .addComponent(getArticlePreviewContent(article))
                        .addLink("返回", QXCMP_BACKEND_URL + "/news/article"))
                        .setBreadcrumb("控制台", "", "新闻管理", "news", "文章管理", "news/article", "查看文章")
                        .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT, ""))
                        .build())
                .orElse(page(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/article")).build());
    }

    @GetMapping("/{id}/audit")
    public ModelAndView articleAuditPage(@PathVariable String id, final AdminNewsArticleAuditForm form) {
        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                .map(article -> {
                    form.setOperation("通过文章");
                    return calculateBadge((BackendPage) page().addComponent(new Overview(article.getTitle(), article.getAuthor()).setAlignment(Alignment.CENTER)
                            .addComponent(getArticleAuditContent(article, form))
                            .addLink("返回", QXCMP_BACKEND_URL + "/news/article/auditing"))
                            .setBreadcrumb("控制台", "", "新闻管理", "news", "文章管理", "news/article", "审核文章")
                            .setVerticalNavigation(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT, NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_AUDITING)
                            .addObject("selection_items_operation", ImmutableList.of("通过文章", "驳回文章")))
                            .build();
                })
                .orElse(page(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/article/auditing")).build());
    }

    @PostMapping("/{id}/audit")
    public ModelAndView articleAuditPage(@PathVariable String id, final AdminNewsArticleAuditForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                .map(article -> submitForm("审核文章", form, context -> {
                    try {
                        if (StringUtils.equals("通过文章", form.getOperation())) {
                            applicationContext.publishEvent(new AdminNewsArticlePublishEvent(user, articleService.update(article.getId(), a -> {
                                a.setAuditor(user.getId());
                                a.setAuditResponse(form.getResponse());
                                a.setDatePublished(new Date());
                                a.setStatus(ArticleStatus.PUBLISHED);
                            })));
                        } else {
                            articleService.update(article.getId(), a -> {
                                a.setAuditor(user.getId());
                                a.setAuditResponse(form.getResponse());
                                a.setDateRejected(new Date());
                                a.setStatus(ArticleStatus.REJECT);
                            });
                        }
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/news/article/auditing")))
                .orElse(page(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/article/auditing")).build());
    }


    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> articleRemove(@PathVariable String id) {
        return articleService.findOne(id)
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

    @PostMapping("/{id}/disable")
    public ResponseEntity<RestfulResponse> articleDisable(@PathVariable String id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                .map(article -> {
                    RestfulResponse restfulResponse = audit("禁用文章", context -> {
                        try {
                            applicationContext.publishEvent(new AdminNewsArticleDisableEvent(user, articleService.update(article.getId(), a -> {
                                a.setDatePublished(new Date());
                                a.setStatus(ArticleStatus.DISABLED);
                                a.setDisableUser(user.getId());
                            })));
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<RestfulResponse> articleEnable(@PathVariable String id) {

        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.DISABLED))
                .map(article -> {
                    RestfulResponse restfulResponse = audit("启用文章", context -> {
                        try {
                            applicationContext.publishEvent(new AdminNewsArticleEnableEvent(currentUser().orElseThrow(RuntimeException::new), articleService.update(article.getId(), a -> {
                                a.setDatePublished(new Date());
                                a.setStatus(ArticleStatus.PUBLISHED);
                            })));
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    });
                    return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }


    @PostMapping("/publish")
    public ResponseEntity<RestfulResponse> articleBatchPublish(@RequestParam("keys[]") List<String> keys) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        RestfulResponse restfulResponse = audit("批量发布文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(key)
                            .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                            .ifPresent(article -> {
                                articleService.update(article.getId(), a -> {
                                    a.setAuditor(user.getId());
                                    a.setAuditResponse("批量发布");
                                    a.setDatePublished(new Date());
                                    a.setStatus(ArticleStatus.PUBLISHED);
                                });
                                applicationContext.publishEvent(new AdminNewsArticlePublishEvent(user, article));
                            });
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }


    @PostMapping("/reject")
    public ResponseEntity<RestfulResponse> userArticleBatchReject(@RequestParam("keys[]") List<String> keys) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        RestfulResponse restfulResponse = audit("批量驳回文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(key)
                            .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                            .ifPresent(article -> articleService.update(article.getId(), a -> {
                                a.setAuditor(user.getId());
                                a.setAuditResponse("批量驳回");
                                a.setDateRejected(new Date());
                                a.setStatus(ArticleStatus.REJECT);
                            }));
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @PostMapping("/remove")
    public ResponseEntity<RestfulResponse> userArticleBatchRemove(@RequestParam("keys[]") List<String> keys) {

        RestfulResponse restfulResponse = audit("批量删除文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(key)
                            .filter(article -> !article.getStatus().equals(ArticleStatus.PUBLISHED))
                            .ifPresent(articleService::delete);
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

    private Component getArticleAuditContent(Article article, AdminNewsArticleAuditForm form) {
        final AbstractGrid grid = new VerticallyDividedGrid().setVerticallyPadded();
        grid.addItem(new Row()
                .addCol(new Col().setComputerWide(Wide.FOUR).setMobileWide(Wide.SIXTEEN).addComponent(new Image(article.getCover()).setCentered().setBordered().setRounded()))
                .addCol(new Col().setComputerWide(Wide.TWELVE).setMobileWide(Wide.SIXTEEN).addComponent(convertToTable(adminNewsPageHelper.getArticleInfoTable(article))).addComponent(convertToForm(form)))
        );
        grid.addItem(new Row().addCol(new Col().setGeneralWide(Wide.SIXTEEN).addComponent(new HtmlText(article.getContent()))));
        return grid;
    }

    private AbstractPage calculateBadge(AbstractPage backendPage) {

        final int MAX_COUNT = 100;

        long auditingCount = articleService.findByStatus(ArticleStatus.AUDITING, new PageRequest(0, MAX_COUNT)).getTotalElements();
        long publishCount = articleService.findByStatus(ArticleStatus.PUBLISHED, new PageRequest(0, MAX_COUNT)).getTotalElements();
        long disableCount = articleService.findByStatus(ArticleStatus.DISABLED, new PageRequest(0, MAX_COUNT)).getTotalElements();

        if (auditingCount > 0) {
            if (auditingCount < 100) {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_AUDITING, String.valueOf(auditingCount));
            } else {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_AUDITING, "99+");
            }
        }

        if (publishCount > 0) {
            if (publishCount < 100) {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_PUBLISHED, String.valueOf(publishCount));
            } else {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_PUBLISHED, "99+");
            }
        }

        if (disableCount > 0) {
            if (disableCount < 100) {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_DISABLED, String.valueOf(disableCount));
            } else {
                backendPage.setVerticalNavigationBadge(NAVIGATION_ADMIN_NEWS_ARTICLE_MANAGEMENT_DISABLED, "99+");
            }
        }

        return backendPage;
    }
}
