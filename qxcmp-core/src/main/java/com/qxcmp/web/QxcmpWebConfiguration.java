package com.qxcmp.web;

import com.qxcmp.account.auth.AuthenticationFilter;
import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.security.PrivilegeAutowired;
import com.qxcmp.statistics.AccessAddressService;
import com.qxcmp.user.UserService;
import com.qxcmp.util.IpAddressResolver;
import com.qxcmp.web.filter.QxcmpFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.EnumSet;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.core.QxcmpConfiguration.QXCMP_LOGIN_URL;
import static com.qxcmp.core.QxcmpSecurityConfiguration.*;


/**
 * 平台内置模块配置
 *
 * @author aaric
 */
@EnableWebSecurity
@PrivilegeAutowired
@Order
@RequiredArgsConstructor
public class QxcmpWebConfiguration extends WebSecurityConfigurerAdapter {

    private final ApplicationContext applicationContext;
    private final SystemConfigService systemConfigService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AccessAddressService accessAddressService;
    private final IpAddressResolver ipAddressResolver;

    /**
     * 增加并发会话控制
     *
     * @return HttpSessionEventPublisher
     */
    @Bean
    public static HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * 平台认证过滤器
     * <p>
     * 该过滤结果平台的认证配置进行相关的认证操作
     *
     * @return 平台认证过滤器
     *
     * @throws Exception
     */
    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(systemConfigService, userService);
        authenticationFilter.setAuthenticationManager(authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return authenticationFilter;
    }

    @Bean
    public QxcmpFilter qxcmpFilter() throws Exception {
        QxcmpFilter qxcmpFilter = new QxcmpFilter(applicationContext, accessAddressService, ipAddressResolver);
        return qxcmpFilter;
    }

    /**
     * Spring Security Bug Fix
     * <p>
     * https://github.com/spring-projects/spring-boot/issues/1048
     * <p>
     * Can't access principle in error page workaround
     *
     * @param springSecurityFilterChain springSecurityFilterChain
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean getSpringSecurityFilterChainBindedToError(
            @Qualifier("springSecurityFilterChain") Filter springSecurityFilterChain) {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(springSecurityFilterChain);
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registration;
    }

    /**
     * work around https://jira.spring.io/browse/SEC-2855
     *
     * @return SessionRegistry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    /**
     * 配置平台用户获取服务
     *
     * @param auth 认证管理器构建器
     *
     * @throws Exception 如果配置失败则平台启动失败
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 配置平台通用安全服务
     * <p>
     * 开启资源访问和API访问权限，以及定义登录操作
     * <p>
     * 拦截所有未知请求
     *
     * @param http Spring Security Http 安全配置
     *
     * @throws Exception 如果配置失败则平台启动失败
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/assets/**", "/login/**", "/api/**", "/account/**").permitAll()
                .antMatchers(QXCMP_BACKEND_URL + "/tools/**").hasRole(PRIVILEGE_ADMIN_TOOL)
                .antMatchers(QXCMP_BACKEND_URL + "/settings/**").hasRole(PRIVILEGE_ADMIN_SETTINGS)
                .antMatchers(QXCMP_BACKEND_URL + "/security/**").hasRole(PRIVILEGE_ADMIN_SECURITY)
                .antMatchers(QXCMP_BACKEND_URL + "/audit/**").hasRole(PRIVILEGE_ADMIN_LOG)
                .antMatchers(QXCMP_BACKEND_URL + "/advertisement/**").hasRole(PRIVILEGE_ADMIN_ADVERTISEMENT)
                .antMatchers(QXCMP_BACKEND_URL + "/redeem/**").hasRole(PRIVILEGE_ADMIN_REDEEM)
                .antMatchers(QXCMP_BACKEND_URL + "/spider/**").hasRole(PRIVILEGE_ADMIN_SPIDER)
                .antMatchers(QXCMP_BACKEND_URL + "/link/**").hasRole(PRIVILEGE_ADMIN_LINK)
                .antMatchers(QXCMP_BACKEND_URL + "/user/**/role/**").hasRole(PRIVILEGE_USER_ROLE)
                .antMatchers(QXCMP_BACKEND_URL + "/user/**/status/**").hasRole(PRIVILEGE_USER_STATUS)
                .antMatchers(QXCMP_BACKEND_URL + "/user/**").hasRole(PRIVILEGE_USER)
                .antMatchers(QXCMP_BACKEND_URL + "/finance/wallet/**").hasRole(PRIVILEGE_FINANCE_WALLET_MANAGEMENT)
                .antMatchers(QXCMP_BACKEND_URL + "/finance/weixin/**").hasRole(PRIVILEGE_FINANCE_WEIXIN)
                .antMatchers(QXCMP_BACKEND_URL + "/finance/**").hasRole(PRIVILEGE_FINANCE)
                .antMatchers(QXCMP_BACKEND_URL + "/message/email/init/**").hasRole(PRIVILEGE_MESSAGE_EMAIL_CONFIG)
                .antMatchers(QXCMP_BACKEND_URL + "/message/email/send/**").hasRole(PRIVILEGE_MESSAGE_EMAIL_SEND)
                .antMatchers(QXCMP_BACKEND_URL + "/message/sms/init/**").hasRole(PRIVILEGE_MESSAGE_SMS_CONFIG)
                .antMatchers(QXCMP_BACKEND_URL + "/message/sms/send/**").hasRole(PRIVILEGE_MESSAGE_SMS_SEND)
                .antMatchers(QXCMP_BACKEND_URL + "/message/inner/message/**").hasRole(PRIVILEGE_MESSAGE_INNER_MESSAGE)
                .antMatchers(QXCMP_BACKEND_URL + "/message/site/notification/**").hasRole(PRIVILEGE_MESSAGE_SITE_NOTIFICATION)
                .antMatchers(QXCMP_BACKEND_URL + "/message/**").hasRole(PRIVILEGE_MESSAGE)
                .antMatchers(QXCMP_BACKEND_URL + "/news/channel/**").hasRole(PRIVILEGE_NEWS_CHANNEL)
                .antMatchers(QXCMP_BACKEND_URL + "/news/article/**/preview").hasAnyRole(PRIVILEGE_NEWS_ARTICLE_AUDIT, PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)
                .antMatchers(QXCMP_BACKEND_URL + "/news/article/**/auditing", QXCMP_BACKEND_URL + "/news/article/**/audit", QXCMP_BACKEND_URL + "/news/article/**/publish", QXCMP_BACKEND_URL + "/news/article/**/reject").hasRole(PRIVILEGE_NEWS_ARTICLE_AUDIT)
                .antMatchers(QXCMP_BACKEND_URL + "/news/article/**/published", QXCMP_BACKEND_URL + "/news/article/**/disabled", QXCMP_BACKEND_URL + "/news/article/**/enable", QXCMP_BACKEND_URL + "/news/article/**/disable", QXCMP_BACKEND_URL + "/news/article/**/remove").hasRole(PRIVILEGE_NEWS_ARTICLE_MANAGEMENT)
                .antMatchers(QXCMP_BACKEND_URL + "/news/user/**").hasRole(PRIVILEGE_NEWS)
                .antMatchers(QXCMP_BACKEND_URL + "/news/**").hasRole(PRIVILEGE_NEWS)
                .antMatchers(QXCMP_BACKEND_URL + "/weixin/settings/**").hasRole(PRIVILEGE_WEIXIN_SETTINGS)
                .antMatchers(QXCMP_BACKEND_URL + "/weixin/menu/**").hasRole(PRIVILEGE_WEIXIN_MENU)
                .antMatchers(QXCMP_BACKEND_URL + "/weixin/material/**").hasRole(PRIVILEGE_WEIXIN_MATERIAL)
                .antMatchers(QXCMP_BACKEND_URL + "/weixin/**").hasRole(PRIVILEGE_WEIXIN)
                .antMatchers(QXCMP_BACKEND_URL + "/mall/order/**").hasRole(PRIVILEGE_MALL_ORDER)
                .antMatchers(QXCMP_BACKEND_URL + "/mall/commodity/**").hasRole(PRIVILEGE_MALL_COMMODITY)
                .antMatchers(QXCMP_BACKEND_URL + "/mall/store/**").hasRole(PRIVILEGE_MALL_STORE)
                .antMatchers(QXCMP_BACKEND_URL + "/mall/settings/**").hasRole(PRIVILEGE_MALL_SETTINGS)
                .antMatchers(QXCMP_BACKEND_URL + "/mall/user/**").hasRole(PRIVILEGE_MALL)
                .antMatchers(QXCMP_BACKEND_URL + "/mall/**").hasRole(PRIVILEGE_MALL)
                .antMatchers(QXCMP_BACKEND_URL + "/statistic/settings/**").hasRole(PRIVILEGE_STATISTIC_SETTINGS)
                .antMatchers(QXCMP_BACKEND_URL + "/statistic/**").hasRole(PRIVILEGE_STATISTIC)
                .antMatchers(QXCMP_BACKEND_URL + "/**").hasRole(PRIVILEGE_SYSTEM_ADMIN)
                .anyRequest().authenticated()
                .and()
                .csrf()
                .ignoringAntMatchers("/api/**")
                .and().formLogin().loginPage(QXCMP_LOGIN_URL).permitAll()
                .and().logout()
                .and().sessionManagement()
                .maximumSessions(systemConfigService.getInteger(QxcmpSystemConfig.SESSION_MAX_ACTIVE_COUNT).orElse(QxcmpSystemConfig.SESSION_MAX_ACTIVE_COUNT_DEFAULT))
                .maxSessionsPreventsLogin(systemConfigService.getBoolean(QxcmpSystemConfig.SESSION_MAX_PREVENT_LOGIN).orElse(QxcmpSystemConfig.SESSION_MAX_PREVENT_LOGIN_DEFAULT))
                .expiredUrl("/login?expired")
                .sessionRegistry(sessionRegistry())
                .and().and().addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling();
    }
}
