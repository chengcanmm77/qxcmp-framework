package com.qxcmp.mall.web;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qxcmp.audit.ActionException;
import com.qxcmp.mall.*;
import com.qxcmp.user.User;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.Component;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.label.BasicLabel;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.modules.form.support.KeyValueEntity;
import com.qxcmp.web.view.support.utils.TableHelper;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT;
import static com.qxcmp.core.QxcmpNavigationConfiguration.NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY;
import static com.qxcmp.core.QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_MALL_COMMODITY_CATALOG;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/mall/user/store")
@RequiredArgsConstructor
public class AdminMallUserPageController extends QxcmpController {

    /**
     * 用户所选店铺偏好
     */
    private final String USER_CONFIG_STORE_SELECTION = "admin.mall.user.store.selection";

    private final StoreService storeService;

    private final CommodityService commodityService;

    private final CommodityVersionService commodityVersionService;

    private final TableHelper tableHelper;

    /**
     * 用户先进行店铺选择，然后进行相关店铺的处理
     *
     * @return 店铺选择页面
     */
    @GetMapping("")
    public ModelAndView userStorePage() {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Store> stores = storeService.findByUser(user);

        if (stores.isEmpty()) {
            return page(new Overview(new IconHeader("你还没有管理任何店铺", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall")).build();
        }

        Store selectedStore = getUserSelectedStore(user);

        if (Objects.isNull(selectedStore) || !stores.contains(selectedStore)) {
            return redirect(QXCMP_BACKEND_URL + "/mall/user/store/select");
        } else {
            return page().addComponent(new Segment()
                    .addComponent(getUserStorePageHeader(selectedStore)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, "")
                    .build();
        }
    }

    @GetMapping("/select")
    public ModelAndView userStoreSelectPage(final AdminMallUserStoreSelectForm form) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Store> stores = storeService.findByUser(user);

        if (stores.isEmpty()) {
            return page(new Overview(new IconHeader("你还没有管理任何店铺", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall")).build();
        }

        form.setStore(stores.get(0));

        return page().addComponent(new TextContainer().addComponent(new Segment()
                .addComponent(new PageHeader(HeaderType.H4, "请选择店铺"))
                .addComponent(convertToForm(form))))
                .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "选择店铺")
                .addObject("selection_items_store", stores)
                .build();
    }

    @PostMapping("/select")
    public ModelAndView userStoreSelectPage(@Valid final AdminMallUserStoreSelectForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Store> stores = storeService.findByUser(user);

        if (bindingResult.hasErrors() || !stores.contains(form.getStore())) {
            return page(new Overview(new IconHeader("店铺不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall")).build();
        }

        userConfigService.save(user.getId(), USER_CONFIG_STORE_SELECTION, form.getStore().getId());

        return redirect(QXCMP_BACKEND_URL + "/mall/user/store");
    }

    @GetMapping("/commodity")
    public ModelAndView userCommodityPage(Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Store> stores = storeService.findByUser(user);

        if (stores.isEmpty()) {
            return page(new Overview(new IconHeader("你还没有管理任何店铺", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall")).build();
        }

        Store selectedStore = getUserSelectedStore(user);

        Page<Commodity> commodities = commodityService.findByStore(selectedStore, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "dateModified"));

        return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToTable("userStoreCommodity", Commodity.class, commodities)))
                .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                .build();
    }

    @GetMapping("/commodity/new")
    public ModelAndView userCommodityNewPage(final AdminMallUserStoreCommodityNewForm form) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Store> stores = storeService.findByUser(user);

        if (stores.isEmpty()) {
            return page(new Overview(new IconHeader("你还没有管理任何店铺", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall")).build();
        }

        Store selectedStore = getUserSelectedStore(user);

        return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "添加商品")
                .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                .build();
    }

    @PostMapping("/commodity/new")
    public ModelAndView userCommodityNewPage(@Valid final AdminMallUserStoreCommodityNewForm form, BindingResult bindingResult,
                                             @RequestParam(value = "add_versions", required = false) boolean addVersions,
                                             @RequestParam(value = "remove_versions", required = false) Integer removeVersions,
                                             @RequestParam(value = "add_customProperties", required = false) boolean addCustomProperties,
                                             @RequestParam(value = "remove_customProperties", required = false) Integer removeCustomProperties) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Store> stores = storeService.findByUser(user);

        if (stores.isEmpty()) {
            return page(new Overview(new IconHeader("你还没有管理任何店铺", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall")).build();
        }

        Store selectedStore = getUserSelectedStore(user);

        if (addCustomProperties) {
            form.getCustomProperties().add(new KeyValueEntity());
            return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "添加商品")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                    .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                    .build();
        }

        if (Objects.nonNull(removeCustomProperties)) {
            form.getCustomProperties().remove(removeCustomProperties.intValue());
            return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "添加商品")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                    .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                    .build();
        }

        if (addVersions) {
            form.getVersions().add(commodityVersionService.next());
            return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "添加商品")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                    .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                    .build();
        }

        if (Objects.nonNull(removeVersions)) {
            form.getVersions().remove(removeVersions.intValue());
            return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "添加商品")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                    .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                    .build();
        }

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "添加商品")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                    .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                    .build();
        }

        return submitForm(form, context -> {
            try {
                commodityService.create(() -> {
                    Commodity commodity = commodityService.next();
                    commodity.setCover(form.getCover());
                    commodity.setAlbums(form.getAlbums());
                    commodity.setDetails(form.getDetails());
                    commodity.setTitle(form.getTitle());
                    commodity.setSubTitle(form.getSubTitle());
                    commodity.setCatalogs(form.getCatalogs());
                    commodity.setOriginPrice(form.getOriginPrice());
                    commodity.setSellPrice(form.getSellPrice());
                    commodity.setInventory(form.getInventory());
                    commodity.setDisabled(form.isDisabled());
                    commodity.setPointOnly(form.isPointOnly());
                    commodity.setPoint(form.getPoint());

                    commodity.getCustomProperties().clear();
                    form.getCustomProperties().forEach(keyValueEntity -> commodity.getCustomProperties().put(keyValueEntity.getKey(), keyValueEntity.getValue()));

                    commodity.setStore(selectedStore);
                    commodity.setUserModified(user);
                    commodity.setDateCreated(new Date());
                    commodity.setDateModified(new Date());
                    return commodity;
                }).ifPresent(commodity -> {
                    form.getVersions().forEach(commodityVersion -> commodityVersionService.create(() -> {
                        CommodityVersion next = commodityVersionService.next();
                        next.setCommodity(commodity);
                        next.setName(commodityVersion.getName());
                        next.setValue(commodityVersion.getValue());
                        return next;
                    }));
                    commodityService.update(commodity.getId(), c -> c.setParentId(commodity.getId()));
                });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (context, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/mall/user/store/commodity").addLink("继续添加商品", QXCMP_BACKEND_URL + "/mall/user/store/commodity/new"));
    }

    @GetMapping("/commodity/{id}/edit")
    public ModelAndView userCommodityEditPage(@PathVariable String id, final AdminMallUserStoreCommodityEditForm form) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Store> stores = storeService.findByUser(user);

        Store selectedStore = getUserSelectedStore(user);

        return commodityService.findOne(id)
                .filter(commodity -> stores.contains(commodity.getStore()))
                .map(commodity -> {
                    form.setCover(commodity.getCover());
                    form.setAlbums(commodity.getAlbums());
                    form.setDetails(commodity.getDetails());
                    form.setTitle(commodity.getTitle());
                    form.setSubTitle(commodity.getSubTitle());
                    form.setCatalogs(commodity.getCatalogs());
                    form.setOriginPrice(commodity.getOriginPrice());
                    form.setSellPrice(commodity.getSellPrice());
                    form.setInventory(commodity.getInventory());
                    form.setDisabled(commodity.isDisabled());
                    form.setPointOnly(commodity.isPointOnly());
                    form.setPoint(commodity.getPoint());
                    form.setCustomProperties(commodity.getCustomProperties().entrySet().stream().map(stringStringEntry -> {
                        KeyValueEntity keyValueEntity = new KeyValueEntity();
                        keyValueEntity.setKey(stringStringEntry.getKey());
                        keyValueEntity.setValue(stringStringEntry.getValue());
                        return keyValueEntity;
                    }).collect(Collectors.toList()));
                    form.setParentId(String.valueOf(Objects.isNull(commodity.getParentId()) ? "" : commodity.getParentId()));
                    form.setVersions(commodity.getVersions());

                    return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                            .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "编辑商品")
                            .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                            .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                            .build();
                }).orElse(page(new Overview(new IconHeader("商品不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall/user/store/commodity")).build());
    }

    @PostMapping("/commodity/{id}/edit")
    public ModelAndView userCommodityEditPage(@PathVariable String id, @Valid final AdminMallUserStoreCommodityEditForm form, BindingResult bindingResult,
                                              @RequestParam(value = "add_versions", required = false) boolean addVersions,
                                              @RequestParam(value = "remove_versions", required = false) Integer removeVersions,
                                              @RequestParam(value = "add_customProperties", required = false) boolean addCustomProperties,
                                              @RequestParam(value = "remove_customProperties", required = false) Integer removeCustomProperties) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Store> stores = storeService.findByUser(user);

        Store selectedStore = getUserSelectedStore(user);

        if (addCustomProperties) {
            form.getCustomProperties().add(new KeyValueEntity());
            return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "编辑商品")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                    .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                    .build();
        }

        if (Objects.nonNull(removeCustomProperties)) {
            form.getCustomProperties().remove(removeCustomProperties.intValue());
            return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "编辑商品")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                    .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                    .build();
        }

        if (addVersions) {
            form.getVersions().add(commodityVersionService.next());
            return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "编辑商品")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                    .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                    .build();
        }

        if (Objects.nonNull(removeVersions)) {
            form.getVersions().remove(removeVersions.intValue());
            return page().addComponent(new Segment().addComponent(getUserStorePageHeader(selectedStore)).addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", "", "商城管理", "mall", "我的店铺", "mall/user/store", "商品管理", "mall/user/store/commodity", "编辑商品")
                    .setVerticalNavigation(NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT, NAVIGATION_ADMIN_MALL_USER_STORE_MANAGEMENT_COMMODITY)
                    .addObject("selection_items_catalogs", systemConfigService.getList(SYSTEM_CONFIG_MALL_COMMODITY_CATALOG))
                    .build();
        }

        return commodityService.findOne(id)
                .filter(commodity -> stores.contains(commodity.getStore()))
                .map(commodity -> submitForm(form, context -> {
                    try {
                        commodityService.update(commodity.getId(), c -> {
                            c.setCover(form.getCover());
                            c.setAlbums(form.getAlbums());
                            c.setDetails(form.getDetails());
                            c.setTitle(form.getTitle());
                            c.setSubTitle(form.getSubTitle());
                            c.setCatalogs(form.getCatalogs());
                            c.setOriginPrice(form.getOriginPrice());
                            c.setSellPrice(form.getSellPrice());
                            c.setInventory(form.getInventory());
                            c.setDisabled(form.isDisabled());
                            c.setPointOnly(form.isPointOnly());
                            c.setPoint(form.getPoint());
                            c.getCustomProperties().clear();
                            form.getCustomProperties().forEach(keyValueEntity -> c.getCustomProperties().put(keyValueEntity.getKey(), keyValueEntity.getValue()));

                            try {
                                c.setParentId(Long.valueOf(form.getParentId()));
                            } catch (Exception e) {
                                c.setParentId(c.getParentId());
                            }
                            c.setStore(selectedStore);
                            c.setUserModified(user);
                            c.setDateModified(new Date());

                            c.getVersions().forEach(commodityVersionService::remove);
                            c.getVersions().clear();
                            form.getVersions().forEach(commodityVersion -> commodityVersionService.create(() -> {
                                CommodityVersion next = commodityVersionService.next();
                                next.setCommodity(c);
                                next.setName(commodityVersion.getName());
                                next.setValue(commodityVersion.getValue());
                                return next;
                            }));
                        });
                    } catch (Exception e) {
                        throw new ActionException(e.getMessage(), e);
                    }
                }, (context, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/mall/user/store/commodity")))
                .orElse(page(new Overview(new IconHeader("商品不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/mall/user/store/commodity")).build());
    }

    @PostMapping("/commodity/{id}/copy")
    public ResponseEntity<RestfulResponse> userCommodityCopy(@PathVariable String id) {
        RestfulResponse restfulResponse = audit("复制商品", context -> {

            User user = currentUser().orElseThrow(RuntimeException::new);

            List<Store> stores = storeService.findByUser(user);

            Store selectedStore = getUserSelectedStore(user);

            try {
                commodityService.findOne(id)
                        .filter(commodity -> stores.contains(commodity.getStore()))
                        .ifPresent(commodity -> {
                            Commodity next = commodityService.next();
                            next.setCover(commodity.getCover());
                            next.setTitle("【复制】" + commodity.getTitle());
                            next.setSubTitle(commodity.getSubTitle());
                            next.setOriginPrice(commodity.getOriginPrice());
                            next.setSellPrice(commodity.getSellPrice());
                            next.setInventory(commodity.getInventory());
                            next.setDisabled(true);
                            next.setParentId(commodity.getParentId());
                            next.setStore(selectedStore);
                            next.setUserModified(user);
                            next.setDateCreated(new Date());
                            next.setDateModified(new Date());

                            Set<String> albums = Sets.newLinkedHashSet();
                            albums.addAll(commodity.getAlbums());
                            next.setAlbums(albums);

                            Set<String> details = Sets.newLinkedHashSet();
                            details.addAll(commodity.getDetails());
                            next.setDetails(details);

                            Set<String> catalogs = Sets.newLinkedHashSet();
                            catalogs.addAll(commodity.getCatalogs());
                            next.setCatalogs(catalogs);

                            Map<String, String> properties = Maps.newLinkedHashMap();
                            properties.putAll(commodity.getCustomProperties());
                            next.setCustomProperties(properties);

                            commodityService.create(() -> next).ifPresent(c -> commodity.getVersions().forEach(commodityVersion -> {
                                CommodityVersion version = commodityVersionService.next();
                                version.setCommodity(c);
                                version.setName(commodityVersion.getName());
                                version.setValue(commodityVersion.getValue());
                                commodityVersionService.create(() -> version);
                            }));
                        });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @PostMapping("/commodity/{id}/disable")
    public ResponseEntity<RestfulResponse> userCommodityDisable(@PathVariable String id) {
        RestfulResponse restfulResponse = audit("下架商品", context -> {

            User user = currentUser().orElseThrow(RuntimeException::new);

            List<Store> stores = storeService.findByUser(user);

            try {
                commodityService.findOne(id)
                        .filter(commodity -> stores.contains(commodity.getStore()))
                        .ifPresent(commodity -> commodityService.update(commodity.getId(), c -> c.setDisabled(true)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    @PostMapping("/commodity/{id}/enable")
    public ResponseEntity<RestfulResponse> userCommodityEnable(@PathVariable String id) {
        RestfulResponse restfulResponse = audit("上架商品", context -> {

            User user = currentUser().orElseThrow(RuntimeException::new);

            List<Store> stores = storeService.findByUser(user);

            try {
                commodityService.findOne(id)
                        .filter(commodity -> stores.contains(commodity.getStore()))
                        .ifPresent(commodity -> commodityService.update(commodity.getId(), c -> c.setDisabled(false)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }

    private Store getUserSelectedStore(User user) {
        return storeService.findOne(userConfigService.getString(user.getId(), USER_CONFIG_STORE_SELECTION).orElse("")).orElse(null);
    }

    private Component getUserStorePageHeader(Store selectedStore) {
        return new BasicLabel(selectedStore.getName()).setIcon(new Icon("marker")).setUrl(QXCMP_BACKEND_URL + "/mall/user/store/select");
    }
}
