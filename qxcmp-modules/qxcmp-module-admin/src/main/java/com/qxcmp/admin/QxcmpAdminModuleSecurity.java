package com.qxcmp.admin;

import com.qxcmp.security.SecurityLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.admin.QxcmpAdminModule.ADMIN_URL;

/**
 * 后台安全配置
 *
 * @author Aaric
 */
@Order(Ordered.LOWEST_PRECEDENCE - 10)
@Configuration
public class QxcmpAdminModuleSecurity extends WebSecurityConfigurerAdapter implements SecurityLoader {

    public static final String PRIVILEGE_SYSTEM_ADMIN = "系统管理员权限";
    public static final String PRIVILEGE_SYSTEM_ADMIN_DESCRIPTION = "可以进入后台系统";

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
