package com.qxcmp.advertisement;

import com.qxcmp.security.SecurityLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.advertisement.AdvertisementModule.ADMIN_ADVERTISEMENT_URL;

/**
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@Configuration
public class AdvertisementModuleSecurity extends WebSecurityConfigurerAdapter implements SecurityLoader {

    public static final String PRIVILEGE_ADMIN_ADVERTISEMENT = "广告管理权限";
    public static final String PRIVILEGE_ADMIN_ADVERTISEMENT_DESCRIPTION = "可以管理平台广告";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_ADVERTISEMENT_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_ADVERTISEMENT_URL + "/**").hasRole(PRIVILEGE_ADMIN_ADVERTISEMENT)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
