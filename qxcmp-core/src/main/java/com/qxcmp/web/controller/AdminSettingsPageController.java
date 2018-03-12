package com.qxcmp.web.controller;

import com.google.common.collect.ImmutableList;
import com.qxcmp.account.AccountService;
import com.qxcmp.audit.ActionException;
import com.qxcmp.config.SystemDictionaryItem;
import com.qxcmp.config.SystemDictionaryItemService;
import com.qxcmp.config.SystemDictionaryService;
import com.qxcmp.core.event.AdminSettingsDictionaryEvent;
import com.qxcmp.core.event.AdminSettingsSiteEvent;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.form.AdminSettingsDictionaryForm;
import com.qxcmp.web.form.AdminSettingsSiteForm;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpSystemConfig.*;

/**
 * @author Aaric
 */
@RequestMapping(QXCMP_ADMIN_URL + "/settings")
@RequiredArgsConstructor
public class AdminSettingsPageController extends QxcmpController {

    private static final List<String> WATERMARK_POSITIONS = ImmutableList.of("左上", "中上", "右上", "左中", "居中", "右中", "左下", "中下", "右下");

    private final AccountService accountService;
    private final SystemDictionaryService systemDictionaryService;
    private final SystemDictionaryItemService systemDictionaryItemService;

    @GetMapping("")
    public ModelAndView settingsPage() {
        return page().addComponent(new Overview("系统设置")
                .addComponent(convertToTable(stringObjectMap -> {
                })))
                .setBreadcrumb("控制台", "", "系统设置")
                .build();
    }

    @GetMapping("/site")
    public ModelAndView sitePage(final AdminSettingsSiteForm form) {

        form.setLogo(systemConfigService.getString(SITE_LOGO).orElse(SITE_LOGO_DEFAULT));
        form.setFavicon(systemConfigService.getString(SITE_FAVICON).orElse(SITE_FAVICON_DEFAULT));
        form.setDomain(systemConfigService.getString(SITE_DOMAIN).orElse(""));
        form.setTitle(systemConfigService.getString(SITE_TITLE).orElse(""));
        form.setKeywords(systemConfigService.getString(SITE_KEYWORDS).orElse(""));
        form.setDescription(systemConfigService.getString(SITE_DESCRIPTION).orElse(""));

        form.setWatermarkEnabled(systemConfigService.getBoolean(IMAGE_WATERMARK_ENABLE).orElse(IMAGE_WATERMARK_ENABLE_DEFAULT));
        form.setWatermarkName(systemConfigService.getString(IMAGE_WATERMARK_NAME).orElse(siteService.getTitle()));
        form.setWatermarkPosition(WATERMARK_POSITIONS.get(systemConfigService.getInteger(IMAGE_WATERMARK_POSITION).orElse(IMAGE_WATERMARK_POSITION_DEFAULT)));
        form.setWatermarkFontSize(systemConfigService.getInteger(IMAGE_WATERMARK_FONT_SIZE).orElse(IMAGE_WATERMARK_FONT_SIZE_DEFAULT));

        form.setAccountEnableUsername(systemConfigService.getBoolean(ACCOUNT_ENABLE_USERNAME).orElse(false));
        form.setAccountEnableEmail(systemConfigService.getBoolean(ACCOUNT_ENABLE_EMAIL).orElse(false));
        form.setAccountEnablePhone(systemConfigService.getBoolean(ACCOUNT_ENABLE_PHONE).orElse(false));
        form.setAccountEnableInvite(systemConfigService.getBoolean(ACCOUNT_ENABLE_INVITE).orElse(false));

        form.setThreadPoolSize(systemConfigService.getInteger(TASK_EXECUTOR_CORE_POOL_SIZE).orElse(TASK_EXECUTOR_CORE_POOL_SIZE_DEFAULT));
        form.setMaxPoolSize(systemConfigService.getInteger(TASK_EXECUTOR_MAX_POOL_SIZE).orElse(TASK_EXECUTOR_MAX_POOL_SIZE_DEFAULT));
        form.setQueueSize(systemConfigService.getInteger(TASK_EXECUTOR_QUEUE_CAPACITY).orElse(TASK_EXECUTOR_QUEUE_CAPACITY_DEFAULT));

        form.setSessionTimeout(systemConfigService.getInteger(SESSION_TIMEOUT).orElse(SESSION_TIMEOUT_DEFAULT));
        form.setMaxSessionCount(systemConfigService.getInteger(SESSION_MAX_ACTIVE_COUNT).orElse(SESSION_MAX_ACTIVE_COUNT_DEFAULT));
        form.setPreventLogin(systemConfigService.getBoolean(SESSION_MAX_PREVENT_LOGIN).orElse(SESSION_MAX_PREVENT_LOGIN_DEFAULT));

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", "", "系统设置", "settings", "网站配置")
                .addObject("selection_items_position", WATERMARK_POSITIONS)
                .build();
    }

    @PostMapping("/site")
    public ModelAndView sitePage(@Valid final AdminSettingsSiteForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                    .setBreadcrumb("控制台", "", "系统设置", "settings", "网站配置")
                    .addObject("selection_items_position", WATERMARK_POSITIONS)
                    .build();
        }

        return submitForm(form, (context) -> {
            try {
                systemConfigService.update(SITE_LOGO, form.getLogo());
                systemConfigService.update(SITE_FAVICON, form.getFavicon());
                systemConfigService.update(SITE_DOMAIN, form.getDomain());
                systemConfigService.update(SITE_TITLE, form.getTitle());
                systemConfigService.update(SITE_KEYWORDS, form.getKeywords());
                systemConfigService.update(SITE_DESCRIPTION, form.getDescription());

                systemConfigService.update(IMAGE_WATERMARK_ENABLE, String.valueOf(form.isWatermarkEnabled()));
                systemConfigService.update(IMAGE_WATERMARK_NAME, form.getWatermarkName());
                systemConfigService.update(IMAGE_WATERMARK_POSITION, String.valueOf(WATERMARK_POSITIONS.indexOf(form.getWatermarkPosition())));
                systemConfigService.update(IMAGE_WATERMARK_FONT_SIZE, String.valueOf(form.getWatermarkFontSize()));

                systemConfigService.update(ACCOUNT_ENABLE_USERNAME, String.valueOf(form.isAccountEnableUsername()));
                systemConfigService.update(ACCOUNT_ENABLE_EMAIL, String.valueOf(form.isAccountEnableEmail()));
                systemConfigService.update(ACCOUNT_ENABLE_PHONE, String.valueOf(form.isAccountEnablePhone()));
                systemConfigService.update(ACCOUNT_ENABLE_INVITE, String.valueOf(form.isAccountEnableInvite()));

                systemConfigService.update(TASK_EXECUTOR_CORE_POOL_SIZE, String.valueOf(form.getThreadPoolSize()));
                systemConfigService.update(TASK_EXECUTOR_MAX_POOL_SIZE, String.valueOf(form.getMaxPoolSize()));
                systemConfigService.update(TASK_EXECUTOR_QUEUE_CAPACITY, String.valueOf(form.getQueueSize()));

                systemConfigService.update(SESSION_TIMEOUT, String.valueOf(form.getSessionTimeout()));
                systemConfigService.update(SESSION_MAX_ACTIVE_COUNT, String.valueOf(form.getMaxSessionCount()));
                systemConfigService.update(SESSION_MAX_PREVENT_LOGIN, String.valueOf(form.isPreventLogin()));

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
                    .build();
        }

        if (Objects.nonNull(removeItems)) {
            form.getItems().remove(removeItems.intValue());
            return page()
                    .addComponent(convertToForm(form))
                    .setBreadcrumb("控制台", "", "系统设置", "settings", "系统字典", "settings/dictionary", "系统字典编辑")
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
        })).orElse(page(viewHelper.nextWarningOverview("字典不存在", "").addLink("返回", QXCMP_ADMIN_URL + "/settings/dictionary")).build());
    }

    @GetMapping("/dictionary/{name}/edit")
    public ModelAndView dictionaryEditPage(@PathVariable String name, final AdminSettingsDictionaryForm form) {
        return systemDictionaryService.findOne(name).map(systemDictionary -> {
            form.setName(systemDictionary.getName());
            form.setItems(systemDictionary.getItems());
            return page()
                    .addComponent(convertToForm(form))
                    .setBreadcrumb("控制台", "", "系统设置", "settings", "系统字典", "settings/dictionary", "系统字典编辑")
                    .build();
        }).orElse(page(new Overview(new IconHeader("字典不存在", new Icon("warning circle"))).addLink("返回", QXCMP_ADMIN_URL + "/settings/dictionary")).build());
    }

}
