package com.qxcmp.link;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.link.LinkModule.ADMIN_LINK_URL;

/**
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 200)
@Configuration
public class LinkModuleSecurity extends WebSecurityConfigurerAdapter {

    public static final String PRIVILEGE_ADMIN_LINK = "广告管理权限";
    public static final String PRIVILEGE_ADMIN_LINK_DESCRIPTION = "可以管理平台广告";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_LINK_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_LINK_URL + "/**").hasRole(PRIVILEGE_ADMIN_LINK)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
