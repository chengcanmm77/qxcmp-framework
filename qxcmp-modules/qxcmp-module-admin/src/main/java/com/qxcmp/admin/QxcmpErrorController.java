package com.qxcmp.admin;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * 平台后台错误页面路由抽象类
 * <p>
 * 需要实现自己的错误页面路由，以添加自己的页面内容
 *
 * @author aaric
 */
@Primary
@Controller
@RequestMapping("/error")
@RequiredArgsConstructor
public class QxcmpErrorController extends QxcmpAdminController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @RequestMapping("")
    public ModelAndView handleError(HttpServletRequest request) {
        Map<String, Object> errors = errorAttributes.getErrorAttributes(new ServletWebRequest(request), true);

        String path = errors.get("path").toString();

        if (StringUtils.startsWith(path, ADMIN_URL)) {
            return adminErrorPage(errors);
        } else {
            return errorPage(errors);
        }
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
