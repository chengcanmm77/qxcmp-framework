package com.qxcmp.link.controller;

import com.qxcmp.link.LinkModule;
import com.qxcmp.link.LinkService;
import com.qxcmp.link.form.AdminLinkEditForm;
import com.qxcmp.link.form.AdminLinkNewForm;
import com.qxcmp.link.page.AdminLinkEditPage;
import com.qxcmp.link.page.AdminLinkNewPage;
import com.qxcmp.link.page.AdminLinkTablePage;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.model.RestfulResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

import static com.qxcmp.link.LinkModule.ADMIN_LINK_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_LINK_URL)
@RequiredArgsConstructor
public class AdminLinkPageController extends QxcmpController {

    private final LinkService linkService;

    @GetMapping("")
    public ModelAndView table(Pageable pageable) {
        return page(AdminLinkTablePage.class, linkService, pageable);
    }

    @GetMapping("/new")
    public ModelAndView newGet(final AdminLinkNewForm form, BindingResult bindingResult) {
        if (StringUtils.isBlank(form.getType())) {
            form.setType(LinkModule.SUPPORT_TYPE.get(0));
        }
        if (StringUtils.isBlank(form.getTarget())) {
            form.setTarget(LinkModule.SUPPORT_TARGET.get(0));
        }
        return entityCreatePage(AdminLinkNewPage.class, form, bindingResult)
                .addObject("selection_items_type", LinkModule.SUPPORT_TYPE)
                .addObject("selection_items_target", LinkModule.SUPPORT_TARGET);
    }

    @PostMapping("/new")
    public ModelAndView newPost(@Valid final AdminLinkNewForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return newGet(form, bindingResult);
        }
        return createEntity(linkService, form);
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editGet(@PathVariable Long id, final AdminLinkEditForm form, BindingResult bindingResult) {
        return entityUpdatePage(AdminLinkEditPage.class, id, linkService, form, bindingResult)
                .addObject("selection_items_type", LinkModule.SUPPORT_TYPE)
                .addObject("selection_items_target", LinkModule.SUPPORT_TARGET);
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editPost(@PathVariable Long id, @Valid final AdminLinkEditForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return editGet(id, form, bindingResult);
        }
        return updateEntity(id, linkService, form);
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> remove(@PathVariable Long id) {
        return deleteEntity("删除链接", id, linkService);
    }
}
