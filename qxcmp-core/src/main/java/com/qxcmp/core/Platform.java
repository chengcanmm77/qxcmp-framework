package com.qxcmp.core;

import org.springframework.stereotype.Component;

/**
 * 平台属性
 *
 * @author Aaric
 */
@Component
public class Platform {

    public static class Pages {
        public static final String ACCOUNT = "${platform.pages.account}";
        public static final String LOGIN = "${platform.pages.login}";
        public static final String ADMIN = "${platform.pages.admin.home}";
        public static final String ABOUT = "${platform.pages.admin.about}";
    }

}
