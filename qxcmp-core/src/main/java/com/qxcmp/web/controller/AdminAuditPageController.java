package com.qxcmp.web.controller;

import com.qxcmp.audit.AuditLog;
import com.qxcmp.audit.AuditLogService;
import com.qxcmp.web.QxcmpController;
import com.qxcmp.web.view.elements.container.TextContainer;
import com.qxcmp.web.view.elements.header.ContentHeader;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.elements.segment.Segment;
import com.qxcmp.web.view.modules.table.dictionary.ComponentCell;
import com.qxcmp.web.view.modules.table.dictionary.TextValueCell;
import com.qxcmp.web.view.support.Color;
import com.qxcmp.web.view.support.Size;
import com.qxcmp.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/audit")
@RequiredArgsConstructor
public class AdminAuditPageController extends QxcmpController {

    private final AuditLogService auditLogService;

    @GetMapping("")
    public ModelAndView logPage(Pageable pageable) {
        return page().addComponent(convertToTable(pageable, auditLogService))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "系统日志")
                .build();
    }

    @GetMapping("/{id}/details")
    public ModelAndView logDetailsPage(@PathVariable String id) {
        return auditLogService.findOne(id).map(auditLog -> page()
                .addComponent(new TextContainer().addComponent(new Overview("日志详情")
                        .addComponent(convertToTable(stringObjectMap -> {
                            stringObjectMap.put("ID", auditLog.getId());
                            stringObjectMap.put("操作名称", auditLog.getTitle());
                            stringObjectMap.put("操作人", auditLog.getOwner() == null ? "无" : new TextValueCell(auditLog.getOwner().getDisplayName(), QXCMP_BACKEND_URL + "/user/" + auditLog.getOwner().getId() + "/details"));
                            stringObjectMap.put("操作链接", auditLog.getUrl());
                            stringObjectMap.put("操作时间", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(auditLog.getDateCreated()));
                            stringObjectMap.put("操作状态", auditLog.getStatus().equals(AuditLog.Status.SUCCESS) ? new ComponentCell(new Icon("check circle").setColor(Color.GREEN)) : new ComponentCell(new Icon("warning circle").setColor(Color.RED)));
                            stringObjectMap.put("备注信息", auditLog.getComments());
                        }))
                        .addComponent(new Segment().addComponent(new ContentHeader("操作内容", Size.SMALL).setDividing()).addComponent(new P(auditLog.getContent())))
                        .addLink("返回", QXCMP_BACKEND_URL + "/audit")
                ))
                .setBreadcrumb("控制台", "", "系统工具", "tools", "系统日志", "audit", "日志详情")
                .build()).orElse(page(new Overview(new IconHeader("日志不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/audit")).build());
    }
}
