package com.qxcmp.region;

import com.qxcmp.security.SecurityLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.qxcmp.region.RegionModule.ADMIN_REGION_URL;

/**
 * @author Aaric
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 300)
@Configuration
public class RegionModuleSecurity extends WebSecurityConfigurerAdapter implements SecurityLoader {

    public static final String PRIVILEGE_ADMIN_REGION = "地区管理权限";
    public static final String PRIVILEGE_ADMIN_REGION_DESCRIPTION = "可以管理地区配置";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers(ADMIN_REGION_URL + "/**")
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_REGION_URL + "/**").hasRole(PRIVILEGE_ADMIN_REGION)
                .and().formLogin().loginPage("/login").permitAll();
    }
}
