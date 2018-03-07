package com.qxcmp.web.page;

import com.jcabi.manifests.Manifests;
import com.qxcmp.core.QxcmpConfiguration;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.header.HeaderType;
import com.qxcmp.web.view.elements.header.PageHeader;
import com.qxcmp.web.view.modules.table.AbstractTable;
import com.qxcmp.web.view.page.QxcmpAdminPage;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP;
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * 后台关于页面
 *
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminAboutPage extends QxcmpAdminPage {

    @Override
    public void render() {
        setBreadcrumb("控制台", "", "关于");
        addComponent(new TextContainer().addComponent(new Overview(new PageHeader(HeaderType.H1, QXCMP)).addComponent(getTableView()).addLink("返回", QXCMP_ADMIN_URL)));
    }

    private AbstractTable getTableView() {
        return viewHelper.nextTable(stringObjectMap -> {
            String appVersion = QxcmpConfiguration.class.getPackage().getImplementationVersion();
            String appBuildDate = "development";
            String appStartUpDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(applicationContext.getStartupDate()));

            try {
                appBuildDate = Manifests.read("Build-Date");
            } catch (Exception ignored) {

            }

            if (StringUtils.isBlank(appVersion)) {
                appVersion = "暂无版本信息";
            }

            stringObjectMap.put("平台版本", appVersion);
            stringObjectMap.put("构建日期", appBuildDate);
            stringObjectMap.put("启动日期", appStartUpDate);
            stringObjectMap.put("软件版本", System.getProperty("java.version"));
        });
    }

}
