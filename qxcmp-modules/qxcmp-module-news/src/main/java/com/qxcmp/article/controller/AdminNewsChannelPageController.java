package com.qxcmp.article.controller;

import com.qxcmp.article.form.AdminNewsChannelEditForm;
import com.qxcmp.article.form.AdminNewsChannelNewForm;
import com.qxcmp.article.page.AdminNewsChannelDetailsPage;
import com.qxcmp.article.page.AdminNewsChannelEditPage;
import com.qxcmp.article.page.AdminNewsChannelNewPage;
import com.qxcmp.article.page.AdminNewsChannelTablePage;
import com.qxcmp.web.model.RestfulResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleSecurity.PRIVILEGE_NEWS;
import static com.qxcmp.core.QxcmpSystemConfig.ARTICLE_CHANNEL_CATALOG;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_NEWS_URL + "/channel")
@RequiredArgsConstructor
public class AdminNewsChannelPageController extends AbstractNewsPageController {

    @GetMapping("")
    public ModelAndView table(Pageable pageable) {
        return page(AdminNewsChannelTablePage.class, channelService, pageable);
    }

    @GetMapping("/new")
    public ModelAndView newGet(final AdminNewsChannelNewForm form, BindingResult bindingResult) {
        if (Objects.isNull(form.getOwner())) {
            currentUser().ifPresent(form::setOwner);
        }
        return page(AdminNewsChannelNewPage.class, form, bindingResult)
                .addObject("selection_items_owner", userService.findByAuthority(PRIVILEGE_NEWS))
                .addObject("selection_items_admins", userService.findByAuthority(PRIVILEGE_NEWS))
                .addObject("selection_items_catalogs", systemConfigService.getList(ARTICLE_CHANNEL_CATALOG));
    }

    @PostMapping("/new")
    public ModelAndView newPost(@Valid final AdminNewsChannelNewForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return newGet(form, bindingResult);
        }
        return createEntity(channelService, form, overview -> {
            overview.addLink("继续新建", "");
            overview.addLink("返回", ADMIN_NEWS_URL + "/channel");
        });
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editGet(@PathVariable Long id, @Valid final AdminNewsChannelEditForm form, BindingResult bindingResult) {
        return entityUpdatePage(AdminNewsChannelEditPage.class, id, channelService, form, bindingResult)
                .addObject("selection_items_owner", userService.findByAuthority(PRIVILEGE_NEWS))
                .addObject("selection_items_admins", userService.findByAuthority(PRIVILEGE_NEWS))
                .addObject("selection_items_catalogs", systemConfigService.getList(ARTICLE_CHANNEL_CATALOG));
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editPost(@PathVariable Long id, final AdminNewsChannelEditForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return editGet(id, form, bindingResult);
        }
        return updateEntity(id, channelService, form);
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> newsChannelRemove(@PathVariable Long id) {
        return deleteEntity("删除栏目", id, channelService);
    }

    @GetMapping("/{id}/preview")
    public ModelAndView newsChannelPreviewPage(@PathVariable Long id) {
        return entityDetailsPage(AdminNewsChannelDetailsPage.class, id, channelService);
    }
}
