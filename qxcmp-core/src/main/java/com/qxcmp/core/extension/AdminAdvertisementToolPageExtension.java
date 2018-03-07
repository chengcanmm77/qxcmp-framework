package com.qxcmp.core.extension;

import org.springframework.stereotype.Component;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_ADMIN_URL;

/**
 * @author Aaric
 */
@Component
public class AdminAdvertisementToolPageExtension implements AdminToolPageExtension {
    @Override
    public String getIcon() {
        return "";
    }

    @Override
    public String getTitle() {
        return "广告管理";
    }

    @Override
    public String getUrl() {
        return QXCMP_ADMIN_URL + "/advertisement";
    }
}
