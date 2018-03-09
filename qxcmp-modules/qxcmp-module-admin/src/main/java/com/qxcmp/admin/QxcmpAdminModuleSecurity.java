package com.qxcmp.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;
import static com.qxcmp.admin.QxcmpAdminModule.PRIVILEGE_SYSTEM_ADMIN;

/**
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@Configuration
public class QxcmpAdminModuleSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_URL + "/**").hasRole(PRIVILEGE_SYSTEM_ADMIN)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
