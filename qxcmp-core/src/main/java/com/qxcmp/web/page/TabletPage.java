package com.qxcmp.web.page;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TabletPage extends BaseFrontendPage {
    public TabletPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
