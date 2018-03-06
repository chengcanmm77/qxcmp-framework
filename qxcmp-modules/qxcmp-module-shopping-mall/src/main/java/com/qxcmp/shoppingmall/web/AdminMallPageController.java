package com.qxcmp.shoppingmall.web;

import com.qxcmp.audit.ActionException;
import com.qxcmp.shoppingmall.Store;
import com.qxcmp.shoppingmall.StoreService;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;
import static com.qxcmp.core.QxcmpSystemConfig.MALL_COMMODITY_CATALOG;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/mall")
@RequiredArgsConstructor
public class AdminMallPageController extends QxcmpController {

    private final StoreService storeService;

    @GetMapping("")
    public ModelAndView mallPage() {
        return page().addComponent(new Overview("商城管理"))
                .setBreadcrumb("控制台", "", "商城管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_MALL, "")
                .build();
    }

    @GetMapping("/store")
    public ModelAndView mallStorePage(Pageable pageable) {
        return page().addComponent(convertToTable("store", new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateModified"), storeService))
                .setBreadcrumb("控制台", "", "商城管理", "mall", "店铺管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_MALL, NAVIGATION_ADMIN_MALL_STORE)
                .build();
    }

    @GetMapping("/store/new")
    public ModelAndView mallStoreNewPage(final AdminMallStoreNewForm form) {
        return page().addComponent(new Segment()
                .addComponent(new P("注：创建好的店铺将无法删除，需要在创建的时候指定店铺所有者，所有者可指定店铺管理员"))
                .addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "商城管理", "mall", "店铺管理", "mall/store", "创建店铺")
                .setVerticalNavigation(NAVIGATION_ADMIN_MALL, NAVIGATION_ADMIN_MALL_STORE)
                .addObject("selection_items_owner", userService.findAll())
                .addObject("selection_items_admins", userService.findAll())
                .build();
    }

    @PostMapping("/store/new")
    public ModelAndView mallStoreNewPage(@Valid final AdminMallStoreNewForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "店铺管理", "mall/store", "创建店铺")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL, NAVIGATION_ADMIN_MALL_STORE)
                    .addObject("selection_items_owner", userService.findAll())
                    .addObject("selection_items_admins", userService.findAll())
                    .build();
        }

        return submitForm(form, context -> {
            try {
                storeService.create(() -> {
                    Store store = storeService.next();
                    store.setCover(form.getCover());
                    store.setName(form.getName());
                    store.setOwner(form.getOwner());
                    store.setDateCreated(new Date());
                    store.setDateModified(new Date());
                    store.setExternal(form.isExternal());
                    return store;
                });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回店铺管理", QXCMP_BACKEND_URL + "/mall/store").addLink("继续新建店铺", QXCMP_BACKEND_URL + "/mall/store/new"));
    }

    @GetMapping("/store/{id}/edit")
    public ModelAndView mallStoreEditPage(@PathVariable String id, final AdminMallStoreEditForm form) {
        return storeService.findOne(id).map(store -> {
            form.setCover(store.getCover());
            form.setName(store.getName());
            form.setOwner(store.getOwner());
            form.setExternal(store.isExternal());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "店铺管理", "mall/store", "编辑店铺")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL, NAVIGATION_ADMIN_MALL_STORE)
                    .addObject("selection_items_owner", userService.findAll())
                    .addObject("selection_items_admins", userService.findAll())
                    .build();
        }).orElse(page(new Overview(new IconHeader("店铺不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall/store")).build());
    }

    @PostMapping("/store/{id}/edit")
    public ModelAndView mallStoreEditPage(@PathVariable String id, final AdminMallStoreEditForm form, BindingResult bindingResult) {
        return storeService.findOne(id).map(store -> {

            if (bindingResult.hasErrors()) {
                return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                        .setBreadcrumb("控制台", "", "商城管理", "mall", "店铺管理", "mall/store", "编辑店铺")
                        .setVerticalNavigation(NAVIGATION_ADMIN_MALL, NAVIGATION_ADMIN_MALL_STORE)
                        .addObject("selection_items_owner", userService.findAll())
                        .addObject("selection_items_admins", userService.findAll())
                        .build();
            }

            return submitForm(form, context -> {
                try {
                    storeService.update(store.getId(), s -> {
                        s.setCover(form.getCover());
                        s.setName(form.getName());
                        s.setOwner(form.getOwner());
                        s.setDateModified(new Date());
                        s.setExternal(form.isExternal());
                    });
                } catch (Exception e) {
                    throw new ActionException(e.getMessage(), e);
                }
            }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/mall/store"));
        }).orElse(page(new Overview(new IconHeader("店铺不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall/store")).build());
    }

    @GetMapping("/settings")
    public ModelAndView mallSettingsPage(final AdminMallSettingsForm form) {

        form.setCatalogs(systemConfigService.getList(MALL_COMMODITY_CATALOG));

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "商城管理", "mall", "商城设置")
                .setVerticalNavigation(NAVIGATION_ADMIN_MALL, NAVIGATION_ADMIN_MALL_SETTINGS)
                .build();
    }

    @PostMapping("/settings")
    public ModelAndView mallSettingsPage(@Valid final AdminMallSettingsForm form, BindingResult bindingResult,
                                         @RequestParam(value = "add_catalogs", required = false) boolean addCatalogs,
                                         @RequestParam(value = "remove_catalogs", required = false) Integer removeCatalogs) {
        if (addCatalogs) {
            form.getCatalogs().add("");
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "商城设置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL, NAVIGATION_ADMIN_MALL_SETTINGS)
                    .build();
        }

        if (Objects.nonNull(removeCatalogs)) {
            form.getCatalogs().remove(removeCatalogs.intValue());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "商城设置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL, NAVIGATION_ADMIN_MALL_SETTINGS)
                    .build();
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "商城设置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL, NAVIGATION_ADMIN_MALL_SETTINGS)
                    .build();
        }
        return submitForm(form, context -> {
            try {
                systemConfigService.update(MALL_COMMODITY_CATALOG, form.getCatalogs());
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/mall"));
    }
}
