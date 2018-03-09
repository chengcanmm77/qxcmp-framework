package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_TOOLS;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 系统工具页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminToolsPage extends AbstractQxcmpAdminPage {

    @Override
    public void render() {
        setMenu(ADMIN_MENU_TOOLS, "");
        addComponent(viewHelper.nextOverview("系统工具"));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("系统工具");
    }

}
