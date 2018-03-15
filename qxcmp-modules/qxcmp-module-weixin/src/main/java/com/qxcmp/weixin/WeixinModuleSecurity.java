package com.qxcmp.weixin;

import com.qxcmp.security.SecurityLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.weixin.WeixinModule.ADMIN_WEIXIN_URL;

/**
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 500)
@Configuration
public class WeixinModuleSecurity extends WebSecurityConfigurerAdapter implements SecurityLoader {
    public static final String PRIVILEGE_ADMIN_WEIXIN = "微信平台管理权限";
    public static final String PRIVILEGE_ADMIN_WEIXIN_DESCRIPTION = "可以管理微信公众号配置";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_WEIXIN_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_WEIXIN_URL + "/**").hasRole(PRIVILEGE_ADMIN_WEIXIN)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
