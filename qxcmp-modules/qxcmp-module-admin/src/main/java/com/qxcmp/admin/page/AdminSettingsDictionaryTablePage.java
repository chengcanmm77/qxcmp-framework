package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.config.SystemDictionaryService;
import com.qxcmp.web.view.modules.table.EntityTable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SETTINGS_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SETTINGS_DICTIONARY;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminSettingsDictionaryTablePage extends AbstractQxcmpAdminEntityTablePage {

    private final SystemDictionaryService systemDictionaryService;
    private final Pageable pageable;

    @Override
    protected EntityTable renderEntityTable() {
        setMenu(ADMIN_MENU_SETTINGS, ADMIN_MENU_SETTINGS_DICTIONARY);
        return viewHelper.nextEntityTable(pageable, systemDictionaryService, request);
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("设置中心", ADMIN_SETTINGS_URL, "字典设置");
    }
}
