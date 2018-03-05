package com.qxcmp.web.auth;

import com.qxcmp.config.SystemConfigService;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.qxcmp.core.QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_SESSION_TIMEOUT;
import static com.qxcmp.core.QxcmpSystemConfigConfiguration.SYSTEM_CONFIG_SESSION_TIMEOUT_DEFAULT_VALUE;

/**
 * 认证成功处理器，属于框架基本功能，供平台和前端共同使用
 * <p>
 * 配置在认证过滤器{@link AuthenticationFilter}中，负责对认证成功进行处理
 * <p>
 * 认证成功流程如下： <ol> <li>设置用户最后一次登录日期</li> </ol>
 *
 * @author aaric
 */
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    private final SystemConfigService systemConfigService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        if (StringUtils.isNotBlank(request.getParameter("callback"))) {
            redirectStrategy.sendRedirect(request, response, request.getParameter("callback"));
        }

        request.getSession().setMaxInactiveInterval(systemConfigService.getInteger(SYSTEM_CONFIG_SESSION_TIMEOUT).orElse(SYSTEM_CONFIG_SESSION_TIMEOUT_DEFAULT_VALUE));

        try {
            String username = request.getParameter("username");
            userService.update(userService.findByUsername(username).map(User::getId).orElse(""), user -> user.setDateLogin(new Date()));
        } catch (Exception ignored) {

        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}