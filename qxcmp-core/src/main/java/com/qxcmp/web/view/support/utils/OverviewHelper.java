package com.qxcmp.web.view.support.utils;

import com.qxcmp.web.view.elements.header.AbstractHeader;
import com.qxcmp.web.view.elements.header.IconHeader;
import com.qxcmp.web.view.elements.icon.Icon;
import com.qxcmp.web.view.views.Overview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class OverviewHelper {

    public Overview nextOverview(Icon icon, String title, String subTitle) {
        final AbstractHeader header = new IconHeader(title, icon);

        if (StringUtils.isNotBlank(subTitle)) {
            header.setSubTitle(subTitle);
        }

        return new Overview(header);
    }
}
