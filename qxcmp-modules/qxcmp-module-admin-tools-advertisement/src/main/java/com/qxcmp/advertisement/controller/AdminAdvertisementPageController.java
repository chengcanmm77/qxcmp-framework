package com.qxcmp.advertisement.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.advertisement.AdvertisementModule;
import com.qxcmp.advertisement.AdvertisementService;
import com.qxcmp.advertisement.form.AdminAdvertisementEditForm;
import com.qxcmp.advertisement.form.AdminAdvertisementNewForm;
import com.qxcmp.advertisement.page.AdminAdvertisementEditPage;
import com.qxcmp.advertisement.page.AdminAdvertisementNewPage;
import com.qxcmp.advertisement.page.AdminAdvertisementTablePage;
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

import static com.qxcmp.advertisement.AdvertisementModule.ADMIN_ADVERTISEMENT_URL;

/**
 * 广告后台管理页面
 *
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_ADVERTISEMENT_URL)
@RequiredArgsConstructor
public class AdminAdvertisementPageController extends QxcmpAdminController {

    private final AdvertisementService advertisementService;

    @GetMapping("")
    public ModelAndView table(Pageable pageable) {
        return page(AdminAdvertisementTablePage.class, advertisementService, pageable);
    }

    @GetMapping("/new")
    public ModelAndView newGet(final AdminAdvertisementNewForm form, BindingResult bindingResult) {
        return page(AdminAdvertisementNewPage.class, form, bindingResult).addObject("selection_items_type", AdvertisementModule.SUPPORT_TYPES);
    }

    @PostMapping("/new")
    public ModelAndView newPost(@Valid final AdminAdvertisementNewForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return newGet(form, bindingResult);
        }
        return createEntity(advertisementService, form);
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editPageGet(@PathVariable Long id, final AdminAdvertisementEditForm form, BindingResult bindingResult) {
        return entityUpdatePage(AdminAdvertisementEditPage.class, id, advertisementService, form, bindingResult).addObject("selection_items_type", AdvertisementModule.SUPPORT_TYPES);
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editPagePost(@PathVariable Long id, @Valid final AdminAdvertisementEditForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return editPageGet(id, form, bindingResult);
        }
        return updateEntity(id, advertisementService, form);
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> advertisementRemove(@PathVariable Long id) {
        return deleteEntity("删除广告", id, advertisementService);
    }
}
