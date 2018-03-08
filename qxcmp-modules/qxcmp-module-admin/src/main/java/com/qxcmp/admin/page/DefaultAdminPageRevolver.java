package com.qxcmp.admin.page;

import org.springframework.stereotype.Service;

/**
 * @author Aaric
 */
@Service
public class DefaultAdminPageRevolver implements AdminPageRevolver {
    @Override
    public Class<? extends AbstractAdminErrorPage> getAdminErrorPage() {
        return DefaultAdminErrorPage.class;
    }

    @Override
    public Class<? extends AbstractAdminOverviewPage> getAdminOverviewPage() {
        return DefaultAdminOverviewPage.class;
    }
}
