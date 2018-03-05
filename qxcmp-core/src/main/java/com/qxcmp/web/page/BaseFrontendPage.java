package com.qxcmp.web.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseFrontendPage extends AbstractPage {

    public BaseFrontendPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
