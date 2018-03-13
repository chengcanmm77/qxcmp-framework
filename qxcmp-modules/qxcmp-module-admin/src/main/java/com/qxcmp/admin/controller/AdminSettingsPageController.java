package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.form.AdminSettingsDictionaryForm;
import com.qxcmp.admin.form.AdminSettingsEmailForm;
import com.qxcmp.admin.form.AdminSettingsSiteForm;
import com.qxcmp.admin.form.AdminSettingsSmsForm;
import com.qxcmp.admin.page.*;
import com.qxcmp.audit.ActionException;
import com.qxcmp.config.SystemDictionaryItem;
import com.qxcmp.config.SystemDictionaryItemService;
import com.qxcmp.config.SystemDictionaryService;
import com.qxcmp.core.QxcmpSystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SETTINGS_URL;
import static com.qxcmp.admin.QxcmpAdminModule.WATERMARK_POSITIONS;
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static com.qxcmp.core.QxcmpSystemConfig.*;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_ADMIN_URL + "/settings")
@RequiredArgsConstructor
public class AdminSettingsPageController extends QxcmpAdminController {

    private final SystemDictionaryService systemDictionaryService;
    private final SystemDictionaryItemService systemDictionaryItemService;

    @GetMapping("")
    public ModelAndView settingsPage() {
        return page(AdminSettingsPage.class);
    }

    @GetMapping("/site")
    public ModelAndView siteGet(final AdminSettingsSiteForm form, BindingResult bindingResult) {
        form.setImageWatermarkName(systemConfigService.getString(IMAGE_WATERMARK_NAME).orElse(siteService.getTitle()));
        form.setImageWatermarkPosition(WATERMARK_POSITIONS.get(systemConfigService.getInteger(IMAGE_WATERMARK_POSITION).orElse(IMAGE_WATERMARK_POSITION_DEFAULT)));
        return systemConfigPage(AdminSettingsSitePage.class, form, bindingResult, QxcmpSystemConfig.class)
                .addObject("selection_items_imageWatermarkPosition", WATERMARK_POSITIONS);
    }

    @PostMapping("/site")
    public ModelAndView sitePost(@Valid final AdminSettingsSiteForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return siteGet(form, bindingResult);
        }
        ModelAndView modelAndView = updateSystemConfig(QxcmpSystemConfig.class, form);
        systemConfigService.update(IMAGE_WATERMARK_POSITION, String.valueOf(WATERMARK_POSITIONS.indexOf(form.getImageWatermarkPosition())));
        return modelAndView;
    }

    @GetMapping("/dictionary")
    public ModelAndView dictionaryPage(Pageable pageable) {
        return page(AdminSettingsDictionaryTablePage.class, systemDictionaryService, pageable);
    }

    @PostMapping("/dictionary")
    public ModelAndView dictionaryPost(@Valid final AdminSettingsDictionaryForm form, BindingResult bindingResult,
                                       @RequestParam(value = "add_items", required = false) boolean addItems,
                                       @RequestParam(value = "remove_items", required = false) Integer removeItems) {
        if (addItems) {
            form.getItems().add(new SystemDictionaryItem());
            return page(AdminSettingsDictionaryUpdatePage.class, form, bindingResult);
        }
        if (Objects.nonNull(removeItems)) {
            form.getItems().remove(removeItems.intValue());
            return page(AdminSettingsDictionaryUpdatePage.class, form, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return dictionaryGet(form.getName(), form, bindingResult);
        }
        return systemDictionaryService.findOne(form.getName()).map(systemDictionary -> execute("编辑系统字典", context -> {
            try {
                systemDictionary.getItems().forEach(systemDictionaryItemService::delete);
                form.getItems().forEach(systemDictionaryItem -> {
                    systemDictionaryItem.setId(null);
                    systemDictionaryItem.setParent(systemDictionary);
                    systemDictionaryItemService.create(() -> systemDictionaryItem);
                });
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回", ADMIN_SETTINGS_URL + "/dictionary")))
                .orElse(overviewPage(viewHelper.nextWarningOverview("字典不存在")));
    }

    @GetMapping("/dictionary/{name}/edit")
    public ModelAndView dictionaryGet(@PathVariable String name, final AdminSettingsDictionaryForm form, BindingResult bindingResult) {
        return entityUpdatePage(AdminSettingsDictionaryUpdatePage.class, name, systemDictionaryService, form, bindingResult);
    }

    @GetMapping("/email")
    public ModelAndView emailGet(final AdminSettingsEmailForm form, BindingResult bindingResult) {
        return systemConfigPage(AdminSettingsEmailPage.class, form, bindingResult, QxcmpSystemConfig.class);
    }

    @PostMapping("/email")
    public ModelAndView emailPost(@Valid final AdminSettingsEmailForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return emailGet(form, bindingResult);
        }
        return updateSystemConfig(QxcmpSystemConfig.class, form);
    }

    @GetMapping("/sms")
    public ModelAndView smsGet(final AdminSettingsSmsForm form, BindingResult bindingResult) {
        return systemConfigPage(AdminSettingsSmsPage.class, form, bindingResult, QxcmpSystemConfig.class);
    }

    @PostMapping("/sms")
    public ModelAndView smsPost(@Valid final AdminSettingsSmsForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return smsGet(form, bindingResult);
        }
        return updateSystemConfig(QxcmpSystemConfig.class, form);
    }
}
