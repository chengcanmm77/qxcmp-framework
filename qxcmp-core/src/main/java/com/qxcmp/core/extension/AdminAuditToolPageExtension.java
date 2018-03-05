package com.qxcmp.core.extension;

import org.springframework.stereotype.Component;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * @author Aaric
 */
@Component
public class AdminAuditToolPageExtension implements AdminToolPageExtension {
    @Override
    public String getIcon() {
        return "";
    }

    @Override
    public String getTitle() {
        return "系统日志";
    }

    @Override
    public String getUrl() {
        return QXCMP_BACKEND_URL + "/audit";
    }
}
