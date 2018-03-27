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
    public static final String PRIVILEGE_ADMIN_USER = "用户管理权限";
    public static final String PRIVILEGE_ADMIN_USER_DESCRIPTION = "可以查看用户信息";
    public static final String PRIVILEGE_ADMIN_LOG = "系统日志管理权限";
    public static final String PRIVILEGE_ADMIN_LOG_DESCRIPTION = "可以管理系统日志";
    public static final String PRIVILEGE_ADMIN_SETTINGS = "设置中心进入权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_DESCRIPTION = "可以进入设置中心，还需要具有具体设置的权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_SITE = "网站设置权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_SITE_DESCRIPTION = "可以修改网站设置";
    public static final String PRIVILEGE_ADMIN_SETTINGS_DICTIONARY = "系统字典设置权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_DICTIONARY_DESCRIPTION = "可以修改系统字典";
    public static final String PRIVILEGE_ADMIN_SETTINGS_EMAIL = "邮件服务设置权限";
    public static final String PRIVILEGE_ADMIN_SETTINGS_EMAIL_DESCRIPTION = "可以修改邮件服务设置";
    public static final String PRIVILEGE_ADMIN_MESSAGE_SMS = "短信服务设置权限";
    public static final String PRIVILEGE_ADMIN_MESSAGE_SMS_DESCRIPTION = "可以修改短信服务设置";
    public static final String PRIVILEGE_ADMIN_MESSAGE_SMS_SEND = "短信服务发送权限";
    public static final String PRIVILEGE_ADMIN_MESSAGE_SMS_SEND_DESCRIPTION = "可以发送短信到指定用户";
    public static final String PRIVILEGE_ADMIN_TOOL = "系统工具使用权限";
    public static final String PRIVILEGE_ADMIN_TOOL_DESCRIPTION = "可以使用系统工具，还需要具有具体工具的使用权限";
    public static final String PRIVILEGE_ADMIN_SECURITY = "系统安全管理权限";
    public static final String PRIVILEGE_ADMIN_SECURITY_DESCRIPTION = "可以修改系统安全配置";
    public static final String PRIVILEGE_ADMIN_STATISTIC = "统计信息查看权限";
    public static final String PRIVILEGE_ADMIN_STATISTIC_DESCRIPTION = "可以查看网站统计信息";
    public static final String PRIVILEGE_FINANCE = "财务管理权限";
    public static final String PRIVILEGE_FINANCE_DESCRIPTION = "可以查看财务管理入口页面";
    public static final String PRIVILEGE_FINANCE_DEPOSIT = "充值订单管理权限";
    public static final String PRIVILEGE_FINANCE_DEPOSIT_DESCRIPTION = "可以查看充值订单页面";
    public static final String PRIVILEGE_FINANCE_WALLET = "用户钱包管理权限";
    public static final String PRIVILEGE_FINANCE_WALLET_DESCRIPTION = "可以查看用户钱包信息";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_FINANCE_URL + "/deposit").hasRole(PRIVILEGE_FINANCE_DEPOSIT)
                .antMatchers(ADMIN_FINANCE_URL + "/wallet").hasRole(PRIVILEGE_FINANCE_WALLET)
                .antMatchers(ADMIN_FINANCE_URL + "/**").hasRole(PRIVILEGE_FINANCE)
                .antMatchers(ADMIN_SECURITY_URL + "/**").hasRole(PRIVILEGE_ADMIN_SECURITY)
                .antMatchers(ADMIN_AUDIT_LOG_URL + "/**").hasRole(PRIVILEGE_ADMIN_LOG)
                .antMatchers(ADMIN_STATISTIC_URL + "/**").hasRole(PRIVILEGE_ADMIN_STATISTIC)
                .antMatchers(ADMIN_TOOLS_URL + "/**").hasRole(PRIVILEGE_ADMIN_TOOL)
                .antMatchers(ADMIN_USER_URL + "/**").hasRole(PRIVILEGE_ADMIN_USER)
                .antMatchers(ADMIN_SETTINGS_SITE_URL + "/**").hasRole(PRIVILEGE_ADMIN_SETTINGS_SITE)
                .antMatchers(ADMIN_SETTINGS_DICTIONARY_URL + "/**").hasRole(PRIVILEGE_ADMIN_SETTINGS_DICTIONARY)
                .antMatchers(ADMIN_SETTINGS_EMAIL_URL + "/**").hasRole(PRIVILEGE_ADMIN_SETTINGS_EMAIL)
                .antMatchers(ADMIN_SMS_URL + "/send").hasRole(PRIVILEGE_ADMIN_MESSAGE_SMS_SEND)
                .antMatchers(ADMIN_SMS_URL + "/**").hasRole(PRIVILEGE_ADMIN_MESSAGE_SMS)
                .antMatchers(ADMIN_SETTINGS_URL + "/**").hasRole(PRIVILEGE_ADMIN_SETTINGS)
                .antMatchers(ADMIN_URL + "/**").hasRole(PRIVILEGE_SYSTEM_ADMIN)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
