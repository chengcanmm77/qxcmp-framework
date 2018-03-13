package com.qxcmp.admin.controller;

import com.qxcmp.admin.QxcmpAdminController;
import com.qxcmp.admin.page.AdminAuditLogDetailsPage;
import com.qxcmp.admin.page.AdminAuditLogTablePage;
import com.qxcmp.audit.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_AUDIT_LOG_URL;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_AUDIT_LOG_URL)
@RequiredArgsConstructor
public class AdminAuditPageController extends QxcmpAdminController {

    private final AuditLogService auditLogService;

    @GetMapping("")
    public ModelAndView logPage(Pageable pageable) {
        return page(AdminAuditLogTablePage.class, auditLogService, pageable);
    }

    @GetMapping("/{id}/details")
    public ModelAndView logDetailsPage(@PathVariable Long id) {
        return entityDetailsPage(AdminAuditLogDetailsPage.class, id, auditLogService);
    }
}
