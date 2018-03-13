package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.form.AdminSettingsSiteForm;
import com.qxcmp.admin.page.AdminSettingsPage;
import com.qxcmp.admin.page.AdminSettingsSitePage;
import com.qxcmp.audit.ActionException;
import com.qxcmp.config.SystemDictionaryItem;
import com.qxcmp.config.SystemDictionaryItemService;
import com.qxcmp.config.SystemDictionaryService;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.web.form.AdminSettingsDictionaryForm;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;

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
