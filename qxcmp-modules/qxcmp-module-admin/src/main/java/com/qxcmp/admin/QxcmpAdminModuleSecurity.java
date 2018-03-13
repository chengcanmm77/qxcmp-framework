package com.qxcmp.admin;

import com.qxcmp.security.SecurityLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.admin.QxcmpAdminModule.*;

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
    public static final String PRIVILEGE_ADMIN_LOG = "系统日志管理权限";
    public static final String PRIVILEGE_ADMIN_LOG_DESCRIPTION = "可以管理系统日志";
    public static final String PRIVILEGE_ADMIN_SETTINGS = "设置中心进入权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_DESCRIPTION = "可以进入设置中心，还需要具有具体设置的权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_SITE = "网站设置权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_SITE_DESCRIPTION = "可以修改网站设置";
    public static final String PRIVILEGE_ADMIN_SETTINGS_DICTIONARY = "系统字典设置权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_DICTIONARY_DESCRIPTION = "可以修改系统字典";
    public static final String PRIVILEGE_ADMIN_TOOL = "系统工具使用权限";
    public static final String PRIVILEGE_ADMIN_TOOL_DESCRIPTION = "可以使用系统工具，还需要具有具体工具的使用权限";


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_AUDIT_LOG_URL).hasRole(PRIVILEGE_ADMIN_LOG)
                .antMatchers(ADMIN_TOOLS_URL).hasRole(PRIVILEGE_ADMIN_TOOL)
                .antMatchers(ADMIN_SETTINGS_SITE_URL).hasRole(PRIVILEGE_ADMIN_SETTINGS_SITE)
                .antMatchers(ADMIN_SETTINGS_DICTIONARY_URL).hasRole(PRIVILEGE_ADMIN_SETTINGS_DICTIONARY)
                .antMatchers(ADMIN_SETTINGS_URL).hasRole(PRIVILEGE_ADMIN_SETTINGS)
                .antMatchers(ADMIN_URL + "/**").hasRole(PRIVILEGE_SYSTEM_ADMIN)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
