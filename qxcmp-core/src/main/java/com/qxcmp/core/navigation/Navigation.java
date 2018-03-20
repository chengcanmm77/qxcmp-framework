package com.qxcmp.core.navigation;


import com.qxcmp.web.view.support.AnchorTarget;

/**
 * @author Aaric
 */
public class Navigation extends AbstractNavigation {

    public Navigation(String id, String title) {
        super(id, title);
    }

    public Navigation(String id, String title, String url) {
        super(id, title, url);
    }

    public Navigation(String id, String title, String url, AnchorTarget target) {
        super(id, title, url, target);
    }

    public static Navigation of(String id, String title) {
        return of(id, title, "");
    }

    public static Navigation of(String id, String title, String url) {
        return of(id, title, url, AnchorTarget.SELF);
    }

    public static Navigation of(String id, String title, String url, AnchorTarget target) {
        return new Navigation(id, title, url, target);
    }
}
