package com.qxcmp.spider;

import com.qxcmp.security.SecurityLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.spider.SpiderModule.ADMIN_SPIDER_URL;

/**
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 600)
@Configuration
public class SpiderModuleSecurity extends WebSecurityConfigurerAdapter implements SecurityLoader {

    public static final String PRIVILEGE_ADMIN_SPIDER = "蜘蛛管理权限";
    public static final String PRIVILEGE_ADMIN_SPIDER_DESCRIPTION = "可以管理平台蜘蛛";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_SPIDER_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_SPIDER_URL + "/**").hasRole(PRIVILEGE_ADMIN_SPIDER)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
