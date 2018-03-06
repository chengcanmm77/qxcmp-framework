package com.qxcmp.account.auth;

import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpConfiguration;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.user.User;
import com.qxcmp.user.UserService;
import com.qxcmp.util.Captcha;
import com.qxcmp.util.CaptchaService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

/**
 * 认证过滤器，属于框架基本功能，供平台和前端共同使用
 * <p>
 * 在{@link QxcmpConfiguration}中配置
 * <p>
 * 平台认证方式如下：
 * <ol>
 * <li>检查用户是否存在</li>
 * <li>如果不存在则返回错误码</li>
 * <li>检查是否开启账户锁定</li>
 * <li>如果开启账户锁定，并且登录时间已经超过账户锁定阈值，则解锁账户</li>
 * <li>检查是否开始账户过期</li>
 * <li>如果开启账户过期，并且账户已经过期，则使账户过期（作废账户，无法恢复）</li>
 * <li>检查是否开始密码过期</li>
 * <li>如果开启密码过期，并且密码已经过期，则返回密码过期错误码。可以通过密码重置，重新设置密码</li>
 * <li>检查验证码</li>
 * <li>如果当前session已经设置了验证码，则检查验证码是否正确</li>
 * </ol>
 *
 * @author aaric
 */
@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private SystemConfigService systemConfigService;
    private UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        checkAccountStatus(request);
        checkCaptcha(request);
        return super.attemptAuthentication(request, response);
    }

    /**
     * 检查账户状态是否正常
     *
     * @param request 认证请求
     */
    private void checkAccountStatus(HttpServletRequest request) {

        String userLoginId = request.getParameter("username");

        User user = userService.findById(userLoginId)
                .orElseThrow(() -> new UsernameNotFoundException("Authentication.userNotFound"));

        checkAccountLock(userLoginId, user);
        checkAccountExpire(userLoginId, user);
        checkCredentialExpire(userLoginId, user);
    }

    /**
     * 检查账户锁定情况
     * <p>
     * 如果满足以下全部条件，则解锁账户： <ol> <li>开启了账户锁定功能</li> <li>账户锁定时间超过了 分钟</li> </ol>
     *
     * @param userLoginId 账户登录名
     * @param user        账户信息
     */
    private void checkAccountLock(String userLoginId, User user) {
        Date dateLock = user.getDateLock();
        if (Objects.nonNull(dateLock)) {
            boolean enableAccountLock = systemConfigService.getBoolean(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_LOCK).orElse(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_LOCK_DEFAULT);
            if (enableAccountLock) {
                int lockDuration = systemConfigService.getInteger(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_LOCK_DURATION).orElse(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_LOCK_DURATION_DEFAULT) * 1000 * 60;
                if (System.currentTimeMillis() - dateLock.getTime() > lockDuration) {
                    userService.update(userLoginId, u -> u.setAccountNonLocked(true));
                }
            }
        }
    }

    /**
     * 检查账户过期情况
     * <p>
     * 如果满足以下全部条件，则使账户过期 <ol> <li>开启了账户过期功能</li> <li>账户上次登陆时间超过了 天</li> </ol>
     *
     * @param userLoginId 账户登录名
     * @param user        账户信息
     */
    private void checkAccountExpire(String userLoginId, User user) {
        Date dateLogin = user.getDateLogin();
        if (Objects.nonNull(dateLogin)) {
            boolean enableAccountExpire = systemConfigService.getBoolean(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_EXPIRE).orElse(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_EXPIRE_DEFAULT);
            if (enableAccountExpire) {
                int expireDuration = systemConfigService.getInteger(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_EXPIRE_DURATION).orElse(QxcmpSystemConfig.AUTHENTICATION_ACCOUNT_EXPIRE_DURATION_DEFAULT) * 1000 * 60 * 60 * 24;
                if (System.currentTimeMillis() - dateLogin.getTime() > expireDuration) {
                    userService.update(userLoginId, u -> u.setAccountNonExpired(false));
                }
            }
        }
    }

    /**
     * 检查账户密码是否过期
     * <p>
     * 如果满足以下条件，则使账户密码过期 <ol> <li>开启了密码过期功能</li> <li>账户上次密码修改时间超过了 天</li> </ol>
     *
     * @param userLoginId 账户登录名
     * @param user        账户信息
     */
    private void checkCredentialExpire(String userLoginId, User user) {
        Date dateCredential = user.getDatePasswordModified();
        if (Objects.isNull(dateCredential)) {
            dateCredential = user.getDateCreated();
        }
        boolean enableCredentialExpire = systemConfigService.getBoolean(QxcmpSystemConfig.AUTHENTICATION_CREDENTIAL_EXPIRE).orElse(QxcmpSystemConfig.AUTHENTICATION_CREDENTIAL_EXPIRE_DEFAULT);
        if (enableCredentialExpire) {
            int expireCredentialDuration = systemConfigService.getInteger(QxcmpSystemConfig.AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION).orElse(QxcmpSystemConfig.AUTHENTICATION_CREDENTIAL_EXPIRE_DURATION_DEFAULT) * 1000 * 60 * 60 * 24;
            if (System.currentTimeMillis() - dateCredential.getTime() > expireCredentialDuration) {
                userService.update(userLoginId, u -> u.setCredentialsNonExpired(false));
            }
        }
    }

    /**
     * 检查验证码是否正确
     *
     * @param request 认证请求
     */
    private void checkCaptcha(HttpServletRequest request) {
        if (request.getSession().getAttribute(AuthenticationFailureHandler.AUTHENTICATION_SHOW_CAPTCHA) != null) {
            boolean showCaptcha = (boolean) request.getSession().getAttribute(AuthenticationFailureHandler.AUTHENTICATION_SHOW_CAPTCHA);
            if (showCaptcha) {
                if (request.getSession().getAttribute(CaptchaService.CAPTCHA_SESSION_ATTR) != null) {
                    Captcha sessionCaptcha = (Captcha) request.getSession().getAttribute(CaptchaService.CAPTCHA_SESSION_ATTR);
                    if (request.getParameter(CaptchaService.CAPTCHA_SESSION_ATTR) != null) {
                        String userCaptcha = request.getParameter(CaptchaService.CAPTCHA_SESSION_ATTR);
                        if (!userCaptcha.equalsIgnoreCase(sessionCaptcha.getCaptcha())) {
                            throw new SessionAuthenticationException("Authentication.captchaInvalid");
                        }
                    } else {
                        throw new SessionAuthenticationException("Authentication.captchaInvalid");
                    }
                } else {
                    throw new SessionAuthenticationException("Authentication.captchaNull");
                }
            }
        }
    }
}