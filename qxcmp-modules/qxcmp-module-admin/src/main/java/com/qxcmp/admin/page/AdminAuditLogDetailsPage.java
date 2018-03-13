package com.qxcmp.admin.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.audit.AuditLog;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.header.ContentHeader;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.modules.table.dictionary.ComponentCell;
import com.qxcmp.web.view.modules.table.dictionary.TextValueCell;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_AUDIT_LOG_URL;
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminAuditLogDetailsPage extends AbstractQxcmpAdminPage {

    private final AuditLog auditLog;

    @Override
    public void render() {
        addComponent(new TextContainer().addComponent(viewHelper.nextInfoOverview("日志详情")
                .addComponent(viewHelper.nextTable(objectObjectMap -> {
                    objectObjectMap.put("ID", auditLog.getId());
                    objectObjectMap.put("操作名称", auditLog.getTitle());
                    objectObjectMap.put("操作人", auditLog.getOwner() == null ? "无" : new TextValueCell(auditLog.getOwner().getDisplayName(), QXCMP_ADMIN_URL + "/user/" + auditLog.getOwner().getId() + "/details"));
                    objectObjectMap.put("操作链接", auditLog.getUrl());
                    objectObjectMap.put("操作时间", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(auditLog.getDateCreated()));
                    objectObjectMap.put("操作状态", auditLog.getStatus().equals(AuditLog.Status.SUCCESS) ? new ComponentCell(new Icon("check circle").setColor(Color.GREEN)) : new ComponentCell(new Icon("warning circle").setColor(Color.RED)));
                    objectObjectMap.put("备注信息", auditLog.getComments());
                }))
                .addComponent(new Segment().addComponent(new ContentHeader("操作内容", Size.SMALL).setDividing()).addComponent(new P(auditLog.getContent())))
                .addLink("返回", ADMIN_AUDIT_LOG_URL)
        ));
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("系统日志", ADMIN_AUDIT_LOG_URL, "日志详情");
    }
}
