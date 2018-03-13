package com.qxcmp.security;

import com.qxcmp.account.auth.AuthenticationFilter;
import com.qxcmp.config.SystemConfigService;
import com.qxcmp.core.QxcmpSystemConfig;
import com.qxcmp.user.UserService;
import com.qxcmp.web.filter.QxcmpFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_LOGIN_URL;


/**
 * 平台安全配置
 *
 * @author aaric
 */
@Order
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationContext applicationContext;
    private final SystemConfigService systemConfigService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;


    /**
     * 增加并发会话控制
     *
     * @return HttpSessionEventPublisher
     */
    @Bean
    public static HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public QxcmpFilter qxcmpFilter() {
        return new QxcmpFilter(applicationContext);
    }

    /**
     * 平台认证过滤器
     * <p>
     * 该过滤结果平台的认证配置进行相关的认证操作
     *
     * @return 平台认证过滤器
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


    /**
     * work around https://jira.spring.io/browse/SEC-2855
     *
     * @return SessionRegistry
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 配置平台用户获取服务
     *
     * @param auth 认证管理器构建器
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
     * @throws Exception 如果配置失败则平台启动失败
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/assets/**", "/login/**", "/api/**", "/account/**").permitAll()
                .anyRequest().authenticated().and()
                .csrf()
                .ignoringAntMatchers("/api/**")
                .and().formLogin().loginPage(QXCMP_LOGIN_URL).permitAll()
                .and().logout()
                .and().sessionManagement()
                .maximumSessions(systemConfigService.getInteger(QxcmpSystemConfig.SESSION_MAX_ACTIVE_COUNT).orElse(QxcmpSystemConfig.SESSION_MAX_ACTIVE_COUNT_DEFAULT))
                .maxSessionsPreventsLogin(systemConfigService.getBoolean(QxcmpSystemConfig.SESSION_PREVENT_LOGIN).orElse(QxcmpSystemConfig.SESSION_PREVENT_LOGIN_DEFAULT))
                .expiredUrl("/login?expired")
                .sessionRegistry(sessionRegistry())
                .and().and().addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling();
    }
}
