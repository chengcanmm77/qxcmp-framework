package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SECURITY;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminSecurityPage extends AbstractQxcmpAdminPage {
    @Override
    public void render() {
        setMenu(ADMIN_MENU_SECURITY, "");
        addComponent(viewHelper.nextOverview("安全配置"));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("安全配置");
    }
}
