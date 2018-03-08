package com.qxcmp.advertisement.controller;

import com.google.common.collect.ImmutableList;
import com.qxcmp.admin.controller.QxcmpAdminController;
import com.qxcmp.admin.page.GenericAdminFormPage;
import com.qxcmp.advertisement.AdvertisementModule;
import com.qxcmp.advertisement.AdvertisementService;
import com.qxcmp.advertisement.event.AdvertisementEditEvent;
import com.qxcmp.advertisement.form.AdminAdvertisementEditForm;
import com.qxcmp.advertisement.form.AdminAdvertisementNewForm;
import com.qxcmp.advertisement.page.AdminAdvertisementNewPage;
import com.qxcmp.advertisement.page.AdminAdvertisementTablePage;
import com.qxcmp.audit.ActionException;
import com.qxcmp.user.User;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.views.Overview;
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
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

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
    public ModelAndView advertisementEditPage(@PathVariable String id, final AdminAdvertisementEditForm form) {
        return advertisementService.findOne(id).map(advertisement -> {

            advertisementService.mergeToObject(advertisement, form);

            return page(GenericAdminFormPage.class, form, null, ImmutableList.of("控制台", "", "系统工具", "tools", "广告管理", "advertisement", "编辑广告"))
                    .addObject("selection_items_type", AdvertisementModule.SUPPORT_TYPES);
        }).orElse(page(new Overview(new IconHeader("广告不存在", new Icon("warning circle"))).addLink("返回", QXCMP_ADMIN_URL + "/advertisement")).build());
    }

    @PostMapping("/{id}/edit")
    public ModelAndView advertisementEditPage(@PathVariable String id, @Valid final AdminAdvertisementEditForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        if (bindingResult.hasErrors()) {
            return page().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                    .setBreadcrumb("控制台", "", "系统工具", "tools", "广告管理", "advertisement", "新建广告")
                    .addObject("selection_items_type", AdvertisementModule.SUPPORT_TYPES)
                    .build();
        }

        return submitForm(form, context -> {
            try {
                applicationContext.publishEvent(new AdvertisementEditEvent(user, advertisementService.update(Long.parseLong(id), advertisement -> {
                    advertisement.setImage(form.getImage());
                    advertisement.setType(form.getType());
                    advertisement.setTitle(form.getTitle());
                    advertisement.setLink(form.getLink());
                    advertisement.setAdOrder(form.getAdOrder());
                    advertisement.setBlank(form.isBlank());
                })));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回广告列表", QXCMP_ADMIN_URL + "/advertisement"));
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> advertisementRemove(@PathVariable String id) {
        RestfulResponse restfulResponse = audit("删除广告", context -> {
            try {
                advertisementService.deleteById(Long.parseLong(id));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }
}
