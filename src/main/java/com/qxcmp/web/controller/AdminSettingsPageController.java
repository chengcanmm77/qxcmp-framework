package com.qxcmp.web.controller;

import com.google.common.collect.ImmutableList;
import com.qxcmp.account.AccountService;
import com.qxcmp.audit.ActionException;
import com.qxcmp.config.SystemDictionaryItem;
import com.qxcmp.config.SystemDictionaryItemService;
import com.qxcmp.config.SystemDictionaryService;
import com.qxcmp.core.event.AdminSettingsDictionaryEvent;
import com.qxcmp.core.event.AdminSettingsRegionEvent;
import com.qxcmp.core.event.AdminSettingsSiteEvent;
import com.qxcmp.region.Region;
import com.qxcmp.region.RegionLevel;
import com.qxcmp.region.RegionService;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.AdminSettingsDictionaryForm;
import com.qxcmp.web.form.AdminSettingsRegionNewForm;
import com.qxcmp.web.form.AdminSettingsSiteForm;
import com.qxcmp.web.model.RestfulResponse;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpNavigationConfiguration.*;
import static com.qxcmp.core.QxcmpSystemConfigConfiguration.*;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/settings")
@RequiredArgsConstructor
public class AdminSettingsPageController extends QxcmpController {

    private static final List<String> WATERMARK_POSITIONS = ImmutableList.of("左上", "中上", "右上", "左中", "居中", "右中", "左下", "中下", "右下");

    private final AccountService accountService;
    private final SystemDictionaryService systemDictionaryService;
    private final SystemDictionaryItemService systemDictionaryItemService;
    private final RegionService regionService;

    @GetMapping("")
    public ModelAndView settingsPage() {
        return page().addComponent(new Overview("系统设置")
                .addComponent(convertToTable(stringObjectMap -> {
                })))
                .setBreadcrumb("控制台", "", "系统设置")
                .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, "")
                .build();
    }

    @GetMapping("/site")
    public ModelAndView sitePage(final AdminSettingsSiteForm form) {

        form.setLogo(systemConfigService.getString(SYSTEM_CONFIG_SITE_LOGO).orElse(SYSTEM_CONFIG_SITE_LOGO_DEFAULT_VALUE));
        form.setFavicon(systemConfigService.getString(SYSTEM_CONFIG_SITE_FAVICON).orElse(SYSTEM_CONFIG_SITE_FAVICON_DEFAULT_VALUE));
        form.setDomain(systemConfigService.getString(SYSTEM_CONFIG_SITE_DOMAIN).orElse(""));
        form.setTitle(systemConfigService.getString(SYSTEM_CONFIG_SITE_TITLE).orElse(""));
        form.setKeywords(systemConfigService.getString(SYSTEM_CONFIG_SITE_KEYWORDS).orElse(""));
        form.setDescription(systemConfigService.getString(SYSTEM_CONFIG_SITE_DESCRIPTION).orElse(""));

        form.setWatermarkEnabled(systemConfigService.getBoolean(SYSTEM_CONFIG_IMAGE_WATERMARK_ENABLE).orElse(SYSTEM_CONFIG_IMAGE_WATERMARK_ENABLE_DEFAULT_VALUE));
        form.setWatermarkName(systemConfigService.getString(SYSTEM_CONFIG_IMAGE_WATERMARK_NAME).orElse(siteService.getTitle()));
        form.setWatermarkPosition(WATERMARK_POSITIONS.get(systemConfigService.getInteger(SYSTEM_CONFIG_IMAGE_WATERMARK_POSITION).orElse(SYSTEM_CONFIG_IMAGE_WATERMARK_POSITION_DEFAULT_VALUE)));
        form.setWatermarkFontSize(systemConfigService.getInteger(SYSTEM_CONFIG_IMAGE_WATERMARK_FONT_SIZE).orElse(SYSTEM_CONFIG_IMAGE_WATERMARK_FONT_SIZE_DEFAULT_VALUE));

        form.setAccountEnableUsername(systemConfigService.getBoolean(SYSTEM_CONFIG_ACCOUNT_ENABLE_USERNAME).orElse(false));
        form.setAccountEnableEmail(systemConfigService.getBoolean(SYSTEM_CONFIG_ACCOUNT_ENABLE_EMAIL).orElse(false));
        form.setAccountEnablePhone(systemConfigService.getBoolean(SYSTEM_CONFIG_ACCOUNT_ENABLE_PHONE).orElse(false));
        form.setAccountEnableInvite(systemConfigService.getBoolean(SYSTEM_CONFIG_ACCOUNT_ENABLE_INVITE).orElse(false));

        form.setThreadPoolSize(systemConfigService.getInteger(SYSTEM_CONFIG_TASK_EXECUTOR_CORE_POOL_SIZE).orElse(SYSTEM_CONFIG_TASK_EXECUTOR_CORE_POOL_SIZE_DEFAULT_VALUE));
        form.setMaxPoolSize(systemConfigService.getInteger(SYSTEM_CONFIG_TASK_EXECUTOR_MAX_POOL_SIZE).orElse(SYSTEM_CONFIG_TASK_EXECUTOR_MAX_POOL_SIZE_DEFAULT_VALUE));
        form.setQueueSize(systemConfigService.getInteger(SYSTEM_CONFIG_TASK_EXECUTOR_QUEUE_CAPACITY).orElse(SYSTEM_CONFIG_TASK_EXECUTOR_QUEUE_CAPACITY_DEFAULT_VALUE));

        form.setSessionTimeout(systemConfigService.getInteger(SYSTEM_CONFIG_SESSION_TIMEOUT).orElse(SYSTEM_CONFIG_SESSION_TIMEOUT_DEFAULT_VALUE));
        form.setMaxSessionCount(systemConfigService.getInteger(SYSTEM_CONFIG_SESSION_MAX_ACTIVE_COUNT).orElse(SYSTEM_CONFIG_SESSION_MAX_ACTIVE_COUNT_DEFAULT_VALUE));
        form.setPreventLogin(systemConfigService.getBoolean(SYSTEM_CONFIG_SESSION_MAX_PREVENT_LOGIN).orElse(SYSTEM_CONFIG_SESSION_MAX_PREVENT_LOGIN_DEFAULT_VALUE));

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "系统设置", "settings", "网站配置")
                .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, NAVIGATION_ADMIN_SETTINGS_SITE)
                .addObject("selection_items_position", WATERMARK_POSITIONS)
                .build();
    }

    @PostMapping("/site")
    public ModelAndView sitePage(@Valid final AdminSettingsSiteForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "系统设置", "settings", "网站配置")
                    .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, NAVIGATION_ADMIN_SETTINGS_SITE)
                    .addObject("selection_items_position", WATERMARK_POSITIONS)
                    .build();
        }

        return submitForm(form, (context) -> {
            try {
                systemConfigService.update(SYSTEM_CONFIG_SITE_LOGO, form.getLogo());
                systemConfigService.update(SYSTEM_CONFIG_SITE_FAVICON, form.getFavicon());
                systemConfigService.update(SYSTEM_CONFIG_SITE_DOMAIN, form.getDomain());
                systemConfigService.update(SYSTEM_CONFIG_SITE_TITLE, form.getTitle());
                systemConfigService.update(SYSTEM_CONFIG_SITE_KEYWORDS, form.getKeywords());
                systemConfigService.update(SYSTEM_CONFIG_SITE_DESCRIPTION, form.getDescription());

                systemConfigService.update(SYSTEM_CONFIG_IMAGE_WATERMARK_ENABLE, String.valueOf(form.isWatermarkEnabled()));
                systemConfigService.update(SYSTEM_CONFIG_IMAGE_WATERMARK_NAME, form.getWatermarkName());
                systemConfigService.update(SYSTEM_CONFIG_IMAGE_WATERMARK_POSITION, String.valueOf(WATERMARK_POSITIONS.indexOf(form.getWatermarkPosition())));
                systemConfigService.update(SYSTEM_CONFIG_IMAGE_WATERMARK_FONT_SIZE, String.valueOf(form.getWatermarkFontSize()));

                systemConfigService.update(SYSTEM_CONFIG_ACCOUNT_ENABLE_USERNAME, String.valueOf(form.isAccountEnableUsername()));
                systemConfigService.update(SYSTEM_CONFIG_ACCOUNT_ENABLE_EMAIL, String.valueOf(form.isAccountEnableEmail()));
                systemConfigService.update(SYSTEM_CONFIG_ACCOUNT_ENABLE_PHONE, String.valueOf(form.isAccountEnablePhone()));
                systemConfigService.update(SYSTEM_CONFIG_ACCOUNT_ENABLE_INVITE, String.valueOf(form.isAccountEnableInvite()));

                systemConfigService.update(SYSTEM_CONFIG_TASK_EXECUTOR_CORE_POOL_SIZE, String.valueOf(form.getThreadPoolSize()));
                systemConfigService.update(SYSTEM_CONFIG_TASK_EXECUTOR_MAX_POOL_SIZE, String.valueOf(form.getMaxPoolSize()));
                systemConfigService.update(SYSTEM_CONFIG_TASK_EXECUTOR_QUEUE_CAPACITY, String.valueOf(form.getQueueSize()));

                systemConfigService.update(SYSTEM_CONFIG_SESSION_TIMEOUT, String.valueOf(form.getSessionTimeout()));
                systemConfigService.update(SYSTEM_CONFIG_SESSION_MAX_ACTIVE_COUNT, String.valueOf(form.getMaxSessionCount()));
                systemConfigService.update(SYSTEM_CONFIG_SESSION_MAX_PREVENT_LOGIN, String.valueOf(form.isPreventLogin()));

                accountService.loadConfig();

                applicationContext.publishEvent(new AdminSettingsSiteEvent(currentUser().orElseThrow(RuntimeException::new)));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
    }

    @GetMapping("/dictionary")
    public ModelAndView dictionaryPage(Pageable pageable) {
        return page().addComponent(convertToTable(pageable, systemDictionaryService))
                .setBreadcrumb("控制台", "", "系统设置", "settings", "系统字典")
                .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, NAVIGATION_ADMIN_SETTINGS_DICTIONARY)
                .build();
    }

    @PostMapping("/dictionary")
    public ModelAndView dictionaryPage(@Valid final AdminSettingsDictionaryForm form,
                                       @RequestParam(value = "add_items", required = false) boolean addItems,
                                       @RequestParam(value = "remove_items", required = false) Integer removeItems) {

        if (addItems) {
            form.getItems().add(new SystemDictionaryItem());
            return page()
                    .addComponent(convertToForm(form))
                    .setBreadcrumb("控制台", "", "系统设置", "settings", "系统字典", "settings/dictionary", "系统字典编辑")
                    .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, NAVIGATION_ADMIN_SETTINGS_DICTIONARY)
                    .build();
        }

        if (Objects.nonNull(removeItems)) {
            form.getItems().remove(removeItems.intValue());
            return page()
                    .addComponent(convertToForm(form))
                    .setBreadcrumb("控制台", "", "系统设置", "settings", "系统字典", "settings/dictionary", "系统字典编辑")
                    .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, NAVIGATION_ADMIN_SETTINGS_DICTIONARY)
                    .build();
        }

        return systemDictionaryService.findOne(form.getName()).map(systemDictionary -> submitForm(form, context -> {
            try {
                systemDictionary.getItems().forEach(systemDictionaryItemService::delete);
                form.getItems().forEach(systemDictionaryItem -> {
                    systemDictionaryItem.setId(null);
                    systemDictionaryItem.setParent(systemDictionary);
                    systemDictionaryItemService.create(() -> systemDictionaryItem);
                });
                applicationContext.publishEvent(new AdminSettingsDictionaryEvent(currentUser().orElseThrow(RuntimeException::new), systemDictionary));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        })).orElse(page(viewHelper.nextWarningOverview("字典不存在", "").addLink("返回", QXCMP_BACKEND_URL + "/settings/dictionary")).build());
    }

    @GetMapping("/dictionary/{name}/edit")
    public ModelAndView dictionaryEditPage(@PathVariable String name, final AdminSettingsDictionaryForm form) {
        return systemDictionaryService.findOne(name).map(systemDictionary -> {
            form.setName(systemDictionary.getName());
            form.setItems(systemDictionary.getItems());
            return page()
                    .addComponent(convertToForm(form))
                    .setBreadcrumb("控制台", "", "系统设置", "settings", "系统字典", "settings/dictionary", "系统字典编辑")
                    .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, NAVIGATION_ADMIN_SETTINGS_DICTIONARY)
                    .build();
        }).orElse(page(new Overview(new IconHeader("字典不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/settings/dictionary")).build());
    }

    @GetMapping("/region")
    public ModelAndView regionPage(Pageable pageable) {
        return page().addComponent(convertToTable(new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "level"), regionService))
                .setBreadcrumb("控制台", "", "系统设置", "settings", "地区管理")
                .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, NAVIGATION_ADMIN_SETTINGS_REGION)
                .build();
    }

    @GetMapping("/region/{id}/new")
    public ModelAndView regionNewPage(@PathVariable String id, final AdminSettingsRegionNewForm form) {
        return regionService.findOne(id)
                .filter(region -> region.getLevel().equals(RegionLevel.CITY))
                .map(region -> {

                    form.setParent(String.format("%s - %s", region.getName(), region.getCode()));

                    List<Region> inferiors = regionService.findInferiors(region);

                    if (!inferiors.isEmpty()) {
                        Region infer = inferiors.get(inferiors.size() - 1);
                        try {
                            form.setCode(String.valueOf(Integer.parseInt(infer.getCode()) + 1));
                        } catch (Exception ignored) {

                        }
                    }

                    return page()
                            .addComponent(convertToForm(form))
                            .addComponent(convertToTable(stringObjectMap -> inferiors.forEach(r -> {
                                stringObjectMap.put(r.getName(), r.getCode());
                            })))
                            .setBreadcrumb("控制台", "", "系统设置", "settings", "地区管理", "settings/region", "添加地区")
                            .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, NAVIGATION_ADMIN_SETTINGS_REGION)
                            .build();
                })
                .orElse(page(viewHelper.nextWarningOverview("地区不存在", "").addLink("返回", QXCMP_BACKEND_URL + "/settings/region")).build());
    }

    @PostMapping("/region/{id}/new")
    public ModelAndView regionNewPage(@PathVariable String id, @Valid final AdminSettingsRegionNewForm form, BindingResult bindingResult) {
        return regionService.findOne(id)
                .filter(region -> region.getLevel().equals(RegionLevel.CITY))
                .map(region -> {

                    if (regionService.findOne(form.getCode()).isPresent()) {
                        bindingResult.rejectValue("code", "", "地区代码已经存在");
                    }

                    if (bindingResult.hasErrors()) {
                        return page()
                                .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                                .addComponent(convertToTable(stringObjectMap -> regionService.findInferiors(region).forEach(r -> {
                                    stringObjectMap.put(r.getName(), r.getCode());
                                })))
                                .setBreadcrumb("控制台", "", "系统设置", "settings", "地区管理", "settings/region", "添加地区")
                                .setVerticalNavigation(NAVIGATION_ADMIN_SETTINGS, NAVIGATION_ADMIN_SETTINGS_REGION)
                                .build();
                    }

                    return submitForm(form, context -> {
                        try {
                            Region r = regionService.next();

                            r.setCode(form.getCode());
                            r.setName(form.getName());
                            r.setParent(id);
                            r.setLevel(RegionLevel.COUNTY);

                            applicationContext.publishEvent(new AdminSettingsRegionEvent(currentUser().orElseThrow(RuntimeException::new), regionService.create(() -> r), "new"));


                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/settings/region").addLink("继续添加", ""));
                })
                .orElse(page(viewHelper.nextWarningOverview("地区不存在", "").addLink("返回", QXCMP_BACKEND_URL + "/settings/region")).build());
    }

    @PostMapping("/region/{code}/disable")
    public ResponseEntity<RestfulResponse> regionDisable(@PathVariable String code) {
        return regionService.findOne(code).map(region -> {
            RestfulResponse restfulResponse = audit("禁用地区", context -> {
                try {
                    if (region.getLevel().equals(RegionLevel.PROVINCE)) {
                        regionService.findInferiors(region).forEach(pInferior -> {
                            regionService.update(pInferior.getCode(), r -> r.setDisable(true));

                            regionService.findInferiors(pInferior).forEach(cInferior -> {
                                regionService.update(cInferior.getCode(), c -> c.setDisable(true));
                            });
                        });
                    } else if (region.getLevel().equals(RegionLevel.CITY)) {
                        regionService.findInferiors(region).forEach(inferior -> regionService.update(inferior.getCode(), r -> r.setDisable(true)));
                    }


                    applicationContext.publishEvent(new AdminSettingsRegionEvent(currentUser().orElseThrow(RuntimeException::new), regionService.update(region.getCode(), r -> r.setDisable(true)), "disable"));
                } catch (Exception e) {
                    throw new ActionException(e.getMessage(), e);
                }
            });
            return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @PostMapping("/region/{code}/enable")
    public ResponseEntity<RestfulResponse> regionEnable(@PathVariable String code) {
        return regionService.findOne(code).map(region -> {
            RestfulResponse restfulResponse = audit("启用地区", context -> {
                try {
                    if (region.getLevel().equals(RegionLevel.PROVINCE)) {
                        regionService.findAllInferiors(region).forEach(pInferior -> {
                            regionService.update(pInferior.getCode(), r -> r.setDisable(false));

                            regionService.findAllInferiors(pInferior).forEach(cInferior -> {
                                regionService.update(cInferior.getCode(), c -> c.setDisable(false));
                            });
                        });
                    } else if (region.getLevel().equals(RegionLevel.CITY)) {
                        regionService.findAllInferiors(region).forEach(inferior -> regionService.update(inferior.getCode(), r -> r.setDisable(false)));
                    }


                    applicationContext.publishEvent(new AdminSettingsRegionEvent(currentUser().orElseThrow(RuntimeException::new), regionService.update(region.getCode(), r -> r.setDisable(false)), "enable"));
                } catch (Exception e) {
                    throw new ActionException(e.getMessage(), e);
                }
            });
            return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }
}
