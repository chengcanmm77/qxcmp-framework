package com.qxcmp.web.view.page;

import org.springframework.stereotype.Service;

/**
 * 平台默认页面解析服务
 *
 * @author Aaric
 */
@Service
public class DefaultQxcmpPageResolver implements QxcmpPageResolver {

    @Override
    public Class<? extends QxcmpPage> getErrorPage() {
        return DefaultErrorPage.class;
    }

    @Override
    public Class<? extends QxcmpPage> getOverviewPage() {
        return DefaultOverviewPage.class;
    }
}
