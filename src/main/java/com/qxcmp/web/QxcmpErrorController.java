package com.qxcmp.web;

import com.qxcmp.exception.BlackListException;
import com.qxcmp.web.view.elements.grid.Col;
import com.qxcmp.web.view.elements.grid.Grid;
import com.qxcmp.web.view.elements.html.P;
import com.qxcmp.web.view.support.Alignment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 平台错误页面路由抽象类
 * <p>
 * 需要实现自己的错误页面路由，以添加自己的页面内容
 *
 * @author aaric
 */
@Controller
@RequestMapping("/error")
public class QxcmpErrorController extends QxcmpController implements ErrorController {

    private ErrorAttributes errorAttributes;

    @RequestMapping("")
    public ModelAndView handleError(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> errors = errorAttributes.getErrorAttributes(new ServletWebRequest(request), true);

        HttpStatus status = HttpStatus.valueOf(Integer.parseInt(errors.get("status").toString()));
        String path = errors.get("path").toString();
        String message = errors.get("message").toString();

        if (StringUtils.equals(BlackListException.class.getName(), String.valueOf(errors.get("exception")))) {
            status = HttpStatus.GONE;
            message = "你已经在网页大叔的黑名单中了";
        }

        return pageResolver.resolveByPath(path, request, response)
                .addComponent(new Grid().setTextContainer().setAlignment(Alignment.CENTER).setVerticallyPadded().addItem(new Col()
                        .addComponent(viewHelper.nextWarningOverview(status.toString(), parseStatusCode(status))
                                .addComponent(new P(message))))).build();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @Autowired
    public void setErrorAttributes(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    private String parseStatusCode(HttpStatus status) {

        if (status.equals(HttpStatus.FORBIDDEN)) {
            return "网页叔叔被抓去关起来了";
        }

        if (status.equals(HttpStatus.GONE)) {
            return "网页大叔已经离你远去了";
        }

        if (status.is4xxClientError()) {
            return "网页叔叔搭乘航班去追寻诗和远方了";
        } else if (status.is5xxServerError()) {
            return "服务器君搭乘航班去追寻诗和远方了";
        }

        return status.getReasonPhrase();
    }
}
