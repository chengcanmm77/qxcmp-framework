package com.qxcmp.article.page;

import com.qxcmp.admin.page.AbstractQxcmpAdminPage;

/**
 * @author Aaric
 */
public abstract class AbstractNewsPage extends AbstractQxcmpAdminPage {

    private static final int MAX_COUNT = 99;

    protected void setMenuBadge(String menuId, Long count) {
        if (count > 0) {
            if (count > MAX_COUNT) {
                setMenuBadge(menuId, MAX_COUNT + "+");
            } else {
                setMenuBadge(menuId, count);
            }
        }
    }
}
