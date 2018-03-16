package com.qxcmp.article.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.article.*;
import com.qxcmp.article.event.AdminNewsArticleDisableEvent;
import com.qxcmp.article.event.AdminNewsArticleEnableEvent;
import com.qxcmp.article.form.AdminNewsUserArticleAuditForm;
import com.qxcmp.article.form.AdminNewsUserArticleEditForm;
import com.qxcmp.article.form.AdminNewsUserArticleNewForm;
import com.qxcmp.article.page.*;
import com.qxcmp.article.support.AdminNewsPageHelper;
import com.qxcmp.audit.ActionException;
import com.qxcmp.user.User;
import com.qxcmp.web.model.RestfulResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_NEWS_URL + "/user/article")
@RequiredArgsConstructor
public class AdminNewsUserArticlePageController extends QxcmpAdminController {

    private final ArticleService articleService;
    private final ChannelService channelService;
    private final AdminNewsPageHelper adminNewsPageHelper;

    @GetMapping("")
    public ModelAndView table(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<Article> articles = articleService.findByUserId(user.getId(), pageable);
        return page(AdminUserArticleTablePage.class, articleService, articles, user);
    }

    @GetMapping("/draft")
    public ModelAndView drage(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.NEW, pageable);
        return page(AdminUserArticleDraftTablePage.class, articleService, articles, user);
    }

    @GetMapping("/auditing")
    public ModelAndView auditing(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.AUDITING, pageable);
        return page(AdminUserArticleAuditingTablePage.class, articleService, articles, user);
    }

    @GetMapping("/rejected")
    public ModelAndView rejected(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.REJECT, pageable);
        return page(AdminUserArticleRejectedTablePage.class, articleService, articles, user);
    }

    @GetMapping("/published")
    public ModelAndView published(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.PUBLISHED, pageable);
        return page(AdminUserArticlePublishedTablePage.class, articleService, articles, user);
    }

    @GetMapping("/disabled")
    public ModelAndView disabled(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<Article> articles = articleService.findByUserIdAndStatus(user.getId(), ArticleStatus.DISABLED, pageable);
        return page(AdminUserArticleDisabledTablePage.class, articleService, articles, user);
    }

    @GetMapping("/new")
    public ModelAndView newGet(final AdminNewsUserArticleNewForm form, BindingResult bindingResult) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        List<Channel> channels = channelService.findByUser(user);
        if (StringUtils.isBlank(form.getAuthor())) {
            form.setAuthor(user.getDisplayName());
        }
        return entityCreatePage(AdminUserArticleNewPage.class, form, bindingResult)
                .addObject("selection_items_channels", channels);
    }

    @PostMapping("/new")
    public ModelAndView newPost(@Valid final AdminNewsUserArticleNewForm form, BindingResult bindingResult) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        List<Channel> channels = channelService.findByUserId(user);
        if (form.getChannels().stream().anyMatch(channel -> !channels.contains(channel))) {
            bindingResult.rejectValue("channels", "", "不能指定不属于自己管理的栏目");
        }
        if (bindingResult.hasErrors()) {
            return newGet(form, bindingResult);
        }
        return execute("新建文章", context -> {
            try {
                Article next = articleService.next();
                articleService.mergeToEntity(form, next);
                next.setUserId(user.getId());
                articleService.create(next);
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ADMIN_NEWS_URL + "/user/article"));
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editGet(@PathVariable Long id, final AdminNewsUserArticleEditForm form, BindingResult bindingResult) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        List<Channel> channels = channelService.findByUser(user);
        return entityUpdatePage(AdminUserArticleEditPage.class, id, articleService, form, bindingResult)
                .addObject("selection_items_channels", channels);
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editPost(@PathVariable Long id, final AdminNewsUserArticleEditForm form, BindingResult bindingResult) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        List<Channel> channels = channelService.findByUserId(user);
        if (form.getChannels().stream().anyMatch(channel -> !channels.contains(channel))) {
            bindingResult.rejectValue("channels", "", "不能指定不属于自己管理的栏目");
        }
        if (bindingResult.hasErrors()) {
            return editGet(id, form, bindingResult);
        }
        ModelAndView modelAndView = updateEntity(id, articleService, form);
        articleService.update(id, article -> article.setDateModified(DateTime.now().toDate()));
        return modelAndView;
    }

    @GetMapping("/{id}/preview")
    public ModelAndView preview(@PathVariable String id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .map(article -> page(AdminUserArticleDetailsPage.class, articleService, user, article))
                .orElse(overviewPage(viewHelper.nextWarningOverview("文章不存在").addLink("返回", ADMIN_NEWS_URL + "/user/article")));
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> delete(@PathVariable String id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> !article.getStatus().equals(ArticleStatus.PUBLISHED))
                .map(article -> execute("删除文章", context -> {
                    try {
                        articleService.delete(article);
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @GetMapping("/{id}/audit")
    public ModelAndView auditGet(@PathVariable String id, final AdminNewsUserArticleAuditForm form) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.NEW) || article.getStatus().equals(ArticleStatus.REJECT))
                .map(article -> page(AdminUserArticleAuditPage.class, articleService, user, article, form))
                .orElse(overviewPage(viewHelper.nextWarningOverview("文章不存在").addLink("返回", ADMIN_NEWS_URL + "/user/article")));
    }

    @PostMapping("/{id}/audit")
    public ModelAndView auditPost(@PathVariable String id, final AdminNewsUserArticleAuditForm form) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.NEW) || article.getStatus().equals(ArticleStatus.REJECT))
                .map(article -> execute("申请文章审核", context -> {
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
                                .addLink("我的文章", ADMIN_NEWS_URL + "/user/article")
                                .addLink("草稿箱", ADMIN_NEWS_URL + "/user/article/draft")
                                .addLink("未通过文章", ADMIN_NEWS_URL + "/user/article/rejected")
                ))
                .orElse(overviewPage(viewHelper.nextWarningOverview("文章不存在").addLink("返回", ADMIN_NEWS_URL + "/user/article")));
    }

    @PostMapping("/{id}/repeal")
    public ResponseEntity<RestfulResponse> repeal(@PathVariable String id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                .map(article -> execute("撤销文章审核申请", context -> {
                    try {
                        articleService.update(article.getId(), a -> {
                            a.setStatus(ArticleStatus.NEW);
                            a.setDateModified(new Date());
                        });
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<RestfulResponse> disable(@PathVariable String id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                .map(article -> execute("禁用文章", context -> {
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
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<RestfulResponse> enable(@PathVariable String id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return articleService.findOne(id)
                .filter(article -> StringUtils.equals(article.getUserId(), user.getId()))
                .filter(article -> article.getStatus().equals(ArticleStatus.DISABLED))
                .map(article -> execute("启用文章", context -> {
                    try {
                        articleService.update(article.getId(), a -> {
                            a.setDatePublished(new Date());
                            a.setStatus(ArticleStatus.PUBLISHED);
                        });
                        applicationContext.publishEvent(new AdminNewsArticleEnableEvent(user, article));
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                })).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/remove")
    public ResponseEntity<RestfulResponse> batchDelete(@RequestParam("keys[]") List<String> keys) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return execute("批量删除文章", context -> {
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
    }

    @PostMapping("/audit")
    public ResponseEntity<RestfulResponse> batchAudit(@RequestParam("keys[]") List<String> keys) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return execute("批量申请文章", context -> {
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
    }

    @PostMapping("/disable")
    public ResponseEntity<RestfulResponse> batchDisable(@RequestParam("keys[]") List<String> keys) {
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
