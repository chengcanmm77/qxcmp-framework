package com.qxcmp.article.controller;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Article;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.article.form.AdminNewsArticleAuditForm;
import com.qxcmp.article.page.*;
import com.qxcmp.audit.ActionException;
import com.qxcmp.user.User;
import com.qxcmp.web.model.RestfulResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_NEWS_URL + "/article")
@RequiredArgsConstructor
public class AdminNewsArticlePageController extends AbstractNewsPageController {

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
    public ModelAndView auditGet(@PathVariable Long id, final AdminNewsArticleAuditForm form) {
        return articleService.findOne(id)
                .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                .map(article -> page(AdminNewsArticleAuditPage.class, article, form)
                        .addObject("selection_items_operation", ImmutableList.of("通过文章", "驳回文章")))
                .orElse(overviewPage(viewHelper.nextWarningOverview("文章不存在")));
    }

    @PostMapping("/{id}/audit")
    public ModelAndView auditPost(@PathVariable Long id, final AdminNewsArticleAuditForm form) {
        return execute("审核文章", context -> {
            try {
                User user = currentUser().orElseThrow(RuntimeException::new);
                Article article = articleService.findOne(id).orElseThrow(RuntimeException::new);
                if (StringUtils.equals("通过文章", form.getOperation())) {
                    articleService.publish(user, article, form.getResponse());
                } else {
                    articleService.reject(user, article, form.getResponse());
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ADMIN_NEWS_URL + "/article/auditing"));
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> delete(@PathVariable Long id) {
        return deleteArticle(id, true);
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<RestfulResponse> disable(@PathVariable Long id) {
        return disableArticle(id, true);
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<RestfulResponse> enable(@PathVariable Long id) {
        return enableArticle(id, true);
    }

    @PostMapping("/remove")
    public ResponseEntity<RestfulResponse> batchDelete(@RequestParam("keys[]") List<String> keys) {
        return batchDelete(keys, true);
    }

    @PostMapping("/disable")
    public ResponseEntity<RestfulResponse> batchDisable(@RequestParam("keys[]") List<String> keys) {
        return batchDisable(keys, true);
    }

    @PostMapping("/publish")
    public ResponseEntity<RestfulResponse> batchPublish(@RequestParam("keys[]") List<String> keys) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return execute("批量发布文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(Long.parseLong(key))
                            .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                            .ifPresent(article -> articleService.publish(user, article, "批量发布"));
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @PostMapping("/reject")
    public ResponseEntity<RestfulResponse> batchReject(@RequestParam("keys[]") List<String> keys) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return execute("批量驳回文章", context -> {
            try {
                for (String key : keys) {
                    articleService.findOne(Long.parseLong(key))
                            .filter(article -> article.getStatus().equals(ArticleStatus.AUDITING))
                            .ifPresent(article -> articleService.reject(user, article, "批量驳回"));
                }
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }
}
