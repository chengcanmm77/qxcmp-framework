package com.qxcmp.article.controller;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Article;
import com.qxcmp.article.ArticleService;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.article.event.AdminNewsArticleDisableEvent;
import com.qxcmp.article.event.AdminNewsArticleEnableEvent;
import com.qxcmp.article.event.AdminNewsArticlePublishEvent;
import com.qxcmp.article.form.AdminNewsArticleAuditForm;
import com.qxcmp.article.page.*;
import com.qxcmp.article.support.AdminNewsPageHelper;
import com.qxcmp.audit.ActionException;
import com.qxcmp.user.User;
import com.qxcmp.web.model.RestfulResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_NEWS_URL + "/article")
@RequiredArgsConstructor
public class AdminNewsArticlePageController extends AbstractNewsPageController {

    private final ArticleService articleService;
    private final AdminNewsPageHelper adminNewsPageHelper;

    @GetMapping("/auditing")
    public ModelAndView auditing(Pageable pageable) {
        Page<Article> articles = articleService.findByStatus(ArticleStatus.AUDITING, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateAuditing"));
        return page(AdminNewsArticleAuditingTablePage.class, articleService, articles);
    }

    @GetMapping("/published")
    public ModelAndView published(Pageable pageable) {
        Page<Article> articles = articleService.findByStatus(ArticleStatus.PUBLISHED, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "datePublished"));
        return page(AdminNewsArticlePublishedTablePage.class, articleService, articles);
    }

    @GetMapping("/disabled")
    public ModelAndView disabled(Pageable pageable) {
        Page<Article> articles = articleService.findByStatus(ArticleStatus.DISABLED, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateDisabled"));
        return page(AdminNewsArticleDisabledTablePage.class, articleService, articles);
    }

    @GetMapping("/{id}/preview")
    public ModelAndView preview(@PathVariable Long id) {
        return entityDetailsPage(AdminNewsArticleDetailsPage.class, id, articleService);
    }

    @GetMapping("/{id}/audit")
    public ModelAndView auditGet(@PathVariable String id, final AdminNewsArticleAuditForm form) {
        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                .map(article -> page(AdminNewsArticleAuditPage.class, article, form)
                        .addObject("selection_items_operation", ImmutableList.of("通过文章", "驳回文章")))
                .orElse(overviewPage(viewHelper.nextWarningOverview("文章不存在")));
    }

    @PostMapping("/{id}/audit")
    public ModelAndView auditPost(@PathVariable String id, final AdminNewsArticleAuditForm form) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                .map(article -> execute("审核文章", context -> {
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
                }, (stringObjectMap, overview) -> overview.addLink("返回", ADMIN_NEWS_URL + "/article/auditing")))
                .orElse(overviewPage(viewHelper.nextWarningOverview("文章不存在")));
    }


    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> articleRemove(@PathVariable String id) {
        return articleService.findOne(id)
                .filter(article -> !article.getStatus().equals(ArticleStatus.PUBLISHED))
                .map(article -> execute("删除文章", context -> {
                    try {
                        articleService.delete(article);
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<RestfulResponse> articleDisable(@PathVariable String id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                .map(article -> execute("禁用文章", context -> {
                    try {
                        applicationContext.publishEvent(new AdminNewsArticleDisableEvent(user, articleService.update(article.getId(), a -> {
                            a.setDatePublished(new Date());
                            a.setStatus(ArticleStatus.DISABLED);
                            a.setDisableUser(user.getId());
                        })));
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<RestfulResponse> articleEnable(@PathVariable String id) {
        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.DISABLED))
                .map(article -> execute("启用文章", context -> {
                    try {
                        applicationContext.publishEvent(new AdminNewsArticleEnableEvent(currentUser().orElseThrow(RuntimeException::new), articleService.update(article.getId(), a -> {
                            a.setDatePublished(new Date());
                            a.setStatus(ArticleStatus.PUBLISHED);
                        })));
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }


    @PostMapping("/publish")
    public ResponseEntity<RestfulResponse> articleBatchPublish(@RequestParam("keys[]") List<String> keys) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return execute("批量发布文章", context -> {
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
    }


    @PostMapping("/reject")
    public ResponseEntity<RestfulResponse> userArticleBatchReject(@RequestParam("keys[]") List<String> keys) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return execute("批量驳回文章", context -> {
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
    }

    @PostMapping("/remove")
    public ResponseEntity<RestfulResponse> userArticleBatchRemove(@RequestParam("keys[]") List<String> keys) {
        return execute("批量删除文章", context -> {
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
    }

    @PostMapping("/disable")
    public ResponseEntity<RestfulResponse> userArticleBatchDisable(@RequestParam("keys[]") List<String> keys) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return execute("批量禁用文章", context -> {
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
    }
}
