package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.message.SmsSendRecord;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.page.Page;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_SMS_URL;
import static com.qxcmp.admin.QxcmpAdminModuleNavigation.ADMIN_MENU_SMS;

/**
 * @author Aaric
 */
@Page
@RequiredArgsConstructor
public class AdminSmsSendRecordDetailsPage extends AbstractQxcmpAdminPage {

    private final SmsSendRecord smsSendRecord;

    @Override
    public void render() {
        addComponent(new TextContainer().addComponent(viewHelper.nextInfoOverview("短信发送详情")
                .addComponent(viewHelper.nextTable(objectObjectMap -> {
                    objectObjectMap.put("发送手机号", smsSendRecord.getPhones());
                    objectObjectMap.put("发送条数", smsSendRecord.getPhones().split(",").length);
                    objectObjectMap.put("发送日期", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(smsSendRecord.getDateCreated()));
                    objectObjectMap.put("业务名称", smsSendRecord.getTemplateName());
                    objectObjectMap.put("短信签名", smsSendRecord.getSignName());
                    objectObjectMap.put("短信模板", smsSendRecord.getTemplateCode());
                    objectObjectMap.put("短信参数", smsSendRecord.getTemplateParameter());
                    objectObjectMap.put("短信内容", smsSendRecord.getContent());
                    objectObjectMap.put("请求ID", smsSendRecord.getId());
                    objectObjectMap.put("发送状态", smsSendRecord.getCode());
                    objectObjectMap.put("状态描述", smsSendRecord.getMessage());
                }))
                .addLink("返回", ADMIN_SMS_URL)
        ));
        setMenu(ADMIN_MENU_SMS, "");
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("短信服务", ADMIN_SMS_URL, "发送详情");
    }
}
