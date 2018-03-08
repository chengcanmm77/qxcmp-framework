package com.qxcmp.web.view.page;

import org.springframework.stereotype.Service;

/**
 * 平台默认页面解析服务
 *
 * @author Aaric
 */
@Service
public class DefaultPageRevolveServiceImpl implements PageRevolveService {

    @Override
    public Class<? extends AbstractErrorPage> getErrorPage() {
        return DefaultErrorPage.class;
    }

    @Override
    public Class<? extends AbstractOverviewPage> getOverviewPage() {
        return DefaultOverviewPage.class;
    }
}
