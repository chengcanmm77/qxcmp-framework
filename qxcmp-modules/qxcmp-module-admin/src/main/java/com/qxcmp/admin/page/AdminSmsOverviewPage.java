package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.message.SmsTemplateExtensionPoint;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.divider.HorizontalDivider;
import com.qxcmp.web.view.page.Page;
import com.qxcmp.web.view.views.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SMS_FEED;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SMS;
import static com.qxcmp.core.QxcmpSystemConfig.MESSAGE_SMS_ACCESS_KEY;
import static com.qxcmp.core.QxcmpSystemConfig.MESSAGE_SMS_ACCESS_SECRET;

/**
 * @author Aaric
 */
@Page
public class AdminSmsOverviewPage extends AbstractQxcmpAdminPage {

    private SmsTemplateExtensionPoint smsTemplateExtensionPoint;

    @Override
    public void render() {
        addComponent(new TextContainer().addComponent(viewHelper.nextInfoOverview("短信服务")
                .addComponent(new HorizontalDivider("基本配置"))
                .addComponent(viewHelper.nextTable(objectObjectMap -> {
                    objectObjectMap.put("AccessKey", systemConfigService.getString(MESSAGE_SMS_ACCESS_KEY).orElse(""));
                    objectObjectMap.put("AccessSecret", systemConfigService.getString(MESSAGE_SMS_ACCESS_SECRET).orElse(""));
                }))
                .addComponent(new HorizontalDivider("业务配置"))
                .addComponent(viewHelper.nextTable(objectObjectMap -> {
                    objectObjectMap.put("业务名称", "模板内容");
                    smsTemplateExtensionPoint.getExtensions().forEach(extension -> objectObjectMap.put(extension.getName() + "-" + extension.getTemplateCode(), extension.getContent()));
                }))
                .addComponent(new HorizontalDivider("最近发送"))
                .addComponent(new Feed(feedService.findByType(ADMIN_SMS_FEED, PageRequest.of(0, 20)).getContent()))
        ));
        setMenu(ADMIN_MENU_SMS, "");
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("短信服务");
    }

    @Autowired
    public void setSmsTemplateExtensionPoint(SmsTemplateExtensionPoint smsTemplateExtensionPoint) {
        this.smsTemplateExtensionPoint = smsTemplateExtensionPoint;
    }
}
