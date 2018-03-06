package com.qxcmp.account.auth;

import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 认证失败处理器，属于框架基本功能，供平台和前端共同使用
 * <p>
 * 配置在认证过滤器{@link AuthenticationFilter} 中，负责对认证失败进行处理
 * <p>
 * 处理流程如下： <ol> <li>如果用户状态不正确，用过sessionAttribute设置状态错误码</li> <li>计算session认证错误次数</li> <li>如果错误次数超过验证码阈值，则生成验证码</li>
 * <li>如果开启账户锁定，并且错误次数超过锁定阈值，则锁定该账户</li> </ol>
 *
 * @author aaric
 */
@Component
@AllArgsConstructor
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * 会话属性，用于标识是否生成验证码
     */
    public static final String AUTHENTICATION_SHOW_CAPTCHA = "authenticationShowCaptcha";

    /**
     * 会话属性，用于存放认证错误消息国际化代码
     */
    public static final String AUTHENTICATION_ERROR_MESSAGE = "authenticationMessage";

    /**
     * 会话属性，用于存放当前会话认证失败次数
     */
    private static final String AUTHENTICATION_ERROR_TIMES = "authenticationErrorTimes";

    private SystemConfigService systemConfigService;

    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, exception);

        setExceptionMessage(request, exception);

        setFailedCount(request);

        calculateCaptcha(request);

        calculateAccountLock(request);
    }

    /**
     * 根据认证失败错误类型设置会话错误消息国际化代码
     *
     * @param request   会话请求
     * @param exception 认证错误类型
     */
    private void setExceptionMessage(HttpServletRequest request, AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            request.getSession().setAttribute(AUTHENTICATION_ERROR_MESSAGE, "Authentication.badCredential");
        } else if (exception instanceof DisabledException) {
            request.getSession().setAttribute(AUTHENTICATION_ERROR_MESSAGE, "Authentication.accountDisabled");
        } else if (exception instanceof LockedException) {
            request.getSession().setAttribute(AUTHENTICATION_ERROR_MESSAGE, "Authentication.accountLocked");
        } else if (exception instanceof AccountExpiredException) {
            request.getSession().setAttribute(AUTHENTICATION_ERROR_MESSAGE, "Authentication.accountExpired");
        } else if (exception instanceof CredentialsExpiredException) {
            request.getSession().setAttribute(AUTHENTICATION_ERROR_MESSAGE, "Authentication.credentialExpired");
        } else {
            request.getSession().setAttribute(AUTHENTICATION_ERROR_MESSAGE, exception.getMessage());
        }
    }

    /**
     * 设置会话认证失败次数加一
     * <p>
     * 失败次数可以用于判断是否生成验证码，以及是否锁定账户
     *
     * @param request 认证请求
     */
    private void setFailedCount(HttpServletRequest request) {
        if (request.getSession().getAttribute(AUTHENTICATION_ERROR_TIMES) != null) {
            int failedCount = (int) request.getSession().getAttribute(AUTHENTICATION_ERROR_TIMES);
            request.getSession().setAttribute(AUTHENTICATION_ERROR_TIMES, ++failedCount);
        } else {
            request.getSession().setAttribute(AUTHENTICATION_ERROR_TIMES, 1);
        }
    }

    /**
     * 计算是否生成验证码
     * <p>
     * 如果认证失败次数超过 {@link QxcmpSystemConfig#AUTHENTICATION_CAPTCHA_THRESHOLD} 以后则生成验证码
     *
     * @param request 认证请求
     */
    private void calculateCaptcha(HttpServletRequest request) {

        int failedCount = (int) request.getSession().getAttribute(AUTHENTICATION_ERROR_TIMES);

        if (failedCount >= systemConfigService.getInteger(QxcmpSystemConfig.AUTHENTICATION_CAPTCHA_THRESHOLD).orElse(QxcmpSystemConfig.AUTHENTICATION_CAPTCHA_THRESHOLD_DEFAULT)) {
            request.getSession().setAttribute(AUTHENTICATION_SHOW_CAPTCHA, true);
        }
    }

    /**
     * 计算是否锁定账户
     * <p>
     * 如果开启了账户锁定功能，并且认证失败次数超过了 {@link QxcmpSystemConfig#AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD}
     * 则锁定该账户
     *
     * @param request 认证请求
     */
    private void calculateAccountLock(HttpServletRequest request) {

        int failedCount = (int) request.getSession().getAttribute(AUTHENTICATION_ERROR_TIMES);

        boolean lockAccount = systemConfigService.getBoolean(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_LOCK).orElse(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_LOCK_DEFAULT);

        if (lockAccount && failedCount >= systemConfigService.getInteger(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD).orElse(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_LOCK_THRESHOLD_DEFAULT)) {
            userService.update(request.getParameter("username"), user -> {
                user.setAccountNonLocked(false);
                user.setDateLock(new Date());
            });
        }
    }
}