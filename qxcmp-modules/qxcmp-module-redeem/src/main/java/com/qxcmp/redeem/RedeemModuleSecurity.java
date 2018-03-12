package com.qxcmp.redeem;

import com.qxcmp.security.SecurityLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.redeem.RedeemModule.ADMIN_REDEEM_URL;

/**
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 400)
@Configuration
public class RedeemModuleSecurity extends WebSecurityConfigurerAdapter implements SecurityLoader {

    public static final String PRIVILEGE_ADMIN_REDEEM = "兑换码管理权限";
    public static final String PRIVILEGE_ADMIN_REDEEM_DESCRIPTION = "可以查看平台兑换码";

    public static final String PRIVILEGE_ADMIN_REDEEM_GENERATE = "兑换码生成权限";
    public static final String PRIVILEGE_ADMIN_REDEEM_GENERATE_DESCRIPTION = "可以生成平台兑换码";

    public static final String PRIVILEGE_ADMIN_REDEEM_SETTING = "兑换码设置权限";
    public static final String PRIVILEGE_ADMIN_REDEEM_SETTING_DESCRIPTION = "可以修改平台兑换码设置";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_REDEEM_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_REDEEM_URL + "/generate").hasRole(PRIVILEGE_ADMIN_REDEEM_GENERATE)
                .antMatchers(ADMIN_REDEEM_URL + "/settings").hasRole(PRIVILEGE_ADMIN_REDEEM_SETTING)
                .antMatchers(ADMIN_REDEEM_URL + "/**").hasRole(PRIVILEGE_ADMIN_REDEEM)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
